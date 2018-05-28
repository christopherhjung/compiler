package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.FREE_MEM;

import compiler.AsmBuilder.Asm;
import parser.Scope;
import parser.Scope.VarAnchor;
import parser.Statement;

public class Compiler {

	private Scope scope = null;
	private CompilerAsmBuilder asm;

	public Compiler()
	{
		asm = new CompilerAsmBuilder();
	}

	public String compile( Statement statement )
	{
		StringBuilder sb = new StringBuilder();
		compileImpl( statement );
		List<AbstractCommand> result = asm.getCommands();
		for ( AbstractCommand command : result )
		{
			sb.append( command );
		}

		return sb.toString();
	}

	private void compileImpl( Statement statement )
	{
		if ( statement.is( "block" ) )
		{
			compileBlock( statement );
		}
		else if ( statement.is( "assign" ) )
		{
			compileAssign( statement );
		}
		else if ( statement.is( "chain" ) )
		{
			Statement method = statement.get( "left" );
			Statement vector = statement.get( "right" );
			String methodName = method.get( "value", String.class );
			Statement[] params = vector.get( "value", Statement[].class );

			// Scope nextScope = new Scope( scope );

			boolean success = scope.allocRegister( 25, 4 * params.length );

			if ( !success )
			{
				throw new RuntimeException( "faaiaia" );
			}

			for ( int i = 0 ; i < params.length ; i++ )
			{
				Statement param = params[i];
				load( param, 25 - i );
			}

			asm.rcall( methodName );
			scope.unuseAll();
		}
	}

	public int load( Statement statement )
	{
		return load( statement, -1 );
	}

	public int ensureRegister( int defaultRegister, int size )
	{
		if ( defaultRegister >= 0 )
		{
			return defaultRegister;
		}

		return scope.allocRegister( size );
	}

	public int load( Statement statement, int register )
	{
		if ( statement.is( "const" ) )
		{
			int value = statement.get( "value", Integer.class );

			int valueReg = ensureRegister( register, 4 );
			loadValue( valueReg, 4, value );

			return valueReg;
		}
		else if ( statement.is( "name" ) )
		{
			String rightName = statement.get( "value", String.class );
			VarAnchor rightAnchor = loadVariable( rightName, register );

			return rightAnchor.register;
		}
		else if ( statement.is( "add" ) )
		{
			Statement left = statement.get( "left" );
			Statement right = statement.get( "right" );

			int leftRegister = load( left, register );
			int rightRegister = load( right );

			addRegister( leftRegister, rightRegister, 4 );
			scope.freeRegister( rightRegister, 4 );
			return leftRegister;
		}
		else if ( statement.is( "sub" ) )
		{
			Statement left = statement.get( "left" );
			Statement right = statement.get( "right" );

			int leftRegister = load( left, register );
			int rightRegister = load( right );

			subRegister( leftRegister, rightRegister, 4 );
			scope.freeRegister( rightRegister, 4 );
			return leftRegister;
		}

		return -1;
	}

	public int compileAdd( Statement add )
	{
		return load( add );
	}

	public void compileAssign( Statement assign )
	{
		Statement target = assign.get( "left" );
		Statement source = assign.get( "right" );

		if ( target.is( "declare" ) )
		{
			compileDeclare( target );
			target = target.get( "right" );
		}

		int reg = load( source );

		storeVariable( reg, target.get( "value", String.class ) );
		scope.freeRegister( reg, 4 );
	}

	public void compileDeclare( Statement declare )
	{
		Statement type = declare.get( "left" );
		Statement name = declare.get( "right" );
		String nameStr = name.get( "value", String.class );
		// String typeStr = type.get( "value", String.class );

		scope.registerVariable( nameStr, 4 );

	}

	public void compileBlock( Statement statement )
	{
		Statement head = statement.get( "left" );
		Statement[] commands = statement.get( "right", Statement[].class );

		Statement[] modifier = head.get( "left", Statement[].class );
		Statement signature = head.get( "right" );

		if ( signature.is( "chain" ) )
		{
			Statement methodName = signature.get( "left" );
			Statement params = signature.get( "right" );

			asm.addCommand( StringCommand.from( ".global {0}", methodName ) );
			asm.addCommand( StringCommand.from( "\t.type\t{0}, @function", methodName ) );
			asm.addCommand( StringCommand.from( "{0}:", methodName ) );

			asm.push( 29 );
			asm.push( 28 );

			scope = new Scope( scope );
			CompilerAsmBuilder temp = asm;
			asm = new CompilerAsmBuilder();
			compileCommands( commands );

			temp.sbiw( 28, scope.getVarOffset() );

			temp.addCommands( asm );
			asm = temp;

			asm.pop( 29 );
			asm.pop( 28 );
			asm.ret();
		}
		else if ( signature.is( "name" ) )
		{
			for ( Statement command : commands )
			{
				compile( command );
			}
		}

	}

	public void addRegister( int dest, int src, int size )
	{
		for ( int offset = 0 ; offset < size ; offset++ )
		{
			if ( offset == 0 )
			{
				asm.add( dest - offset, src - offset );
			}
			else
			{
				asm.adc( dest - offset, src - offset );
			}
		}
	}

	public void subRegister( int dest, int src, int size )
	{
		for ( int offset = 0 ; offset < size ; offset++ )
		{
			if ( offset == 0 )
			{
				asm.sub( dest - offset, src - offset );
			}
			else
			{
				asm.sbc( dest - offset, src - offset );
			}
		}
	}

	public void mulRegister( int dest, int src, int size )
	{
		throw new RuntimeException( "not yet implemented" );
	}

	public List<AbstractCommand> compileCommands( Statement[] commands )
	{
		List<AbstractCommand> result = new ArrayList<>();

		for ( Statement command : commands )
		{
			compileImpl( command );
		}

		return result;
	}

	private void storeVariable( int register, String name )
	{
		VarAnchor entry = scope.getVarAnchor( name );
		scope.storeRegister( register, name );
		for ( int i = 0 ; i < entry.size ; i++ )
		{
			int byteRegister = register - i;
			int offset = i + 1;

			asm.std( offset + entry.offset, byteRegister );
		}
	}

	public int getSize( String type )
	{
		if ( type.equals( "int" ) )
		{
			return 4;
		}

		return 1;
	}

	public void moveRegister( int destReg, int srcReg, int size )
	{
		for ( int i = 0 ; i < size ; i++ )
		{
			asm.mov( destReg - i, srcReg - i );
		}
	}

	public VarAnchor loadVariable( String name )
	{
		return loadVariable( name, -1 );
	}

	public VarAnchor loadVariable( String name, int register )
	{
		VarAnchor anchor = scope.getVarAnchor( name );

		if ( register < 0 )
		{
			if ( anchor.register >= 0 )
			{
				scope.useRegister( anchor.register, anchor.size );
				return anchor;
			}

			register = scope.allocRegister( anchor.size );
		}

		for ( int i = 0 ; i < anchor.size ; i++ )
		{
			int byteRegister = register - i;
			int offset = i + 1;

			asm.ldd( byteRegister, offset + anchor.offset );
		}

		scope.storeRegister( register, name );
		return anchor;
	}

	public void loadValue( int register, int size, int value )
	{
		for ( int i = 0 ; i < size ; i++ )
		{
			asm.ldi( register - i, value & 255 );

			value >>>= 8;
		}
	}

	private class CompilerAsmBuilder extends AsmBuilder {
		@Override
		public void override( int register, int size )
		{
			scope.override( register, size );
			super.override( register, size );
		}
	}
}

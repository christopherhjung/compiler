package compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.Scope;
import parser.Scope.VarAnchor;
import parser.Statement;

public class Compiler {

	private Scope scope = null;

	public String compile( Statement statement )
	{
		StringBuilder sb = new StringBuilder();
		List<AbstractCommand> result = compileImpl( statement );
		for ( AbstractCommand command : result )
		{
			sb.append( command );
		}

		return sb.toString();
	}

	private List<AbstractCommand> compileImpl( Statement statement )
	{
		if ( statement.is( "block" ) )
		{
			return compileBlock( statement );
		}
		else if ( statement.is( "assign" ) )
		{
			return compileAssign( statement );
		}
		else if ( statement.is( "add" ) )
		{
			return compileAdd( statement );
		}

		return null;
	}

	public List<AbstractCommand> compileAdd( Statement add )
	{
		List<AbstractCommand> commands = new ArrayList<>();
		Statement left = add.get( "left" );
		Statement right = add.get( "right" );

		if ( left.is( "name" ) )
		{
			int pos = scope.allocRegister( 3 );
			String name = left.get( "value", String.class );
			VarAnchor anchor = scope.getVarAnchor( name );

			int register = loadVariable( commands, 4, name );

			int tempRegister = scope.allocRegister( 4 );
			moveRegister( commands, tempRegister, register, 4 );

			if ( right.is( "const" ) )
			{
				int value = right.get( "value", Integer.class );
				for ( int offset = 0 ; offset < 4 ; offset++ )
				{
					Asm command;
					if ( offset == 0 )
					{
						command = Asm.add( register - offset, value );
					}
					else
					{
						command = Asm.adc( register - offset, value );
					}
					commands.add( command );
					value >>>= 8;
				}
			}
			
			
			scope.freeRegister( register, 4 );
		}

		return commands;
	}

	public void moveRegister( List<AbstractCommand> commands, int destReg, int srcReg, int size )
	{
		for ( int i = 0 ; i < size ; i++ )
		{
			commands.add( Asm.mov( destReg + i, srcReg + i ) );
		}
	}

	public int loadVariable( List<AbstractCommand> commands, int size, String name )
	{
		VarAnchor anchor = scope.getVarAnchor( name );

		if ( anchor.register >= 0 )
		{
			scope.useRegister( anchor.register );
			return anchor.register;
		}

		int register = scope.allocRegister( size );
		scope.registerVariable( name, register );

		for ( int i = 0 ; i < size ; i++ )
		{
			int byteRegister = register - i;
			int offset = i + 1;

			commands.add( Asm.ldd( byteRegister, offset + anchor.offset ) );
		}

		return register;
	}

	public void loadValue( List<AbstractCommand> commands, int register, int size, int value )
	{
		for ( int i = 0 ; i < size ; i++ )
		{
			int byteRegister = register - i;
			int offset = i + 1;

			commands.add( Asm.ldi( byteRegister, value & 255 ) );

			value >>>= 8;
		}
	}

	public List<AbstractCommand> compileAssign( Statement assign )
	{
		List<AbstractCommand> commands = new ArrayList<>();
		Statement target = assign.get( "left" );
		Statement source = assign.get( "right" );

		if ( target.is( "declare" ) )
		{
			compileDeclare( target );
			target = target.get( "right" );
		}

		String nameStr = target.get( "value", String.class );
		VarAnchor entry = scope.getVarAnchor( nameStr );

		if ( source.is( "const" ) )
		{
			int value = source.get( "value", Integer.class );

			int register = scope.allocRegister( entry.size );

			scope.storeRegister( register, nameStr );
			for ( int i = 0 ; i < entry.size ; i++ )
			{
				int byteRegister = register - i;
				int offset = i + 1;

				commands.add( Asm.ldi( byteRegister, value & 255 ) );
				commands.add( Asm.std( offset + entry.offset, byteRegister ) );

				value >>= 8;
			}

			scope.freeRegister( register, entry.size );
		}

		return commands;
	}

	public int getSize( String type )
	{
		if ( type.equals( "int" ) )
		{
			return 4;
		}

		return 1;
	}

	public List<AbstractCommand> compileDeclare( Statement declare )
	{
		Statement type = declare.get( "left" );
		Statement name = declare.get( "right" );
		String nameStr = name.get( "value", String.class );
		// String typeStr = type.get( "value", String.class );

		scope.registerVariable( nameStr, 4 );

		return null;
	}

	public List<AbstractCommand> compileBlock( Statement statement )
	{
		List<AbstractCommand> result = new ArrayList<>();
		Statement head = statement.get( "left" );
		Statement[] commands = statement.get( "right", Statement[].class );

		Statement[] modifier = head.get( "left", Statement[].class );
		Statement methodSignature = head.get( "right" );

		Statement methodName = methodSignature.get( "left" );
		Statement params = methodSignature.get( "right" );

		result.add( StringCommand.from( ".global {0}", methodName ) );
		result.add( StringCommand.from( "\t.type\t{0}, @function", methodName ) );
		result.add( StringCommand.from( "{0}:", methodName ) );

		result.add( Asm.push( 29 ) );
		result.add( Asm.push( 28 ) );

		scope = new Scope( scope );
		List<AbstractCommand> list = compileCommands( commands );

		result.add( Asm.sbiw( 28, scope.getVarOffset() ) );

		result.addAll( list );

		result.add( Asm.pop( 29 ) );
		result.add( Asm.pop( 28 ) );

		return result;
	}

	public List<AbstractCommand> compileCommands( Statement[] commands )
	{
		List<AbstractCommand> result = new ArrayList<>();

		for ( Statement command : commands )
		{
			result.addAll( compileImpl( command ) );
		}

		return result;
	}
}

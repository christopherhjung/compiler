package compiler;

import java.util.ArrayList;
import java.util.List;

public class AsmBuilder {
	public static class Asm extends AbstractCommand {
		public String params;
		public String opcode;

		public Asm( String opcode, String params )
		{
			this.opcode = opcode;
			this.params = params;
		}

		@Override
		public String toString()
		{
			return "\t" + this.opcode + " " + this.params + "\n";
		}
	}

	private List<AbstractCommand> commands;

	public AsmBuilder()
	{
		commands = new ArrayList<>();
	}

	public List<AbstractCommand> getCommands()
	{
		return commands;
	}

	public void addCommands( AsmBuilder builder )
	{
		this.commands.addAll( builder.commands );
	}
	
	public void addCommand( AbstractCommand command )
	{
		this.commands.add( command );
	}

	public Asm add( String opcode, String format, Object... args )
	{
		Asm asm = new Asm( opcode, String.format( format, args ) );
		commands.add( asm );
		return asm;
	}

	public Asm ldi( int register, int value )
	{
		override( register, 1 );
		return add( "ldi", "r%d,%d", register, value );
	}

	public Asm std( int offset, int register )
	{
		return add( "std", "Y+%d,r%d", offset, register );
	}

	public Asm ldd( int register, int offset )
	{
		override( register, 1 );
		return add( "ldd", "r%d,Y+%d", register, offset );
	}

	public Asm push( int register )
	{
		return add( "push", "r%d", register );
	}

	public Asm pop( int register )
	{
		return add( "pop", "r%d", register );
	}

	public Asm ret()
	{
		return add( "ret", "" );
	}

	public Asm rcall( String method )
	{
		return add( "rcall", "%s", method );
	}

	public Asm subi( int register, int value )
	{
		override( register, 1 );
		return add( "subi", "r%d,%d", register, value );
	}

	public Asm sbci( int register, int value )
	{
		override( register, 1 );
		return add( "sbci", "r%d,%d", register, value );
	}

	public Asm add( int dest, int src )
	{
		override( dest, 1 );
		return add( "add", "r%d,r%d", dest, src );
	}

	public Asm adc( int dest, int src )
	{
		override( dest, 1 );
		return add( "adc", "r%d,r%d", dest, src );
	}
	
	public Asm sub( int dest, int src )
	{
		override( dest, 1 );
		return add( "sub", "r%d,r%d", dest, src );
	}

	public Asm sbc( int dest, int src )
	{
		override( dest, 1 );
		return add( "sbc", "r%d,r%d", dest, src );
	}

	public Asm mov( int dest, int src )
	{
		override( dest, 1 );
		return add( "mov", "r%d,r%d", dest, src );
	}

	public void override( int register, int size )
	{

	}

	/**
	 * Subtract value from r[value+1],r[value]
	 * 
	 * @param register
	 * @param value
	 * @return
	 */
	public Asm sbiw( int register, int value )
	{
		override( register + 1, 2 );
		return add( "sbiw", "r%d,%d", register, value );
	}
}

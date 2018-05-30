package compiler;

import java.util.ArrayList;
import java.util.List;

public class AsmBuilder {
	public static class Asm extends AbstractCommand {
		public String params;
		public String opcode;
		public int cycles;

		public Asm( String opcode, int cycles, String params )
		{
			this.opcode = opcode;
			this.params = params;
			this.cycles = cycles;
		}

		@Override
		public String toString()
		{
			return "\t" + this.opcode + " " + this.params + "\n";
		}
	}

	private List<AbstractCommand> commands;
	private int cycles = 0;

	public AsmBuilder()
	{
		commands = new ArrayList<>();
		cycles = 0;
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

	public Asm add( String opcode, int cycles, String format, Object... args )
	{
		Asm asm = new Asm( opcode, cycles, String.format( format, args ) );
		this.cycles += cycles;
		commands.add( asm );
		return asm;
	}

	public Asm ldi( int register, int value )
	{
		override( register, 1 );
		return add( "ldi", 1, "r%d,%d", register, value );
	}

	public Asm std( int offset, int register )
	{
		return add( "std", 2, "Y+%d,r%d", offset, register );
	}

	public Asm ldd( int register, int offset )
	{
		override( register, 1 );
		return add( "ldd", 2, "r%d,Y+%d", register, offset );
	}

	public Asm push( int register )
	{
		return add( "push", 2, "r%d", register );
	}

	public Asm pop( int register )
	{
		return add( "pop", 2, "r%d", register );
	}

	public Asm ret()
	{
		return add( "ret", 4, "" );
	}

	public Asm rcall( String method )
	{
		return add( "rcall", 3, "%s", method );
	}

	public Asm subi( int register, int value )
	{
		override( register, 1 );
		return add( "subi", 1, "r%d,%d", register, value );
	}

	public Asm sbci( int register, int value )
	{
		override( register, 1 );
		return add( "sbci", 1, "r%d,%d", register, value );
	}

	public Asm add( int dest, int src )
	{
		override( dest, 1 );
		return add( "add", 1, "r%d,r%d", dest, src );
	}

	public Asm adc( int dest, int src )
	{
		override( dest, 1 );
		return add( "adc", 1, "r%d,r%d", dest, src );
	}

	public Asm sub( int dest, int src )
	{
		override( dest, 1 );
		return add( "sub", 1, "r%d,r%d", dest, src );
	}

	public Asm sbc( int dest, int src )
	{
		override( dest, 1 );
		return add( "sbc", 1, "r%d,r%d", dest, src );
	}

	public Asm mov( int dest, int src )
	{
		override( dest, 1 );
		return add( "mov", 1, "r%d,r%d", dest, src );
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
		return add( "sbiw", 2, "r%d,%d", register, value );
	}
}

package compiler;

public class Asm extends AbstractCommand {
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

	public static Asm from( String opcode, String format, Object... args )
	{
		return new Asm( opcode, String.format( format, args ) );
	}

	public static Asm ldi( int register, int value )
	{
		override( register, register );
		return from( "ldi", "r%d,%d", register, value );
	}

	public static Asm std( int offset, int register )
	{
		return from( "std", "Y+%d,r%d", offset, register );
	}

	public static Asm ldd( int register, int offset )
	{
		override( register, register );
		return from( "ldd", "r%d,Y+%d", register, offset );
	}

	public static Asm push( int register )
	{
		return from( "push", "r%d", register );
	}

	public static Asm pop( int register )
	{
		return from( "pop", "r%d", register );
	}

	public static Asm rcall( String method )
	{
		return from( "rcall", "%s", method );
	}

	public static Asm subi( int register, int value )
	{
		override( register, register );
		return from( "subi", "r%d,%d", register, value );
	}

	public static Asm sbci( int register, int value )
	{
		override( register, register );
		return from( "sbci", "r%d,%d", register, value );
	}

	public static Asm add( int register, int value )
	{
		override( register, register );
		return from( "add", "r%d,%d", register, value );
	}

	public static Asm adc( int register, int value )
	{
		override( register, register );
		return from( "adc", "r%d,%d", register, value );
	}

	public static Asm mov( int dest, int src )
	{
		override( dest, dest );
		return from( "mov", "r%d,r%d", dest, src );
	}

	public static Asm movw( int dest, int src )
	{
		override( dest + 1, dest );
		return from( "movw", "r%d:r%d,r%d:r%d", dest + 1, dest, src + 1, src );
	}

	public static void override( int highRegister, int lowRegister )
	{

	}

	/**
	 * Subtract value from r[value+1],r[value]
	 * 
	 * @param register
	 * @param value
	 * @return
	 */
	public static Asm sbiw( int register, int value )
	{
		override( register + 1, register );
		return from( "sbiw", "r%d,%d", register, value );
	}
}

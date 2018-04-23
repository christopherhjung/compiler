package therms;

import parser.ThermStringifier;

public class Const extends Therm {

	public static final Const MINUS_TWO = new Const( -2 );
	public static final Const MINUS_ONE = new Const( -1 );
	public static final Const ZERO = new Const( 0 );
	public static final Const ONE = new Const( 1 );
	public static final Const TWO = new Const( 2 );

	private final double value;

	public Const( double value )
	{
		this.value = value;
	}

	public double getValue()
	{
		return value;
	}

	@Override

	public Therm derivate( Variable name )
	{
		return Const.ZERO;
	}
	
	@Override
	public Therm reduce( VarSet varSet, Therm... therms )
	{
		return this;
	}
	
	@Override
	public Therm add( Therm therm )
	{
		if ( therm instanceof Const )
		{
			Const other = (Const) therm;
			return new Const( value + other.value );
		}
		
		return super.add( therm );
	}

	@Override
	public Therm mul( Therm therm )
	{
		if ( therm instanceof Const )
		{
			Const other = (Const) therm;
			return new Const( value * other.value );
		}
		
		return super.mul( therm );
	}


	@Override
	public boolean equals( Object obj )
	{
		return super.equals( obj ) || obj instanceof Const && value == ((Const) obj).value;
	}
	
	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( value );
	}
	
	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}
}

package therms;

import java.util.Arrays;

import parser.ThermStringifier;
import tools.ReflectionUtils;

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

	@Override
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "value" ) )
		{
			return value;
		}
		else if ( key.equals( "type" ) )
		{
			return "const";
		}
		else if ( key.equals( "derivate" ) )
		{
			return new Const( 0 );
		}
		else if ( key.equals( "add" ) && params.length == 1 )
		{
			Therm therm = ReflectionUtils.as( params[0], Therm.class );
			if ( therm != null && therm.execute( "type" ).equals( "const" ) )
			{
				double otherValue = therm.get( "value", Double.class );
				return new Const( value + otherValue );
			}
		}
		else if ( key.equals( "reduce" ) )
		{
			return this;
		}

		return super.execute( key );
	}

	public double getValue()
	{
		return value;
	}

	@Override
	public Therm reduce( VarSet varSet, Therm... therms )
	{
		return this;
	}

	@Override
	public Therm add( Therm therm )
	{
		if ( therm.execute( "type" ).equals( "const" ) )
		{
			double otherValue = therm.get( "value", Double.class );
			return new Const( value + otherValue );
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

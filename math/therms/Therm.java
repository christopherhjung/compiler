package therms;

import java.util.ArrayList;
import java.util.List;

import parser.ThermStringifier;

public abstract class Therm {
	public final static int ZERO_LEVEL = 0;
	public final static int EQUATION_LEVEL = 1;
	public final static int ADDITION_LEVEL = 2;
	public final static int MULTIPLY_LEVEL = 3;
	public final static int EXPONENT_LEVEL = 4;
	public final static int FUNCTION_LEVEL = 5;

	public <T> T get( String key, Class<T> type )
	{
		Object result = execute( key );

		if ( type.isAssignableFrom( result.getClass() ) )
		{
			return type.cast( result );
		}

		return null;
	}

	public Object execute( String key, Object... params )
	{
		return null;
	}

	public abstract Therm derivate( Variable name );

	public abstract void toString( ThermStringifier builder );

	public Therm integrate( Variable name, Const constant )
	{
		return null;
	}

	public Therm reduce( VarSet varSet, Therm... therms )
	{
		return this;
	}

	public Therm add( Therm therm )
	{
		if ( this.equals( Const.ZERO ) ) return therm;
		if ( therm.equals( Const.ZERO ) ) return this;

		if ( this instanceof Additional )
		{
			List<Therm> result = new ArrayList<>( ((Additional) this).getTherms() );
			result.add( therm );
			return new Additional( result );
		}
		else if ( therm instanceof Additional )
		{
			List<Therm> result = new ArrayList<>( ((Additional) therm).getTherms() );
			result.add( 0, this );
			return new Additional( result );
		}

		return new Additional( this, therm );
	}

	public Therm mul( Therm therm )
	{
		if ( equals( Const.ONE ) || therm.equals( Const.ZERO ) ) return therm;
		if ( therm.equals( Const.ONE ) || equals( Const.ZERO ) ) return this;

		if ( this instanceof Multiply )
		{
			List<Therm> result = new ArrayList<>( ((Multiply) this).getTherms() );
			result.add( therm );
			return new Multiply( result );
		}
		else if ( therm instanceof Multiply )
		{
			List<Therm> result = new ArrayList<>( ((Multiply) therm).getTherms() );
			result.add( 0, this );
			return new Multiply( result );
		}

		return new Multiply( this, therm );
	}

	public Therm pow( Therm therm )
	{
		// if ( therm.equals( Const.ZERO ) ) return Const.ONE;
		// if ( therm.equals( Const.ONE ) ) return this;

		return new Exponenional( this, therm );
	}

	public Therm div( Therm therm )
	{
		return mul( therm.pow( Const.MINUS_ONE ) );
	}

	public Therm sub( Therm therm )
	{
		return add( Const.MINUS_ONE.mul( therm ) );
	}

	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}

	public String toString()
	{
		return new ThermStringifier().append( this ).toString();
	}
}

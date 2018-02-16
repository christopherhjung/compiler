package therms;

import java.util.ArrayList;
import java.util.List;

import parser.ThermStringify;

public abstract class Therm {
	public final static int ZERO_LEVEL = 0;
	public final static int EQUATION_LEVEL = 1;
	public final static int ADDITION_LEVEL = 2;
	public final static int MULTIPLY_LEVEL = 3;
	public final static int EXPONENT_LEVEL = 4;
	public final static int FUNCTION_LEVEL = 5;

	public abstract Therm derivate( Variable name );

	public abstract void toString( ThermStringify builder );

	public Therm integrate( Variable name, Const constant )
	{
		return null;
	}

	public Therm replace( Therm replacer, Therm replacement )
	{
		if ( equals( replacer ) ) return replacement;
		return this;
	}

	public abstract double valueAt( VarSet varSet );

	public boolean contains( Therm var )
	{
		return false;
	}

	public Therm simplify()
	{
		return this;
	}

	public Therm contractMultiply( Therm therm )
	{
		if ( this.equals( therm ) ) return pow( Const.TWO );

		if ( false && therm instanceof Additional )
		{
			Additional other = (Additional) therm;
			List<Therm> newTherms = new ArrayList<>();
			for ( Therm element : other.getTherms() )
			{
				newTherms.add( mul( element ) );
			}

			return new Additional( newTherms );
		}

		if ( therm instanceof Exponenional )
		{
			Exponenional other = (Exponenional) therm;
			if ( other.getBasis().equals( this ) )
			{
				return pow( Const.ONE.add( other.getExponent() ) );
			}
		}

		return null;
	}

	public Therm contractAdditional( Therm therm )
	{
		if ( this.equals( therm ) ) return Const.TWO.mul( this );

		return null;
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
		if ( therm.equals( Const.ZERO ) ) return Const.ONE;
		if ( therm.equals( Const.ONE ) ) return this;

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
		return new ThermStringify().append( this ).toString();
	}
}

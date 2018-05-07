package crap;

import functions.AddPlugin.Additional;
import functions.ConstPlugin.Const;
import functions.MulPlugin.Multiply;
import therms.Therm;

public class Polynominal extends Therm {

	public static Therm fromArray( double[] therms )
	{
		Additional additional = new Additional();

		for ( int i = therms.length - 1 ; i >= 0 ; i-- )
		{
			if ( therms[i] != 0 )
			{
				if ( i == 0 )
				{
					additional.add( new Const( therms[i] ) );
				}
				else
				{
					additional.add( new Multiply( new Const( therms[i] ), new Polynominal( i ) ) );
				}
			}
		}

		return additional;
	}

	private double exponent;

	/*
	 * private Therm therm; public Polinominal( Therm therm, double exponent ) {
	 * this.therm = therm; this.exponent = exponent; }
	 */

	public Polynominal( double exponent )
	{
		this.exponent = exponent;
	}

	public double getExponent()
	{
		return exponent;
	}

	@Override
	public Therm derivate( String name )
	{
		if ( exponent == 0 )
		{
			return new Const( 0 );
		}
		else
		{
			Multiply chain = new Multiply();

			chain.mul( new Const( exponent ) );
			if ( exponent != 1 )
			{
				chain.mul( new Polynominal( exponent - 1 ) );
			}

			return chain;
		}
	}

	@Override
	public Therm integrate( String name, double constant )
	{
		return new Multiply( new Const( 1 / (exponent + 1) ) , new Polynominal( exponent + 1 ) );
	}
	
	@Override
	public double valueAt( double x )
	{
		return Math.pow( x, exponent );
	}

	@Override
	public Therm simplify()
	{
		if ( exponent == 0 ) return new Const( 1 );
		return this;
	}

	@Override
	public Therm contractAdditional( Therm therm )
	{
		if ( therm instanceof Polynominal )
		{
			Polynominal other = (Polynominal) therm;

			if ( this.exponent == other.exponent )
			{
				return new Multiply( new Const( 2 ), this );
			}
		}

		return null;
		// return new Additional( this, therm );
	}

	@Override
	public Therm contractMultiply( Therm therm )
	{
		if ( therm instanceof Polynominal )
		{
			Polynominal other = (Polynominal) therm;

			double newExponent = exponent + other.exponent;

			if ( newExponent == 0 )
			{
				return new Const( 1 );
			}
			else
			{
				return new Polynominal( newExponent );
			}
		}

		return null;
		// return new Multiply( this, therm );
	}

	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" ) && exponent != 0;
	}

	@Override
	public boolean equals( Object obj )
	{
		return super.equals( obj ) || obj instanceof Polynominal && exponent == ((Polynominal) obj).exponent;
	}
	
	@Override
	public String toString()
	{
		if ( exponent == 0 ) return "1";
		if ( exponent == 1 ) return "x";

		return "x ^ " + exponent;
	}
}

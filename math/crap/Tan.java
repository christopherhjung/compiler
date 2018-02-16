package crap;

import therms.Chain;
import therms.Therm;

public class Tan extends Therm {

	@Override
	public Therm derivate( String name )
	{
		return new Chain( new Polynominal( -2 ), new Cos() );
	}

	@Override
	public double valueAt( double x )
	{
		return Math.tan( x );
	}
	
	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" );
	}
	
	@Override
	public String toString()
	{
		return "tan( x )";
	}
}

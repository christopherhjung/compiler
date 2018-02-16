package crap;

import therms.Therm;

public class Sin extends Therm{

	@Override
	public Therm derivate( String name )
	{
		return new Cos();
	}

	@Override
	public double valueAt( double x )
	{
		return Math.sin( x );
	}
	
	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" );
	}

	@Override
	public String toString()
	{
		return "sin( x )";
	}
}

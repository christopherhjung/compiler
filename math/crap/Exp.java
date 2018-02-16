package crap;

import therms.Therm;

public class Exp extends Therm{

	@Override
	public Therm derivate( String name )
	{
		return this;
	}

	@Override
	public double valueAt( double x )
	{
		return Math.exp( x );
	}
	
	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" );
	}

	@Override
	public String toString()
	{
		return "exp( x )";
	}
}

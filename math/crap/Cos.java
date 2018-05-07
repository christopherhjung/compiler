package crap;

import functions.ConstPlugin.Const;
import therms.Therm;

public class Cos extends Therm {

	@Override
	public Therm derivate( String name )
	{
		return Const.MINUS_ONE.mul( new Sin() );
	}

	@Override
	public double valueAt( double x )
	{
		return Math.cos( x );
	}

	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" );
	}

	@Override
	public String toString()
	{
		return "cos( x )";
	}
}

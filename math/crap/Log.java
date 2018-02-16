package crap;

import therms.Const;
import therms.Exponenional;
import therms.Therm;
import therms.Variable;

public class Log extends Therm{

	@Override
	public Therm derivate( String name )
	{
		return new Exponenional( new Variable("x") , Const.MINUS_ONE );
	}

	@Override
	public double valueAt( double x )
	{
		return Math.log( x );
	}
	
	@Override
	public boolean contains( String var )
	{
		return var.equals( "x" );
	}

	@Override
	public String toString()
	{
		return "log( x )";
	}
}

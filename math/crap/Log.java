package crap;

import functions.ConstPlugin.Const;
import functions.ExponentPlugin.Exponenional;
import functions.VariablePlugin.Variable;
import therms.Therm;

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

package functions;

import parser.MathEngine;
import parser.ThermStringify;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Sin extends EnginePlugin {

	private Therm derivate = null;
	
	@Override
	public void enable( MathEngine engine )
	{
		derivate = engine.eval( "cos(x)" );
	}
	
	@Override
	public Therm derivate( Variable name )
	{
		return derivate;
	}

	@EngineExecute
	public static Const execute( Const a )
	{
		return new Const( Math.sin( a.getValue() ) );
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		return Math.sin( varSet.getValue( Variable.X ) );
	}

	@Override
	public boolean contains( Therm var )
	{
		return var.equals( Variable.X );
	}

	@Override
	public void toString( ThermStringify builder )
	{
		builder.append( "sin( x )" );
	}
}
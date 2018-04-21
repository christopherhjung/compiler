package functions;

import parser.MathParser;
import parser.ThermStringify;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Tan extends EnginePlugin {


	private Therm derivate = null;
	
	@Override
	public void enable( MathParser engine )
	{
		derivate = engine.eval( "cos(x) ^ -2" );
	}
	
	@Override
	public Therm derivate( Variable name )
	{
		return derivate;
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		return Math.tan( varSet.getValue( Variable.X ) );
	}

	@Override
	public boolean contains( Therm var )
	{
		return var.equals( Variable.X );
	}

	@Override
	public void toString( ThermStringify builder )
	{
		builder.append( "tan( x )" );
	}
}
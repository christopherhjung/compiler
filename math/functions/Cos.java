package functions;

import parser.MathParser;
import parser.ThermStringify;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Cos extends EnginePlugin {

	private Therm derivate = null;

	@Override
	public void enable( MathParser engine )
	{
		derivate = engine.eval( "-sin(x)" );
	}

	@Override
	public Therm derivate( Variable name )
	{
		return derivate;
	}

	@EngineExecute
	public static Const execute( Const a )
	{
		return new Const( Math.cos( a.getValue() ) );
	}

	@EngineExecute
	public Therm execute( Therm var )
	{
		return new Chain(this, var);
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
		builder.append( "cos( x )" );
	}
}
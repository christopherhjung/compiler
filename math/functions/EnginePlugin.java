package functions;

import parser.MathParser;
import therms.Chain;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public abstract class EnginePlugin {

	public void onEnable()
	{

	}

	public void onAttach( MathParser parser )
	{

	}

	public Therm handle( MathParser parser )
	{
		return null;
	}
}

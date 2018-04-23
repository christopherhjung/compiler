package functions;

import parser.MathParser;
import therms.Chain;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public abstract class EnginePlugin{

	public void enable( MathParser engine )
	{

	}	
	
	public Therm handle( MathParser engine ){
		return null;
	}
}

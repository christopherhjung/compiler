package functions;

import parser.MathParser;
import parser.MathProgram;
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
	
	public void onStart( MathProgram program )
	{
		
	}
	
	public Therm handle( MathParser parser )
	{
		return null;
	}

	public Therm handle( MathParser parser, Therm left )
	{
		if ( left != null )
		{
			return null;
		}
		return handle( parser );
	}
}

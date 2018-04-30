package functions;

import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import therms.Chain;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.Run;

public abstract class EnginePlugin {

	private MathEngine engine;

	public MathEngine getEngine()
	{
		return engine;
	}

	public Therm eval( String str )
	{
		return getEngine().eval( str );
	}

	public Therm eval( Object... objs )
	{
		return getEngine().eval( objs );
	}
	
	public void onStart( MathEngine engine )
	{
		this.engine = engine;
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

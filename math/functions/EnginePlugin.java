package functions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import parser.MathEngine;
import parser.MathParser;
import therms.Therm;

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
	
	public Therm eval( List<Object> list )
	{
		return getEngine().eval( list );
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

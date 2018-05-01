package parser;

import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
import therms.Therm;

public class MathEngine { 
	private Map<Integer, Set<EnginePlugin>> plugins;

	public MathEngine( Map<Integer, Set<EnginePlugin>> plugins )
	{
		this.plugins = plugins;
	}

	public Therm eval( String str )
	{

		return new MathParser( plugins ).eval( str );
	}
	
	public Therm eval( Object... obj )
	{

		return new HybridMathParser( plugins ).eval( obj );
	}
}

package parser;

import java.util.Iterator;
import java.util.List;
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
	
	public Therm eval( List<Object> list )
	{

		return new HybridMathParser( plugins ).eval( list.toArray() );
	}
}

package parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
import functions.VariablePlugin.Variable;
import parser.Space.Scope;
import therms.Therm;

public class MathEngine { 
	private Map<Integer, Set<EnginePlugin>> plugins;
	
	public Map<Therm, Therm> variables = new HashMap<>();
	
	public Scope globalScope = new Scope();

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

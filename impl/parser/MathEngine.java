package parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import parser.Space.Scope;

public class MathEngine {
	private Map<Integer, Set<EnginePlugin>> plugins;
	private Map<EnginePlugin, Integer> levels;

	public MathEngine( Map<Integer, Set<EnginePlugin>> plugins, Map<EnginePlugin, Integer> levels )
	{
		this.plugins = plugins;
		this.levels = levels;
	}

	public int getLevel( EnginePlugin plugin )
	{
		Integer result = levels.get( plugin );

		if ( result == null )
		{
			return Integer.MIN_VALUE;
		}

		return result;
	}

	public Statement eval( Object... obj )
	{
		return new HybridMathParser( plugins ).eval( obj );
	}

	public Statement eval( List<Object> list )
	{
		return eval( list.toArray() );
	}
}

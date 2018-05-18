package parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

import parser.Space.Scope;
import therms.Therm;

public class MathEngine {
	private Map<Integer, Set<EnginePlugin>> plugins;
	private Map<EnginePlugin, Integer> levels;

	public Scope globalScope = new Scope();
	public Scope currentScope = globalScope;

	public void enterScope( Scope scope )
	{
		scope.setParentScope( currentScope );
		currentScope = scope;
	}

	public void leaveScope()
	{
		currentScope = currentScope.getParentScope();
	}

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

	public Therm eval( String obj )
	{
		return new MathParser( plugins ).eval( obj );
	}

	public Therm eval( Object... obj )
	{
		return new HybridMathParser( plugins ).eval( obj );
	}

	public Therm eval( List<Object> list )
	{

		return eval( list.toArray() );
	}
}

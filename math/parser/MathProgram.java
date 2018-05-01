package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;

public class MathProgram {

	private Map<Integer, Set<Class<? extends EnginePlugin>>> plugins = new HashMap<>();

	public void installPlugin( Class<? extends EnginePlugin> plugin )
	{
		installPlugin( Integer.MAX_VALUE, plugin );
	}

	public void installPlugin( int level, Class<? extends EnginePlugin> plugin )
	{
		Set<Class<? extends EnginePlugin>> set = plugins.computeIfAbsent( level, i -> new HashSet<>() );
		set.add( plugin );
	}

	public MathEngine start() throws Exception
	{
		Map<Integer, Set<EnginePlugin>> plugins = new HashMap<>();
		
		for ( int level : this.plugins.keySet() )
		{
			Set<Class<? extends EnginePlugin>> set = this.plugins.get( level );
			Set<EnginePlugin> target = new HashSet<>();
			plugins.put( level, target );

			for ( Class<? extends EnginePlugin> pluginClass : set )
			{
				target.add( pluginClass.newInstance() );
			}
		}

		MathEngine engine = new MathEngine( plugins );
		
		for ( int level : plugins.keySet() )
		{
			plugins.get( level ).forEach( plugin -> plugin.onStart( engine ) );
		}
		
		return engine;
	}
}

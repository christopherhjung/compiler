package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import functions.EnginePlugin;

public class MathProgram {

	private Set<Class<? extends EnginePlugin>> plugins = new HashSet<>();

	public void installPlugin( Class<? extends EnginePlugin> plugin )
	{
		plugins.add( plugin );
	}

	public MathParser start() throws Exception
	{
		Set<EnginePlugin> plugins = new HashSet<EnginePlugin>();

		for ( Class<? extends EnginePlugin> plugin : this.plugins )
		{
			plugins.add( plugin.newInstance());
		}

		MathParser engine = new MathParser( plugins );

		for ( EnginePlugin plugin : plugins )
		{
			plugin.onAttach( engine );
		}
		
		return engine;
	}
}

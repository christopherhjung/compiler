package parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import functions.EnginePlugin;

public class MathProgram {

	private Map<String, Class<? extends EnginePlugin>> plugins = new HashMap<>();

	public void installPlugin( String str, Class<? extends EnginePlugin> plugin )
	{
		plugins.put( str.toLowerCase(), plugin );
	}

	public MathEngine start() throws Exception
	{
		Map<String, EnginePlugin> plugins = new HashMap<String, EnginePlugin>();

		for ( Entry<String, Class<? extends EnginePlugin>> plugin : this.plugins.entrySet() )
		{
			plugins.put( plugin.getKey(), plugin.getValue().newInstance() );
		}

		MathEngine engine = new MathEngine( plugins );

		for ( EnginePlugin plugin : plugins.values() )
		{
			plugin.enable( engine );
		}

		return engine;
	}
}

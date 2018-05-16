package parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

import tools.Run;

import java.util.Map.Entry;
import java.util.Queue;

public class MathProgram {
	private Queue<Source> plugins = new ConcurrentLinkedQueue<>();

	public void installPlugin( Class<? extends EnginePlugin> plugin )
	{
		installPlugin( Integer.MAX_VALUE, plugin );
	}

	public void installPlugin( int level, Class<? extends EnginePlugin> plugin )
	{
		installPlugin( level, () -> Run.safe( () -> plugin.newInstance() ) );
	}

	public void installPlugin( Supplier<EnginePlugin> plugin )
	{
		installPlugin( Integer.MAX_VALUE, plugin );
	}

	public void installPlugin( int level, Supplier<EnginePlugin> plugin )
	{
		plugins.add( new Source( plugin, level ) );
	}

	public MathEngine start() throws Exception
	{
		Map<Integer, Set<EnginePlugin>> plugins = new HashMap<>();
		Map<String, EnginePlugin> parents = new HashMap<>();
		Map<String, Set<EnginePlugin>> children = new HashMap<>();
		Map<EnginePlugin, Integer> levels = new HashMap<>();

		for ( Source entry : this.plugins )
		{
			int level = entry.getLevel();
			EnginePlugin plugin = entry.createPlugin();
			String parent = plugin.getParentName();
			String name = plugin.getName();

			if ( children.containsKey( name ) )
			{
				for ( EnginePlugin child : children.get( name ) )
				{
					plugin.installExtention( child );
				}

				children.remove( name );
			}

			if ( parent == null )
			{
				levels.put( plugin, level );
				Set<EnginePlugin> target = plugins.computeIfAbsent( level, $ -> new HashSet<>() );
				target.add( plugin );
				parents.put( plugin.getName(), plugin );
			}
			else
			{
				if ( parents.containsKey( parent ) )
				{
					EnginePlugin parentPlugin = parents.get( parent );
					parentPlugin.installExtention( plugin );
				}
				else
				{
					children.computeIfAbsent( parent, $ -> new HashSet<>() ).add( plugin );
				}
			}

			plugin.onCreate( this );
		}

		if ( children.size() > 0 )
		{
			throw new RuntimeException( "Missing Plugins: " + children.values() );
		}

		MathEngine engine = new MathEngine( plugins, levels );

		plugins.keySet().forEach( level -> plugins.get( level ).forEach( plugin -> plugin.onStart( engine ) ) );

		return engine;
	}

	public static class Source {
		Supplier<EnginePlugin> supplier;
		int level;

		protected Source( Supplier<EnginePlugin> supplier, int level )
		{
			this.supplier = supplier;
			this.level = level;
		}

		public int getLevel()
		{
			return level;
		}

		public EnginePlugin createPlugin()
		{
			return supplier.get();
		}
	}
}

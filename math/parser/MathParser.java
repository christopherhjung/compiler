package parser;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import functions.EnginePlugin;
import therms.Therm;

public class MathParser extends StringParser<Therm> {
	private TreeMap<Integer, Set<EnginePlugin>> plugins;
	private Integer level;

	public MathParser( Map<Integer, Set<EnginePlugin>> plugins )
	{
		this.plugins = new TreeMap<>( plugins );
	}

	@Override
	protected void reset()
	{
		resetLevel();
		for ( Set<EnginePlugin> pluginSet : plugins.values() )
		{
			for ( EnginePlugin plugin : pluginSet )
			{
				plugin.reset();
			}
		}
	}

	protected void resetLevel()
	{
		level = plugins.firstKey();
	}

	public Therm parseWithLevelReset()
	{
		Integer currentLevel = level;
		resetLevel();
		Therm result = parse();
		level = currentLevel;
		return result;
	}

	private int c = 0;

	@Override
	public Therm parse()
	{
		if ( level == null )
		{
			return null;
		}

		Set<EnginePlugin> plugins = this.plugins.get( level );
		Integer currentLevel = level;
		Integer nextLevel = this.plugins.higherKey( level );

		level = nextLevel;

		Therm result = parse();

		while ( hasCurrent() )
		{
			Therm parsed = parseLevel( plugins, result );

			if ( parsed != null )
			{
				result = parsed;
			}
			else
			{
				break;
			}
		}

		level = currentLevel;

		return result;
	}

	protected Therm parseLevel( Set<EnginePlugin> plugins, Therm left )
	{
		for ( EnginePlugin plugin : plugins )
		{
			RestoreAction savedState = getRestorePoint();

			Therm result = plugin.handle( this, left );

			if ( result != null )
			{
				return result;
			}
			else
			{
				savedState.restore();
			}
		}

		return null;
	}
}

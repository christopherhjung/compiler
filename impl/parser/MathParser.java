package parser;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MathParser extends StringParser<Statement> {
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

	public Statement parseWithLevelReset()
	{
		Integer currentLevel = level;
		resetLevel();
		Statement result = parse();
		level = currentLevel;
		return result;
	}

	@Override
	public Statement parse()
	{
		if ( level == null )
		{
			return null;
		}

		Set<EnginePlugin> plugins = this.plugins.get( level );
		Integer currentLevel = level;
		Integer nextLevel = this.plugins.higherKey( level );

		level = nextLevel;

		Statement result = parse();

		while ( hasCurrent() )
		{
			Statement parsed = parseLevel( plugins, result );

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

	protected Statement parseLevel( Set<EnginePlugin> plugins, Statement left )
	{
		for ( EnginePlugin plugin : plugins )
		{
			RestoreAction savedState = getRestorePoint();

			Statement result = plugin.handle( this, left );

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

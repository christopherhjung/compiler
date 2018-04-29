package parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.events.EndDocument;

import java.util.Stack;
import java.util.TreeMap;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import functions.BasicPlugin;
import functions.EngineExecute;
import functions.EnginePlugin;

import matrix.Matrix;
import matrix.Vector;
import therms.Chain;
import therms.Const;
import therms.Equation;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.ReflectionUtils;
import tools.Run;

public class MathParser extends StringParser<Therm> {
	private TreeMap<Integer, Set<EnginePlugin>> plugins;
	private Integer level;
	private HashMap<CacheKey, Therm> cache = new HashMap<>();	
	
	private class CacheKey {
		private int position;
		private int level;

		public CacheKey( int position, int level )
		{
			this.position = position;
			this.level = level;
		}

		@Override
		public int hashCode()
		{
			return position | (level << 16);
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( !(obj instanceof CacheKey) ) return false;
			CacheKey key = (CacheKey) obj;

			return position == key.position && level == key.level;
		}

		@Override
		public String toString()
		{
			return String.format( "(%d , %d)", position, level );
		}
	}

	public MathParser( Map<Integer, Set<EnginePlugin>> plugins )
	{
		this.plugins = new TreeMap<>( plugins );
	}

	@Override
	protected void reset( char[] chars )
	{
		super.reset( chars );
		cache.clear();
		resetLevel();
	}

	private void resetLevel()
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

		while ( hasNext() )
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

	private Therm parseLevel( Set<EnginePlugin> plugins, Therm left )
	{
		for ( EnginePlugin plugin : plugins )
		{
			int savePosition = getPosition();
			//System.out.println( this + " ----> " + plugin );
			Therm result = plugin.handle( this, left );
			//System.out.println( result + " <---- " + plugin );

			if ( result != null )
			{
				return result;
			}
			else
			{
				setPosition( savePosition );
			}
		}

		return null;
	}
}

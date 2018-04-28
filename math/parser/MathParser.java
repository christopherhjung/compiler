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
import therms.Variable;
import tools.ReflectionUtils;
import tools.Run;

public class MathParser extends StringParser<Therm> {
	private Set<EnginePlugin> plugins = new HashSet<>();

	public MathParser( Set<EnginePlugin> plugins )
	{
		this.plugins = plugins;
	}

	@Override
	protected void reset( char[] chars )
	{
		super.reset( chars );
		map.clear();
		callStack.clear();
	}

	class StackElement {
		int position;
		EnginePlugin plugin;

		public StackElement( int position, EnginePlugin plugin )
		{
			this.position = position;
			this.plugin = plugin;
		}
	}

	HashMap<Integer, Stack<EnginePlugin>> map = new HashMap<>();
	Stack<EnginePlugin> callStack = new Stack<>();

	@Override
	public Therm parse()
	{
		for ( EnginePlugin plugin : plugins )
		{
			int savePosition = getPosition();

			if ( map.containsKey( savePosition ) && map.get( savePosition ).contains( plugin ) )
			{
				continue;
			}

			System.out.println( this + " ----> " + plugin );

			map.computeIfAbsent( savePosition, i -> new Stack<>() ).push( plugin );
			Therm result = plugin.handle( this );
			map.get( savePosition ).remove( callStack.pop() );

			System.out.println( result + " <---- " + plugin );

			if ( result != null )
			{
				return result;
			}
			else
			{
				setPosition( savePosition );
			}
		}

		throw new ParseException( this );
	}

	public Therm parseWithIgnore()
	{
		EnginePlugin plugin = callStack.peek();
		
		int savePosition = getPosition();
		map.computeIfAbsent( savePosition, i -> new HashSet<>() ).add( plugin );

		Therm therm = parse();

		map.get( savePosition ).remove( plugin );
		
		return therm;
	}	
}

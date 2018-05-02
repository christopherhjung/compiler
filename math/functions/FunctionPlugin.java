package functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import parser.MathEngine;
import parser.MathParser;
import parser.ParseException;
import parser.ThermStringifier;
import therms.Therm;
import tools.ReflectionUtils;

public class FunctionPlugin extends EnginePlugin {

	private HashMap<String, EnginePlugin> plugins = new HashMap<>();

	{
		plugins.put( "sin", new SinPlugin() );
		plugins.put( "reduce", new ReducePlugin() );
		plugins.put( "derivate", new DerivatePlugin() );
		plugins.put( "log", new LogPlugin() );
	}

	@Override
	public void onStart( MathEngine engine )
	{
		plugins.values().forEach( plugin -> plugin.onStart( engine ) );
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm therm = null;

		if ( left == null )
		{
			return null;
		}

		if ( left.is( "variable" ) )
		{
			List<Therm> therms = new ArrayList<>();

			if ( parser.eat( '(' ) )
			{

				for ( ; parser.isNot( ')' ) ; )
				{
					Therm param = parser.parseWithLevelReset();
					therms.add( param );
					parser.eat( ',' );
				}

				parser.eat( ')' );
			}

			Class<?>[] classes = new Class<?>[therms.size()];
			Object[] thermsArr = new Therm[therms.size()];

			for ( int i = 0 ; i < therms.size() ; i++ )
			{
				thermsArr[i] = therms.get( i );
				classes[i] = thermsArr[i].getClass();
			}

			String methodName = left.get( "value", String.class ).toLowerCase();

			EnginePlugin function = plugins.get( methodName );

			if ( function != null )
			{
				List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith( function.getClass(), EngineExecute.class );
				Method method = ReflectionUtils.findBestMethod( methods, classes );
				therm = ReflectionUtils.safeInvoke( null, Therm.class, function, method, thermsArr );
			}
		}

		return therm;
	}

	public class Chain extends Therm {

		private final Therm outer;
		private final Therm[] inner;

		public Chain( Therm outer, Therm... inner )
		{
			this.outer = outer;
			this.inner = inner;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				ArrayList<Object> builder = new ArrayList<>();
				builder.add( inner[0].execute( "derivate", params ) );
				builder.add( "*" );
				builder.add( outer.execute( "derivate" ) );
				builder.add( '(' );
				builder.add( inner[0] );
				builder.add( ')' );

				return eval( builder.toArray( new Object[builder.size()] ) );
			}
			else if ( key.equals( "reduce" ) )
			{
				Object[] reducedParams = new Object[inner.length];
				for ( int i = 0 ; i < inner.length ; i++ )
				{
					reducedParams[i] = inner[i].execute( "reduce" );
				}
				return outer.execute( key, reducedParams );
			}

			return super.execute( key, params );
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( !(obj instanceof Chain) ) return false;

			Chain other = (Chain) obj;
			return inner.equals( other.inner ) && outer.equals( other.outer );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			outer.toString( builder );
			builder.append( inner, "," );
		}

		@Override
		public int getLevel()
		{
			return FUNCTION_LEVEL;
		}
	}
	
}

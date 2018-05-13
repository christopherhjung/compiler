package functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;
import tools.ReflectionUtils;
import tools.Run;

public class FunctionPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function";
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );
		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.variable";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				if ( key.equals( "call" ) )
				{
					Therm left = (Therm) params[0];

					if ( left.is( "variable" ) )
					{
						Object[] objs = (Object[]) params[1];
						return super.handle( left.get( "value", String.class ), objs );
					}
				}

				return super.handle( key, params );
			}
		} );

		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.callable";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				Therm left = (Therm) params[0];
				Object[] objs = (Object[]) params[1];

				return left.execute( "call", objs );
			}
		} );
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm therm = null;

		if ( left == null )
		{
			return null;
		}

		if ( parser.eat( '(' ) )
		{
			List<Therm> therms = new ArrayList<>();

			for ( ; parser.isNot( ')' ) ; )
			{
				Therm param = parser.parseWithLevelReset();
				therms.add( param );
				parser.eat( ',' );
			}

			parser.eat( ')' );

			Object[] params = therms.toArray();

			return (Therm) super.handle( "call", left, params );
		}

		return therm;
	}

	public class Chain extends Therm {

		private final Therm[] outer;
		private final Therm method;

		public Chain( Therm method, Therm... outer )
		{
			this.method = method;
			this.outer = outer;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				ArrayList<Object> builder = new ArrayList<>();
				builder.add( "derivate(" );
				builder.add( method );
				builder.add( ")*" );
				builder.add( outer.execute( "derivate" ) );
				builder.add( '(' );
				builder.add( inner[0] );
				builder.add( ')' );

				return eval( builder );
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

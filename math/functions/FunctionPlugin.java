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

		/*
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
		} );*/
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

			Therm[] params = therms.toArray( new Therm[therms.size()] );

			//Therm result = new Chain( left, params );
			//return result;
			
			return (Therm) super.handle( "call", left, params );
		}

		return therm;
	}

	public class Chain extends Therm {

		private final Therm[] params;
		private final Therm method;

		public Chain( Therm method, Therm... params )
		{
			this.method = method;
			this.params = params;
		}

		@Override
		public String getType()
		{
			return "chain";
		}
		
		@Override
		public EnginePlugin getPlugin()
		{
			return FunctionPlugin.this;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "method" ) )
			{
				return method;
			}
			else if ( key.equals( "params" ) )
			{
				return this.params;
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( method );
			builder.append( "(" );
			builder.append( params, "," );
			builder.append( ")" );
		}
	}

}

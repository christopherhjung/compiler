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
			else
			{
				return null;
			}
			
			String methodName = left.get( "value", String.class ).toLowerCase();

			Object[] params =  therms.toArray();
			therm = (Therm) handle( methodName, params );
			

			/*if ( therm == null )
			{
				therm = 			}*/
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

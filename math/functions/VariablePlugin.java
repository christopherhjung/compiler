package functions;

import parser.EnginePlugin;
import parser.MathEngine;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class VariablePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "variable";
	}

	@Override
	public Therm handle( MathParser parser )
	{
		if ( parser.is( Character::isAlphabetic ) )
		{
			String name = parser.eatWhile( Character::isAlphabetic );

			return new Variable( name );
		}

		return null;
	}

	public class Variable extends Therm {

		private final String name;

		public Variable( String name )
		{
			this.name = name;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return VariablePlugin.this;
		}

		public String getName()
		{
			return name;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "value" ) )
			{
				return name;
			}
			else if ( key.equals( "type" ) )
			{
				return "variable";
			}
			else if ( key.equals( "assign" ) && params.length == 1 )
			{
				Therm therm = (Therm) params[0];
				getPlugin().getEngine().currentScope.set( this, therm );
			}
			else if ( key.equals( "call" ) )
			{
				Therm therm = getPlugin().getEngine().currentScope.get( name );
				if ( therm != null )
				{
					therm = (Therm) therm.execute( "call", params );
				}
				System.out.println( therm );
				return therm;
			}

			return super.execute( key, params );
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( obj instanceof Variable ) return name.equals( ((Variable) obj).name );
			if ( obj instanceof String ) return name.equals( obj );

			return false;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( name );
		}

		@Override
		public int hashCode()
		{
			return name.hashCode();
		}
	}
}

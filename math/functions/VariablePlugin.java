package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class VariablePlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		if ( parser.is( Character::isAlphabetic ) )
		{
			StringBuilder builder = new StringBuilder();
			while ( parser.is( Character::isAlphabetic ) )
			{
				builder.append( parser.nextChar() );
			}

			return new Variable( builder.toString() );
		}

		return null;
	}

	public class Variable extends Therm {

		private final String name;

		public Variable( String name )
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				if ( params.length == 1 && params[0] instanceof Therm )
				{
					Therm derivateFor = (Therm) params[0];

					if ( derivateFor.execute( "type" ).equals( "variable" ) )
					{
						String name = derivateFor.get( "value", String.class );

						if ( this.name.equals( name ) )
						{
							return "1";
						}
						else
						{
							return "0";
						}
					}
				}
			}
			else if ( key.equals( "value" ) )
			{
				return name;
			}
			else if ( key.equals( "type" ) )
			{
				return "variable";
			}
			else if ( key.equals( "reduce" ) )
			{
				return this;
			}
			else if ( key.equals( "addreduce" ) && params.length == 1 )
			{
				return eval( this, "+", params[0] );
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
		public int getLevel()
		{
			return FUNCTION_LEVEL;
		}

		@Override
		public int hashCode()
		{
			return name.hashCode();
		}
	}
}

package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class NamePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "name";
	}

	@Override
	public Statement handle( MathParser parser )
	{
		parser.eatAll( ' ' );
		if ( parser.is( Character::isAlphabetic ) )
		{
			String name = parser.eatWhile( Character::isAlphabetic );

			Statement therm = (Statement) handle( name );

			if ( therm == null )
			{
				therm = new Name( name );
			}

			return therm;
		}

		return null;
	}

	public class Name extends Statement {

		private final String name;

		public Name( String name )
		{
			this.name = name;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return NamePlugin.this;
		}

		public String getName()
		{
			return name;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "value" ) )
			{
				return name;
			}

			return super.get( key, params );
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( obj instanceof Name ) return name.equals( ((Name) obj).name );
			if ( obj instanceof String ) return name.equals( obj );

			return false;
		}

		@Override
		public void toString( StatementStringifier builder )
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

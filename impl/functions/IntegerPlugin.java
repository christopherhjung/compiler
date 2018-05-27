package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.MathProgram;
import parser.Statement;
import parser.StatementStringifier;

public class IntegerPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "const";
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );

		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "sign.const";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				Statement therm = (Statement) params[0];
				if ( key.equals( "negate" ) && therm.is( "const" ) )
				{
					Integer value = therm.get( "value", Integer.class );

					return new Const( -value );
				}

				return super.handle( key, params );
			}
		} );
	}

	@Override
	public Statement handle( MathParser parser )
	{
		StringBuilder builder = new StringBuilder();

		parser.eatAll( ' ' );
		if ( parser.is( Character::isDigit ) )
		{
			while ( parser.is( Character::isDigit ) )
			{
				builder.append( parser.nextChar() );
			}
/*
			if ( parser.is( '.' ) )
			{
				builder.append( parser.nextChar() );

				while ( parser.is( Character::isDigit ) )
				{
					builder.append( parser.nextChar() );
				}
			}*/

			return new Const(Integer.parseInt( builder.toString() ));
		}

		return null;
	}
	
	
	public class Const extends Statement {

		private Integer value;

		public Const( Integer value )
		{
			this.value = value;
		}
		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "value" ) )
			{
				return this.value;
			}
			else if ( key.equals( "size" ) )
			{
				return 1;
			}

			return super.get( key, params );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return IntegerPlugin.this;
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( value );
		}
	}

}

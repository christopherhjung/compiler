package functions;

import parser.EnginePlugin;
import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;

public class ConstPlugin extends EnginePlugin {

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
				Therm therm = (Therm) params[0];
				if ( key.equals( "negate" ) && therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );

					return new Const( -value );
				}

				return super.handle( key, params );
			}
		} );
	}

	@Override
	public Therm handle( MathParser parser )
	{
		StringBuilder builder = new StringBuilder();

		parser.eatAll( ' ' );
		if ( parser.is( Character::isDigit ) )
		{
			while ( parser.is( Character::isDigit ) )
			{
				builder.append( parser.nextChar() );
			}

			if ( parser.is( '.' ) )
			{
				builder.append( parser.nextChar() );

				while ( parser.is( Character::isDigit ) )
				{
					builder.append( parser.nextChar() );
				}
			}

			return new Const( Double.parseDouble( builder.toString() ) );
		}

		return null;
	}

	public class Const extends Therm {

		private final double value;

		public Const( double value )
		{
			this.value = value;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return ConstPlugin.this;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "value" ) )
			{
				return value;
			}
			else if ( key.equals( "type" ) )
			{
				return "const";
			}

			return super.execute( key );
		}

		public double getValue()
		{
			return value;
		}

		@Override
		public boolean equals( Object obj )
		{
			return super.equals( obj ) || obj instanceof Const && value == ((Const) obj).value;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( value );
		}
	}
}

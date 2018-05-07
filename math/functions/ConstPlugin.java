package functions;

import parser.MathEngine;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class ConstPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser engine )
	{
		StringBuilder builder = new StringBuilder();

		if ( engine.is( Character::isDigit ) )
		{
			while ( engine.is( Character::isDigit ) )
			{
				builder.append( engine.nextChar() );
			}

			if ( engine.is( '.' ) )
			{
				builder.append( engine.nextChar() );

				while ( engine.is( Character::isDigit ) )
				{
					builder.append( engine.nextChar() );
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
		public MathEngine getEngine()
		{
			return ConstPlugin.this.getEngine();
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

		@Override
		public int getLevel()
		{
			return FUNCTION_LEVEL;
		}
	}
}

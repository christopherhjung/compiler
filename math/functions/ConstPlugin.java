package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import tools.ReflectionUtils;

public class ConstPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser engine )
	{
		StringBuilder builder = new StringBuilder();

		if ( engine.is( Character::isDigit ) )
		{
			while ( engine.isDigit() )
			{
				builder.append( engine.nextChar() );
			}

			if ( engine.is( '.' ) )
			{
				builder.append( engine.nextChar() );

				while ( engine.isDigit() )
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
			else if ( key.equals( "derivate" ) )
			{
				return eval( 0 );
			}
			else if ( key.equals( "addreduce" ) && params.length == 1 )
			{
				Therm therm = ReflectionUtils.as( params[0], Therm.class );
				if ( therm != null && therm.is( "const" ) )
				{
					double otherValue = therm.get( "value", Double.class );
					return new Const( value + otherValue );
				}

				return eval( this, "+", params[0] );
			}
			else if ( key.equals( "mulreduce" ) && params.length == 1 )
			{
				Therm therm = ReflectionUtils.as( params[0], Therm.class );
				if ( therm != null && therm.is( "const" ) )
				{
					double otherValue = therm.get( "value", Double.class );
					return eval( value * otherValue );
				}

				return eval( this, "*", params[0] );
			}
			else if ( key.equals( "reduce" ) && params.length == 0 )
			{
				return this;
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

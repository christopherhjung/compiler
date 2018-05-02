package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class SignPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		parser.eat( ' ' );
		boolean invert = false;
		if ( parser.eat( '-' ) )
		{
			invert = true;
		}
		else
		{
			parser.eat( '+' );
		}

		Therm therm = parser.parse();

		if ( invert )
		{
			therm = new Invert( therm );
		}

		return therm;
	}

	public class Invert extends Therm {

		private Therm therm;

		public Invert( Therm therm )
		{
			this.therm = therm;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "type" ) )
			{
				return therm.execute( key );
			}
			else if ( key.equals( "value" ) )
			{
				if ( therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );
					return -value;
				}
			}
			else if ( key.equals( "reduce" ) )
			{
				Therm inner = (Therm) therm.execute( "reduce" );

				if ( inner.is( "const" ) )
				{
					Double value = inner.get( "value", Double.class );

					if ( value < 0 )
					{
						return eval( -value );
					}
					else
					{
						return new Invert( inner );
					}
				}

				return new Invert( (Therm) therm.execute( "reduce" ) );
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "-" );
			builder.append( therm );
		}

		@Override
		public int getLevel()
		{
			return MULTIPLY_LEVEL;
		}
	}
}

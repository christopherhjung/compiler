package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class SignPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "sign";
	}

	@Override
	public Therm handle( MathParser parser )
	{
		parser.eatAll( ' ' );
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

		if ( invert && therm != null )
		{
			Therm negate = (Therm) handle( "negate", therm );

			
			if ( negate == null )
			{
				therm = new Negate( therm );
			}
			else
			{
				therm = negate;
			}
		}

		return therm;
	}

	public class Negate extends Therm {

		private Therm therm;

		public Negate( Therm therm )
		{
			this.therm = therm;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "type" ) )
			{
				return "negate";
			}
			else if ( key.equals( "value" ) )
			{
				return therm;
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "-" );
			builder.append( therm );
		}
	}
}

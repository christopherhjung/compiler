package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class ExponentPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "exponent";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm therm;
		if ( left != null )
		{
			therm = left;
		}
		else
		{
			therm = parser.parse();
		}

		if ( parser.eat( '^' ) )
		{
			therm = new Exponenional( therm, parser.parse() );
			return therm;
		}

		return null;
	}

	public class Exponenional extends BiTherm {

		public Exponenional( Therm basis, Therm exponent )
		{
			super( basis, exponent, "^" );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return ExponentPlugin.this;
		}

		@Override
		public String getType()
		{
			return "exponent";
		}
	}

}

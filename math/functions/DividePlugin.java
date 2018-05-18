package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class DividePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "divide";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		parser.eatAll( ' ' );
		if ( parser.eat( '/' ) )
		{
			return new Divide( left, parser.parse() );
		}

		return null;
	}

	public class Divide extends BiTherm {

		public Divide( Therm numerators, Therm denominators )
		{
			super( numerators, denominators, "/" );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return DividePlugin.this;
		}

		@Override
		public String getType()
		{
			return "divide";
		}
	}
}

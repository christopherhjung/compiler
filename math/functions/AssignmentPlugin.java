package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class AssignmentPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "assign";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}
		parser.eatAll( ' ' );
		if ( parser.eat( '=' ) )
		{
			Therm right = parser.parse();

			return new Assignment( left, right );
		}

		return null;
	}

	public class Assignment extends BiTherm {

		public Assignment( Therm left, Therm right )
		{
			super( left, right, "=" );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return AssignmentPlugin.this;
		}

		@Override
		public String getType()
		{
			return "assignment";
		}
	}
}

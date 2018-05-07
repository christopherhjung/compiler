package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import therms.Variable;

public class IncrementPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		if ( parser.eat( "++" ) )
		{
			Therm therm = parser.parse();
			if ( therm.is( "variable" ) )
			{
				return new Increment( therm );
			}
		}

		return null;
	}

	public static class Increment extends Therm {

		Therm inside;

		public Increment( Therm var )
		{
			inside = var;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "++" );
			builder.append( inside );
		}
		
		@Override
		public int getLevel()
		{
			return ADDITION_LEVEL;
		}

	}
}

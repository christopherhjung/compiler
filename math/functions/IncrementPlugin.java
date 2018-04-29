package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import therms.Variable;

public class IncrementPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		if ( parser.eat( '+' ) && parser.eat( '+' ) )
		{
			Therm therm = parser.parse();
			if ( therm instanceof Variable )
			{
				return new Increment( (Variable) therm );
			}
		}

		return null;
	}

	public static class Increment extends Therm {

		Variable inside;

		public Increment( Variable var )
		{
			inside = var;
		}

		@Override
		public Therm derivate( Variable name )
		{
			return null;
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

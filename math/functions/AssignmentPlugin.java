package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Equation;
import therms.Therm;
import therms.Variable;

public class AssignmentPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm therm;

		if ( left == null )
		{
			therm = parser.parse();
		}
		else
		{
			therm = left;
		}
		
		if ( therm instanceof Variable )
		{
			Variable var = (Variable) therm;

			if ( parser.eat( '=' ) )
			{
				Therm right = parser.parse();

				return new Assignment( var, right );
			}
		}

		return null;
	}

	public static class Assignment extends Therm {

		Variable left;
		Therm right;

		public Assignment( Variable left, Therm right )
		{
			this.left = left;
			this.right = right;
		}

		@Override
		public Therm derivate( Variable name )
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( left );
			builder.append( "=" );
			builder.append( right );
		}
		
		@Override
		public int getLevel()
		{
			return EQUATION_LEVEL;
		}
	}
}

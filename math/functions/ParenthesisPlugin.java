package functions;

import parser.EnginePlugin;
import parser.MathParser;
import therms.Therm;

public class ParenthesisPlugin extends EnginePlugin {
	@Override
	public String getName()
	{
		return "parenthesis";
	}

	@Override
	public Therm handle( MathParser parser )
	{
		Therm therm = null;
		parser.eatAll( ' ' );
		if ( parser.eat( '(' ) )
		{
			therm = parser.parseWithLevelReset();
			if ( !parser.eat( ')' ) )
			{
				return null;
			}
		}

		return therm;
	}
}

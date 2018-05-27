package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;

public class ParenthesisPlugin extends EnginePlugin {
	@Override
	public String getName()
	{
		return "parenthesis";
	}

	@Override
	public Statement handle( MathParser parser )
	{
		Statement therm = null;
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

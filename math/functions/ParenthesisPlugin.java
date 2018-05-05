package functions;

import parser.MathParser;
import therms.Therm;

public class ParenthesisPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		Therm therm = null;
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

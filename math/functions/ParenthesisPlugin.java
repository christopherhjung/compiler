package functions;

import parser.MathParser;
import therms.Therm;

public class ParenthesisPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		Therm therm = null;
		if ( parser.is( '(' ) )
		{
			parser.next();
			therm = parser.parseWithLevelReset();
			parser.next();
		}

		return therm;
	}
}

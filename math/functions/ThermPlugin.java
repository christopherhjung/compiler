package functions;

import parser.MathParser;
import therms.Therm;

public class ThermPlugin extends EnginePlugin{

	@Override
	public Therm handle( MathParser engine )
	{
		Therm therm = null;
		
		if( engine.getLevel() == 3 ){
			engine.eatAll( ' ' );
			if ( engine.is( '(' ) )
			{
				engine.next();
				engine.setLevel( 1 );
				therm = engine.parseTest();
				engine.next();
			}
		}
		
		return therm;
	}
	
}

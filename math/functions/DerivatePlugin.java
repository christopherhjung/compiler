package functions;

import therms.Therm;
import therms.Variable;

public class DerivatePlugin extends EnginePlugin {

	@EngineExecute
	public Therm execute( Therm therm, Variable var )
	{

		String str = (String) therm.execute( "derivate", var );
		if ( str == null )
		{
			return null;
		}

		return eval( str );
	}
}

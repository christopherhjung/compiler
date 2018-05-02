package functions;

import functions.VariablePlugin.Variable;
import therms.Therm;

public class DerivatePlugin extends EnginePlugin {

	@EngineExecute
	public Therm execute( Therm therm, Variable var )
	{

		Therm result = (Therm) therm.execute( "derivate", var );

		if ( result == null )
		{
			return null;
		}

		return result;
	}
}

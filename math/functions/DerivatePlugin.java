package functions;

import parser.MathParser;
import parser.MathProgram;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.Run;

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

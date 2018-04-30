package functions;

import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.Run;

public class ReducePlugin extends EnginePlugin {
	
	@EngineExecute
	public Therm execute( Therm therm )
	{
		Therm result = (Therm) therm.execute( "reduce" );

		if ( result == null )
		{
			return therm;
		}

		return result;
	}
}
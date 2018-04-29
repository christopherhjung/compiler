package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class ReducePlugin extends EnginePlugin {
	
	
	
	@EngineExecute
	public Therm execute( Therm therm )
	{
		return (Therm)therm.execute( "reduce" );
		//return therm.reduce( new VarSet() );
	}
}
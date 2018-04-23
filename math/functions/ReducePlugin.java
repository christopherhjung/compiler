package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class ReducePlugin extends EnginePlugin {


	public boolean handle( String message )
	{
		if ( message.equals( "derivate" ) )
		{

		}
		return false;
	}

	@EngineExecute
	public Therm execute( Therm therm )
	{
		return therm.reduce( new VarSet() );
	}
}
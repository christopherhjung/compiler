package functions;

import therms.Therm;

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
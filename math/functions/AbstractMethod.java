package functions;

import parser.ThermStringifier;
import therms.Therm;

public abstract class AbstractMethod extends Therm {

	@Override
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "call" ) )
		{
			return call( (Therm[]) params );
		}

		return super.execute( key, params );
	}

	public abstract Therm call( Therm[] params );

	@Override
	public String getType()
	{
		return "method";
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		// TODO Auto-generated method stub

	}

}

package functions;

import java.util.Arrays;

import parser.ThermStringifier;
import therms.Therm;

public abstract class AbstractMethod extends Therm {

	@Override
	public Object get( String key, Object... params )
	{
		if ( key.equals( "call" ) )
		{
			Therm[] therms = new Therm[params.length];
			for ( int i = 0 ; i < params.length ; i++ )
			{
				therms[i] = (Therm) params[i];
			}
			
			return call( therms );
		}

		return super.get( key, params );
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

package crap;

import therms.Chain;
import therms.Exponenional;
import therms.Therm;

public class Root extends Exponenional {

	public Root( Therm root, Therm basis )
	{
		super( basis, new Chain( new Polynominal( -1 ), root ) );
	}

	@Override
	public boolean contains( String var )
	{
		return true;
	}
}

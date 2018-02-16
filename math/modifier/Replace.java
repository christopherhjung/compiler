package modifier;

import therms.Therm;

public class Replace {

	public static Therm modify( Therm search, Therm replacer, Therm replacement )
	{
		return search.replace( replacer, replacement );
	}

}

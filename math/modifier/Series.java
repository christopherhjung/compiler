package modifier;

import functions.ConstPlugin.Const;
import maths.TaylorSeries;
import therms.Therm;

public class Series {

	public static Therm modify( Therm therm, Const degree )
	{
		return modify( therm, Const.ZERO, degree );
	}

	public static Therm modify( Therm therm, Const position, Const degree )
	{
		return TaylorSeries.envolve( therm, position.getValue(), (int)degree.getValue() );
	}

}

package modifier;

import matrix.Matrix;
import therms.Therm;

public class Det {

	public static Therm modify( Matrix matrix )
	{
		return matrix.trueDeterminant();
	}

}

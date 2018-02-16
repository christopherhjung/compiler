package modifier;

import matrix.Matrix;
import therms.Therm;

public class Inv {

	public static Therm modify( Matrix matrix )
	{
		return matrix.trueInverse();
	}

}

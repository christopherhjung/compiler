package modifier;

import therms.Const;
import therms.Equation;
import therms.Therm;
import therms.Variable;

public class Sum {

	
	
	public static Therm modify( Equation from, Const end, Therm therm )
	{
		Therm sum = Const.ZERO;
		Variable incre = (Variable) from.getLeft();
		Const start = (Const) from.getRight();
		int current = (int) start.getValue();
		int endValue = (int) end.getValue();

		System.out.println( endValue );
		
		if ( current > endValue )
		{
			throw new ArithmeticException( "end < start" );
		}

		for ( ; current <= endValue ; current++ )
		{
			sum = sum.add( therm.replace( incre, new Const( current ) ) );
		}

		return sum;
	}

}

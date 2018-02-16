package parser;

import java.lang.reflect.Method;

import therms.Const;
import therms.Therm;
import therms.Variable;

public class ThermSimplifier {

	public enum Behavior {
		ADD, MUL, EXP, CHAIN;
	}
	
	public static Therm simpify( Therm therm ){
		
		return null;
	}

	public static Therm simplify( Therm a, Therm b, Behavior behavior )
	{
		try
		{
			Method method = ThermSimplifier.class.getMethod( "simplify", a.getClass(), b.getClass(), Behavior.class );
			
			return (Therm) method.invoke( null, a, b, behavior );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		if ( behavior == Behavior.ADD )
		{
			if ( a.equals( b ) )
			{
				return a.mul( Const.TWO );
			}
		}
		else if ( behavior == Behavior.MUL )
		{
			if ( a.equals( b ) )
			{
				return a.pow( Const.TWO );
			}
		}

		return null;
	}

	public static Therm simplify( Variable a, Variable b, Behavior behavior )
	{
		System.out.println( "ss" );
		System.out.println( a + " " + b );
		return null;
	}
}

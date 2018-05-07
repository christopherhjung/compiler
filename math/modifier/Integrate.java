package modifier;

import functions.ConstPlugin.Const;
import functions.VariablePlugin.Variable;
import therms.Therm;

public class Integrate {

	public static Therm modify( Therm therm )
	{
		return modify( therm, Variable.X , Const.ZERO);
	}
	
	public static Therm modify( Therm therm, Const offset )
	{
		return modify( therm, Variable.X , offset);
	}
	
	public static Therm modify( Therm therm, Variable var )
	{
		return modify( therm, var, Const.ZERO );
	}

	public static Therm modify( Therm therm, Variable var, Const offset )
	{
		return therm.integrate( var, offset );
	}

}

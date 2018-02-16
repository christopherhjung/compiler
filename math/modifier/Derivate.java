package modifier;

import parser.ThermStringify;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Derivate extends Therm {

	public static Therm modify( Therm therm )
	{
		return modify( therm, Variable.X );
	}

	public static Therm modify( Therm therm, Variable var )
	{
		return therm.derivate( var );
	}

	public static Therm modify( Therm therm, Variable var, Const times )
	{
		return modify( therm, var, times.getValue() );
	}

	public static Therm modify( Therm therm, Variable var, double times )
	{
		if ( times % 1 != 0 ) throw new ArithmeticException( "No " + times + "th derivate possible" );

		for ( ; times > 0 ; times-- )
		{
			therm = therm.derivate( var );
		}

		return therm;
	}

	public static Therm modify( Therm therm, Variable var, Therm times )
	{
		return new Derivate( therm, var, times );
	}

	private Variable var;
	private Therm times;
	private Therm therm;

	public Derivate( Therm therm, Variable var, Therm times )
	{
		this.therm = therm;
		this.var = var;
		this.times = times;
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		Therm newTherm = therm.replace( replacer, replacement ).simplify();
		Therm newTimes = times.replace( replacer, replacement ).simplify();

		if ( newTimes instanceof Const )
		{
			return modify( newTherm, var, (Const) newTimes );
		}
		else
		{
			return new Derivate( newTherm, var, newTimes );
		}
	}

	@Override
	public Therm derivate( Variable name )
	{
		if ( var.equals( name ) )
		{
			return new Derivate( therm, var, times.add( Const.ONE ) );
		}
		else
		{
			return new Derivate( this, name, Const.ONE );
		}
	}

	@Override
	public void toString( ThermStringify builder )
	{
		builder.append( "derivate( " );
		builder.append( therm );
		builder.append( " , " );
		builder.append( var );
		builder.append( " , " );
		builder.append( times );
		builder.append( " )" );
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		return modify( therm, var, times.valueAt( varSet ) ).valueAt( varSet );
	}

	@Override
	public int getLevel()
	{
		return ZERO_LEVEL;
	}
}

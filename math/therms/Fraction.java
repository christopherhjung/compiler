package therms;

import parser.ThermStringifier;

public class Fraction extends Therm{
	private final int upper;
	private final int lower;
	
	public Fraction( int upper , int lower )
	{
		this.upper = upper;
		this.lower = lower;
	}

	@Override
	public Therm derivate( Variable name )
	{
		return Const.ZERO;
	}
	
	@Override
	public double reduce( VarSet varSet )
	{
		return (double)upper/lower;
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( upper );
		builder.append( "/" );
		builder.append( lower );
	}
	
	@Override
	public int getLevel()
	{
		return MULTIPLY_LEVEL;
	}
}

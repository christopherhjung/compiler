package therms;

import parser.ThermStringify;

public class Equation extends Therm {

	private final Therm left;
	private final Therm right;

	public Equation( Therm left, Therm right )
	{
		this.left = left;
		this.right = right;
	}

	public Therm getLeft()
	{
		return left;
	}

	public Therm getRight()
	{
		return right;
	}

	@Override
	public Therm derivate( Variable name )
	{
		return new Equation( left.derivate( name ), right.derivate( name ) );
	}
	
	@Override
	public double valueAt( VarSet varSet )
	{
		return Double.NaN;
	}
	
	@Override
	public int getLevel()
	{
		return EQUATION_LEVEL;
	}

	@Override
	public void toString( ThermStringify builder )
	{
		builder.append( left );
		builder.append( " = " );
		builder.append( right );
	}
}

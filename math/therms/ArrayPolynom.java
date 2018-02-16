package therms;

import crap.Polynominal;

public class ArrayPolynom extends Polynom {

	public double[] therms;

	public ArrayPolynom( double... therms )
	{
		this.therms = therms;
	}

	public ArrayPolynom( String name, double... therms )
	{
		super( name );
		this.therms = therms;
	}

	public Therm split()
	{
		return Polynominal.fromArray( therms );
	}

	@Override
	protected int size()
	{
		return therms.length;
	}

	@Override
	protected double get( int element )
	{
		return therms[element];
	}
}

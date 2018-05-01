package therms;

import parser.ThermStringifier;

public class Exponenional extends Therm {

	private final Therm basis;
	private final Therm exponent;

	public Exponenional( Therm basis, Therm exponent )
	{
		this.basis = basis;
		this.exponent = exponent;
	}

	public Therm getBasis()
	{
		return basis;
	}

	public Therm getExponent()
	{
		return exponent;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( super.equals( obj ) ) return true;
		if ( !(obj instanceof Exponenional) ) return false;
		Exponenional other = (Exponenional) obj;

		return basis.equals( other.basis ) && exponent.equals( other.exponent );
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( basis );
		builder.append( " ^ " );
		builder.append( exponent );
	}

	@Override
	public int getLevel()
	{
		return EXPONENT_LEVEL;
	}
}

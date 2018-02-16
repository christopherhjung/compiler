package therms;

import functions.Functions;
import parser.ThermStringify;

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
	public Therm derivate( Variable name )
	{
		if ( exponent.contains( name ) )
		{
			return mul( exponent.mul( new Chain( Functions.LOG.getTherm(), basis ) ).derivate( name ) );
		}
		else
		{
			return exponent.mul( basis.derivate( name ) ).mul( basis.pow( exponent.add( Const.MINUS_ONE ) ) );
		}
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		if ( equals( replacer ) )
		{
			return replacement;
		}
		
		Therm newBasis;
		Therm newExponent;

		if ( basis.equals( replacer ) )
		{
			newBasis = replacement;
		}
		else
		{
			newBasis = basis.replace( replacer, replacement );
		}

		if ( exponent.equals( replacer ) )
		{
			newExponent = replacement;
		}
		else
		{
			newExponent = exponent.replace( replacer, replacement );
		}

		if ( basis == newBasis && exponent == newExponent ) return this;

		
		return newBasis.pow( newExponent );
	}

	@Override
	public Therm contractMultiply( Therm therm )
	{
		if ( therm instanceof Exponenional )
		{
			Exponenional other = (Exponenional) therm;
			if ( basis.equals( other.basis ) )
			{
				return basis.pow( exponent.add( other.exponent ) );
			}
			else if ( exponent.equals( other.exponent ) )
			{
				return basis.mul( other.basis ).pow( exponent );
			}
		}
		else if ( therm instanceof Variable )
		{
			Variable other = (Variable) therm;
			if ( basis.equals( other ) ) return therm.pow( Const.ONE.add( exponent ) );
		}

		return super.contractMultiply( therm );
	}

	@Override
	public boolean contains( Therm var )
	{
		return basis.contains( var ) || exponent.contains( var );
	}

	@Override
	public Therm simplify()
	{
		Therm simplifiedBasis = basis.simplify();
		Therm simplifiedExponent = exponent.simplify();

		if ( simplifiedBasis instanceof Const && simplifiedExponent instanceof Const )
		{
			return new Const(
					Math.pow( ((Const) simplifiedBasis).getValue(), ((Const) simplifiedExponent).getValue() ) );
		}

		if ( simplifiedBasis == basis && simplifiedExponent == exponent )
		{
			return this;
		}

		return simplifiedBasis.pow( simplifiedExponent );
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
	public double valueAt( VarSet varSet )
	{
		return Math.pow( basis.valueAt( varSet ), exponent.valueAt( varSet ) );
	}
	
	@Override
	public void toString( ThermStringify builder )
	{
		if ( exponent.equals( Const.ONE ) )
		{
			builder.append( basis );
		}
		else
		{
			builder.append( basis );
			builder.append( " ^ " );
			builder.append( exponent );
		}
	}

	@Override
	public int getLevel()
	{
		return EXPONENT_LEVEL;
	}
}

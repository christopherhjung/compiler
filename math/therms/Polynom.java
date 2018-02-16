package therms;

import parser.ThermStringify;

public abstract class Polynom extends Therm {

	public final String name;

	public Polynom()
	{
		this.name = "x";
	}

	public Polynom( String name )
	{
		this.name = name;
	}

	protected abstract int size();

	protected abstract double get( int element );
	
	@Override
	public double valueAt( VarSet varSet )
	{
		double pos = varSet.getValue( Variable.X );
		double result = 0;
		for ( int i = size() - 1 ; i >= 0 ; i-- )
		{
			result = result * pos + get( i );
		}
		return result;
	}

	@Override
	public Therm derivate( Variable name )
	{
		if ( size() == 1 )
		{
			return Const.ZERO;
		}

		double[] newTherms = new double[size() - 1];
		for ( int i = 1 ; i < size() ; i++ )
		{
			newTherms[i - 1] = get( i ) * i;
		}

		return new ArrayPolynom( newTherms );
	}

	@Override
	public Polynom integrate( Variable name, Const constant )
	{		
		double[] newTherms = new double[size() + 1];
		newTherms[0] = constant.getValue();
		for ( int i = 0 ; i < size() ; )
		{
			newTherms[i + 1] = get( i ) * (1.0 / ++i);
		}
		return new ArrayPolynom( newTherms );
	}

	public Polynom multiply( Polynom other )
	{
		double[] newTherms = new double[(size() - 1) * (other.size() - 1) + 1];
		for ( int i = 0 ; i < size() ; i++ )
		{
			for ( int j = 0 ; j < other.size() ; j++ )
			{
				newTherms[i + j] += get( i ) * other.get( j );
			}
		}
		return new ArrayPolynom( newTherms );
	}

	@Override
	public Therm contractMultiply( Therm therm )
	{
		if ( therm instanceof Polynom )
		{
			Polynom other = (Polynom) therm;
			return multiply( other );
		}
		return null;
	}

	@Override
	public boolean contains( Therm var )
	{
		return var.equals( name ) && size() > 0;
	}

	@Override
	public Therm simplify()
	{
		if ( size() == 1 ) return new Const( get( 0 ) );

		return this;
	}

	@Override
	public void toString( ThermStringify builder )
	{
		for ( int i = size() - 1 ; i >= 0 ; i-- )
		{
			if ( get( i ) != 0 )
			{
				if ( get( i ) > 0 )
				{
					if ( builder.length() > 2 )
					{
						builder.append( " + " );
					}
				}
				else if ( get( i ) < 0 )
				{
					builder.append( " - " );
				}

				double value = Math.abs( get( i ) );

				if ( i == 0 )
				{
					builder.append( value );
				}
				else if ( i == 1 )
				{
					if ( value == 1 )
					{
						builder.append( name );
					}
					else
					{
						builder.append( value ).append( " * " ).append( name );
					}
				}
				else
				{
					if ( value == 1 )
					{
						builder.append( name ).append( " ^ " ).append( i );
					}
					else
					{
						builder.append( value ).append( " *  " ).append( name ).append( " ^ " ).append( i );
					}
				}
			}
		}
	}

	@Override
	public int getLevel()
	{
		return ADDITION_LEVEL;
	}
}

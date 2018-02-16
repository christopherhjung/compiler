package therms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import builder.AdditionalBuilder;
import builder.ThermBuilder;
import parser.ThermStringify;
import tools.ListComparer;

public class Additional extends Therm implements Iterable<Therm> {

	private final List<Therm> therms;

	public Additional()
	{
		therms = new ArrayList<>();
	}

	public <T extends Therm> Additional( T... therms )
	{
		this.therms = Arrays.asList( therms );
	}

	public Additional( List<? extends Therm> therms )
	{
		this.therms = new ArrayList<>( therms );
	}

	public List<Therm> getTherms()
	{
		return therms;
	}

	@Override
	public Therm derivate( Variable name )
	{
		AdditionalBuilder builder = new AdditionalBuilder();
		for ( Therm therm : this )
		{
			builder.add( therm.derivate( name ) );
		}

		return builder.build();
	}

	private int size()
	{
		return therms.size();
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		double result = 0;
		for ( Therm therm : this )
		{
			result += therm.valueAt( varSet );
		}
		return result;
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		if ( equals( replacer ) ) return replacement;

		AdditionalBuilder builder = new AdditionalBuilder( therms );

		for ( int i = 0 ; i < therms.size() ; i++ )
		{
			builder.set( i, therms.get( i ).replace( replacer, replacement ) );
		}

		return builder.build();
	}

	@Override
	public Therm simplify()
	{
		List<Therm> newTherms = new ArrayList<>( 3 );

		compute( therms, newTherms );

		if ( newTherms.size() == 0 ) return Const.ZERO;
		if ( newTherms.size() == 1 ) return newTherms.get( 0 );

		return new Additional( newTherms );
	}

	private static void compute( List<Therm> oldTherms, List<Therm> newTherms )
	{
		loop: for ( Therm therm : oldTherms )
		{
			therm = therm.simplify();

			if ( therm instanceof Additional )
			{
				compute( ((Additional) therm).therms, newTherms );
			}
			else if ( !therm.equals( Const.ZERO ) )
			{
				for ( int i = 0 ; i < newTherms.size() ; i++ )
				{
					Therm smaller = newTherms.get( i ).contractAdditional( therm );
					if ( smaller != null )
					{
						newTherms.set( i, smaller.simplify() );
						continue loop;
					}
				}

				newTherms.add( therm );
			}
		}
	}

	@Override
	public boolean contains( Therm var )
	{
		for ( Therm therm : this )
		{
			if ( therm.contains( var ) )
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public Therm contractAdditional( Therm therm )
	{
		if ( therm instanceof Additional )
		{
			Additional other = (Additional) therm;
			List<Therm> therms = new ArrayList<>( this.therms );
			therms.addAll( other.therms );
			return new Additional( therms );
		}

		return super.contractAdditional( therm );
	}

	@Override
	public Therm contractMultiply( Therm therm )
	{
		if ( therm instanceof Additional )
		{
			Additional other = (Additional) therm;
			List<Therm> newTherms = new ArrayList<>();

			for ( Therm element : therms )
			{
				for ( Therm otherElement : other )
				{
					newTherms.add( element.mul( otherElement ) );
				}
			}
			return new Additional( newTherms );
		}
		else
		{
			List<Therm> newTherms = new ArrayList<>();
			for ( Therm element : therms )
			{
				newTherms.add( element.mul( therm ) );
			}
			return new Additional( newTherms );
		}

		// return super.multiplyTherm( therm );
	}

	@Override
	public Iterator<Therm> iterator()
	{
		return therms.iterator();
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( super.equals( obj ) ) return true;
		if ( !(obj instanceof Additional) ) return false;

		Additional other = (Additional) obj;
		return ListComparer.containsSame( therms, other.therms );
	}

	@Override
	public void toString( ThermStringify builder )
	{
		for ( int i = 0 ; i < therms.size() ; i++ )
		{
			if ( i != 0 ) builder.append( " + " );
			builder.append( therms.get( i ) );
		}
	}

	@Override
	public int getLevel()
	{
		return ADDITION_LEVEL;
	}
}

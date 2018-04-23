package therms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import builder.ThermBuilder;
import parser.ThermStringifier;
import tools.ListComparer;

public class Multiply extends Therm implements Iterable<Therm> {

	private List<Therm> therms;

	public Multiply()
	{
		this.therms = new ArrayList<>();
	}

	public <T extends Therm> Multiply( T... therms )
	{
		this.therms = Arrays.asList( therms );
	}

	public Multiply( Collection<? extends Therm> therms )
	{
		this.therms = new ArrayList<>( therms );
	}

	public List<Therm> getTherms()
	{
		return therms;
	}

	@Override
	public Therm reduce( VarSet varSet, Therm... params )
	{
		Therm result = null;
		for ( Therm therm : this )
		{
			Therm next = therm.reduce( varSet );

			if ( result == null )
			{
				result = next;
			}
			else
			{
				result = result.mul( next );
			}
		}
		return result;
	}

	@Override
	public Therm derivate( Variable name )
	{
		ThermBuilder additionalBuilder = new AdditionalBuilder();
		for ( int i = 0 ; i < therms.size() ; i++ )
		{
			ThermBuilder multiplyBuilder = new MultiplyBuilder();
			int j = 0;
			for ( Therm therm : therms )
			{
				if ( i == j )
				{
					multiplyBuilder.add( therm.derivate( name ) );
				}
				else
				{
					multiplyBuilder.add( therm );
				}
				j++;
			}
			additionalBuilder.add( multiplyBuilder.build() );
		}

		return additionalBuilder.build();
	}

	private static boolean compute( List<Therm> oldTherms, List<Therm> newTherms )
	{
		loop: for ( Therm therm : oldTherms )
		{
			therm = therm.simplify();
			if ( therm.equals( Const.ZERO ) )
			{
				return false;
			}
			else if ( therm.equals( Const.ONE ) )
			{
				continue;
			}
			else if ( therm instanceof Multiply )
			{
				if ( !compute( ((Multiply) therm).therms, newTherms ) )
				{
					return false;
				}
				continue loop;
			}

			for ( int i = 0 ; i < newTherms.size() ; i++ )
			{
				Therm smaller = newTherms.get( i ).contractMultiply( therm );
				if ( smaller != null )
				{
					newTherms.set( i, smaller.simplify() );
					continue loop;
				}
			}

			newTherms.add( therm );
		}

		return true;
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
		if ( !(obj instanceof Multiply) ) return false;
		Multiply other = (Multiply) obj;

		return ListComparer.containsSame( therms, other.therms );
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( therms, " * " );
	}

	@Override
	public int getLevel()
	{
		return MULTIPLY_LEVEL;
	}
}

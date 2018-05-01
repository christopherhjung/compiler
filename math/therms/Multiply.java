package therms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "derivate" ) )
		{
			ArrayList<Object> list = new ArrayList<>();
			for ( int i = 0 ; i < therms.size() ; i++ )
			{
				if ( i > 0 )
				{
					list.add( '+' );
				}

				for ( int j = 0 ; j < therms.size() ; j++ )
				{
					if ( j > 0 )
					{
						list.add( '*' );
					}

					if ( i == j )
					{
						list.add( therms.get( j ).execute( key, params ) );
					}
					else
					{
						list.add( therms.get( j ) );
					}
				}
			}

			return list.toString();
		}
		else if ( key.equals( "reduce" ) )
		{
			Therm therm = (Therm)therms.get( 0 ).execute( "reduce" );

			for ( int i = 1 ; i < therms.size() ; i++ )
			{
				therm = (Therm) therm.execute( "mulreduce", therms.get( i ).execute( "reduce" ) );
			}

			return therm;
		}

		return super.execute( key, params );
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

package therms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import builder.AdditionalBuilder;
import parser.ThermStringifier;
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
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "derivate" ) )
		{
			StringBuilder builder = new StringBuilder();
			for ( Therm therm : this )
			{
				if ( builder.length() > 0 )
				{
					builder.append( '+' );
				}
				
				builder.append( therm.execute( key, params ).toString() );
			}

			return builder.toString();
		}

		return super.execute( key, params );
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
	public Therm reduce( VarSet varSet, Therm... therms )
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
				result = result.add( next );
			}
		}

		if ( result == null ) return new Neutral();

		return result;
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
	public void toString( ThermStringifier builder )
	{
		builder.append( therms, " + " );
	}

	@Override
	public int getLevel()
	{
		return ADDITION_LEVEL;
	}
}

package therms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

				builder.append( therm.execute( key, params ) );
			}
			

			return builder.toString();
		}
		else if ( key.equals( "reduce" ) )
		{
			Therm therm = (Therm)therms.get( 0 ).execute( "reduce" );

			for ( int i = 1 ; i < therms.size() ; i++ )
			{
				therm = (Therm) therm.execute( "addreduce", therms.get( i ).execute( "reduce" ) );
			}

			return therm;
		}

		return super.execute( key, params );
	}

	private int size()
	{
		return therms.size();
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

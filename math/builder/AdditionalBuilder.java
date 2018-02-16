package builder;

import java.util.ArrayList;
import java.util.List;

import therms.Additional;
import therms.Const;
import therms.Therm;

public class AdditionalBuilder implements ThermBuilder {

	private ArrayList<Therm> list;

	public AdditionalBuilder()
	{
		list = new ArrayList<>();
	}

	public AdditionalBuilder( List<Therm> therms )
	{
		list = new ArrayList<>( therms );
	}

	@Override
	public Therm build()
	{
		if ( list.size() == 0 ) return Const.ZERO;
		if ( list.size() == 1 ) return list.get( 0 );

		return new Additional( list );
	}

	@Override
	public void add( Therm therm )
	{
		list.add( therm );
	}

	public void set( int index, Therm therm )
	{
		if ( therm.equals( Const.ZERO ) )
		{
			list.remove( index );
		}
		else
		{
			list.set( index, therm );
		}

	}

}

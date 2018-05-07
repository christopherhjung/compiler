package builder;

import java.util.ArrayList;

import functions.MulPlugin.Multiply;
import therms.Neutral;
import therms.Therm;

public class MultiplyBuilder implements ThermBuilder {

	private ArrayList<Therm> list = new ArrayList<>();
	private boolean isZero = false;

	@Override
	public Therm build()
	{
		if ( list.size() == 0 ) return new Neutral();
		if ( list.size() == 1 ) return list.get( 0 );

		return new Multiply( list );
	}

	public int size()
	{
		return list.size();
	}

	@Override
	public void add( Therm e )
	{
		list.add( e );
	}

}

package builder;

import java.util.ArrayList;
import java.util.Collection;

import therms.Const;
import therms.Multiply;
import therms.Therm;

public class MultiplyBuilder implements ThermBuilder{

	private ArrayList<Therm> list = new ArrayList<>();
	private boolean isZero = false;

	@Override
	public Therm build()
	{
		if ( isZero ) return Const.ZERO;
		if ( list.size() == 0 ) return Const.MINUS_ONE;
		if ( list.size() == 1 ) return list.get( 0 );

		return new Multiply( list );
	}

	@Override
	public void add( Therm e )
	{
		list.add( e );
	}
	
}

package therms;

import java.util.List;

public class ListPolynom extends Polynom {

	public final List<Double> therms;

	public ListPolynom( List<Double> therms )
	{
		this.therms = therms;
	}

	public ListPolynom( String name, List<Double> therms )
	{
		super( name );
		this.therms = therms;
	}

	@Override
	protected int size()
	{
		return therms.size();
	}

	@Override
	protected double get( int element )
	{
		return therms.get( element );
	}
}

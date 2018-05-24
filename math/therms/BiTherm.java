package therms;

import parser.ThermStringifier;

public class BiTherm extends Therm {

	private Therm left;
	private Therm right;
	private String linker;

	public BiTherm( Therm left, Therm right, String linker )
	{
		this.left = left;
		this.right = right;
		this.linker = linker;
	}

	@Override
	public Object get( String key, Object... params )
	{
		if ( key.equals( "left" ) )
		{
			return this.left;
		}
		else if ( key.equals( "right" ) )
		{
			return this.right;
		}

		return super.get( key, params );
	}
	
	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( left );
		builder.append( linker );
		builder.append( right );
	}
}


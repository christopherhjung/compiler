package parser;

import therms.Therm;

public class ThermStringify {
	private StringBuilder builder = new StringBuilder();
	private int last;

	public int length()
	{
		return builder.length();
	}

	public int getLast()
	{
		return last;
	}

	public ThermStringify append( String str )
	{
		builder.append( str );
		return this;
	}

	public ThermStringify append( double number )
	{
		builder.append( number );
		return this;
	}

	public ThermStringify append( Therm therm )
	{
		int level = therm.getLevel();
		boolean inner = level <= last;

		int previous = last;
		last = level;
		if ( inner )
		{
			append( "(" );
		}
		therm.toString( this );
		if ( inner )
		{
			append( ")" );
		}

		last = previous;
		return this;
	}

	@Override
	public String toString()
	{
		return builder.toString();
	}
}

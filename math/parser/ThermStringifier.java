package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import therms.Therm;

public class ThermStringifier {
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

	public StringBuilder getBuilder()
	{
		return builder;
	}

	public ThermStringifier append( String str )
	{
		builder.append( str );
		return this;
	}

	public ThermStringifier append( double number )
	{
		builder.append( number );
		return this;
	}

	public <T extends Therm> ThermStringifier append( T[] elements, String limiter )
	{
		return append( Arrays.asList( elements ), limiter );
	}

	public <T extends Therm> ThermStringifier append( List<T> elements, String limiter )
	{
		boolean first = true;

		for ( T therm : elements )
		{
			if ( !first )
			{
				append( limiter );
			}

			append( therm );

			first = false;
		}

		return this;
	}

	public ThermStringifier append( Therm therm )
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

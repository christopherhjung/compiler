package parser;

import java.util.Arrays;
import java.util.List;

public class StatementStringifier {
	private StringBuilder builder = new StringBuilder();
	private int last;
	private int level = 0;

	public StatementStringifier()
	{
		resetLast();
	}

	public int length()
	{
		return builder.length();
	}

	public int getLast()
	{
		return last;
	}
	
	public int getLevel()
	{
		return level;
	}

	public void incLevel()
	{
		++level;
	}

	public void decLevel()
	{
		--level;
	}

	public int resetLast()
	{
		int temp = this.last;
		this.last = Integer.MIN_VALUE;
		return temp;
	}

	public void setLast( int last )
	{
		this.last = last;
	}

	public StringBuilder getBuilder()
	{
		return builder;
	}

	public StatementStringifier append( Object obj )
	{
		if ( obj instanceof Statement )
		{
			append( (Statement) obj );
		}
		else
		{
			append( obj.toString() );
		}
		return this;
	}

	public StatementStringifier append( String str )
	{
		builder.append( str );
		return this;
	}

	public StatementStringifier append( Object str, int repeat )
	{
		for ( ; --repeat >= 0 ; )
		{
			append( str );
		}
		return this;
	}

	public StatementStringifier append( double number )
	{
		builder.append( number );
		return this;
	}

	public <T extends Statement> StatementStringifier append( T[] elements, String limiter )
	{
		return append( Arrays.asList( elements ), limiter );
	}

	public <T extends Statement> StatementStringifier append( List<T> elements, String limiter )
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

	public StatementStringifier append( Statement therm )
	{
		int level = therm.getLevel();
		boolean inner = level < last;

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

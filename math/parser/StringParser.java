package parser;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public abstract class StringParser<T> {

	private int position;
	private char[] chars;
	private boolean idle = true;

	protected abstract T parse();

	public T eval( char[] chars )
	{
		if ( !idle )
		{
			throw new ParseException( "Parser is already started" );
		}

		idle = false;

		resetBuffer( chars );
		reset();

		T result;
		try
		{
			result = parse();
		}
		finally
		{
			idle = true;
		}

		if ( hasCurrent() ) throw new ParseException( this );

		return result;
	}

	public T eval( String str )
	{
		return eval( str.toCharArray() );
	}

	protected void reset()
	{
		position = 0;
	}

	public void next()
	{
		position++;
	}

	public int getPosition()
	{
		return position;
	}

	protected void setPosition( int position )
	{
		this.position = position;
	}

	public void resetBuffer( char[] str )
	{
		this.chars = str;
		this.position = 0;
	}

	public void resetBuffer( String str )
	{
		resetBuffer( str.toCharArray() );
	}

	private void setBuffer( int position, char[] str )
	{
		resetBuffer( new String( str ) );
		this.position = position;
	}

	public char getChar()
	{
		return chars[getPosition()];
	}

	public char nextChar()
	{
		char temp = getChar();
		next();
		return temp;
	}

	public boolean hasCurrent()
	{
		return position < chars.length;
	}

	public boolean hasNext()
	{
		return position + 1 < chars.length;
	}

	public boolean is( char cha )
	{
		return hasCurrent() && getChar() == cha;
	}

	public boolean isNot( char cha )
	{
		return hasCurrent() && getChar() != cha;
	}

	public boolean eat( char cha )
	{
		if ( is( cha ) )
		{
			next();
			return true;
		}

		return false;
	}

	public String eatWhile( Predicate<Character> predicate )
	{
		StringBuilder sb = new StringBuilder();

		while ( hasCurrent() && predicate.test( getChar() ) )
		{
			sb.append( nextChar() );
		}

		return sb.toString();
	}

	public boolean eat( String str )
	{
		RestoreAction state = getRestorePoint();
		for ( char cha : str.toCharArray() )
		{
			if ( !is( cha ) )
			{
				state.restore();
				return false;
			}

			next();
		}

		return true;
	}

	public boolean eatAll( char cha )
	{
		if ( is( cha ) )
		{
			do
			{
				next();
			}
			while ( is( cha ) );

			return true;
		}

		return false;
	}

	public boolean is( Predicate<Character> predicate )
	{
		return hasCurrent() && predicate.test( getChar() );
	}

	public boolean isAlpha()
	{
		return is( Character::isAlphabetic );
	}

	public boolean isDigit()
	{
		return is( Character::isDigit );
	}

	protected static abstract class RestoreAction {
		public abstract void restore();
	}

	protected RestoreAction getRestorePoint()
	{
		return new RestoreActionImpl( getPosition(), chars );
	}

	private class RestoreActionImpl extends RestoreAction {

		private int position = getPosition();
		private char[] chars = this.chars;

		public RestoreActionImpl( int position, char[] chars )
		{
			this.position = position;
			this.chars = chars;
		}

		@Override
		public void restore()
		{
			setBuffer( position, chars );
		}

		@Override
		public int hashCode()
		{
			return position * chars.hashCode();
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( !(obj instanceof StringParser.RestoreActionImpl) ) return false;
			StringParser<T>.RestoreActionImpl other = (StringParser<T>.RestoreActionImpl) obj;

			return other.position == this.position && other.chars == this.chars;
		}
	};

	@Override
	public String toString()
	{
		if ( !hasCurrent() ) return "Parse finished";
		return new String( chars, position, chars.length - position );
	}
}

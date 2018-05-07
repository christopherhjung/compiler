package parser;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringParser<T> {

	private int position;
	private char[] chars;
	private AtomicBoolean idle = new AtomicBoolean( true );

	public abstract T parse();

	public final T eval( char[] chars )
	{
		if ( !idle.compareAndSet( true, false ) )
		{
			throw new ParseException( "Parser is already started" );
		}

		this.chars = chars;
		reset();
		T result;
		try
		{
			result = parse();
		}
		finally
		{
			idle.set( true );
		}

		if ( hasNext() ) throw new ParseException( this );

		return result;
	}

	public final T eval( String str )
	{
		return eval( str.replace( " ", "" ).toCharArray() );
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

	public boolean hasNext()
	{
		return position < chars.length;
	}

	public boolean is( char cha )
	{
		return hasNext() && getChar() == cha;
	}

	public boolean isNot( char cha )
	{
		return hasNext() && getChar() != cha;
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

		while ( hasNext() && predicate.test( getChar() ) )
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
		return hasNext() && predicate.test( getChar() );
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
		int position = getPosition();
		return new RestoreAction() {
			@Override
			public void restore()
			{
				setPosition( position );
			}
		};
	}

	@Override
	public String toString()
	{
		if ( !hasNext() ) return "Parse finished";
		return new String( chars, position, chars.length - position );
	}
}

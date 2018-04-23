package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringParser<T> {

	private int position;
	private char[] chars;

	protected abstract T parse();

	public T eval( char[] chars )
	{
		reset( chars );
		return parse();
	}

	public T eval( String str )
	{
		reset( str.toCharArray() );
		return parse();
	}

	private final void reset( char[] chars )
	{
		position = 0;
		this.chars = chars;
	}

	protected void next()
	{
		position++;
	}

	protected int getPosition()
	{
		return position;
	}

	protected void setPosition( int position )
	{
		this.position = position;
	}
	
	protected char getChar()
	{
		return chars[position];
	}

	protected char nextChar()
	{
		return chars[position++];
	}

	protected boolean hasNext()
	{
		return position < chars.length;
	}

	protected boolean is( char cha )
	{
		return hasNext() && getChar() == cha;
	}

	protected boolean isNot( char cha )
	{
		return hasNext() && getChar() != cha;
	}
	
	protected boolean eat( char cha )
	{
		if ( is( cha ) )
		{
			next();
			return true;
		}

		return false;
	}

	protected boolean eatAll( char cha )
	{
		if ( is( cha ) )
		{
			do{
				position++;
			}while ( is( cha ) );
			
			return true;
		}
		
		return false;
	}

	protected boolean isAlpha()
	{
		return hasNext() && Character.isAlphabetic( getChar() );
	}

	protected boolean isDigit()
	{
		return hasNext() && Character.isDigit( getChar() );
	}

	protected String readFor( Pattern pattern )
	{
		Matcher matcher = pattern.matcher( new String(chars) );
		matcher.region( getPosition(), chars.length - 1 );	
		matcher.find( position );
		if ( matcher.find() )
		{
			position = matcher.end();
			return matcher.group();
		}
		return null;
	}

	protected boolean matches( String regex )
	{
		return Character.toString( getChar() ).matches( regex );
	}
}

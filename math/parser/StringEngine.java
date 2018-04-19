package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringEngine<T> {

	private int position;
	private char[] chars;

	protected abstract T parse();

	public T eval( char[] chars )
	{
		init( chars );
		return parse();
	}

	public T eval( String str )
	{
		init( str.toCharArray() );
		return parse();
	}

	private final void init( char[] chars )
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

	protected boolean hasNext( char cha )
	{
		return hasNext() && getChar() == cha;
	}

	protected boolean eatNext( char cha )
	{
		if ( hasNext( cha ) )
		{
			next();
			return true;
		}

		return false;
	}

	protected boolean eatAll( char cha )
	{
		if ( hasNext( cha ) )
		{
			do{
				position++;
			}while ( hasNext( cha ) );
			
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

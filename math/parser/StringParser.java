package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringParser<T> {

	private int position;
	private char[] chars;

	protected abstract T parse();
	protected abstract T parseTest();

	public T eval( char[] chars )
	{
		reset( chars );
		return parseImpl();
	}

	public T eval( String str )
	{
		reset( str.toCharArray() );
		return parseImpl();
	}
	
	private T parseImpl(){
		T result = parseTest();
		
		if(hasNext()) throw new ParseException( this );
		
		return result;
	}

	protected void reset( char[] chars )
	{
		position = 0;
		this.chars = chars;
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
		return chars[position];
	}

	public char nextChar()
	{
		return chars[position++];
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

	public boolean eatAll( char cha )
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

	public boolean isAlpha()
	{
		return hasNext() && Character.isAlphabetic( getChar() );
	}

	public boolean isDigit()
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

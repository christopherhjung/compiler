package therms;

import java.util.Objects;

import parser.MathEngine;
import parser.ThermStringifier;

public abstract class Therm {
	public final static int ZERO_LEVEL = 0;
	public final static int EQUATION_LEVEL = 1;
	public final static int ADDITION_LEVEL = 2;
	public final static int MULTIPLY_LEVEL = 3;
	public final static int EXPONENT_LEVEL = 4;
	public final static int FUNCTION_LEVEL = 5;

	public MathEngine getEngine()
	{
		return null;
	}

	public <T> T get( String key, Class<T> type )
	{
		Object result = execute( key );

		if ( result != null && type.isAssignableFrom( result.getClass() ) )
		{
			return type.cast( result );
		}

		return null;
	}

	public String getType()
	{
		return null;
	}

	public boolean is( String type )
	{
		return Objects.equals( type, execute( "type" ) );
	}

	public Object execute( String key, Object... params )
	{
		if ( key.equals( "do" ) )
		{
			return this;
		}
		else if ( key.equals( "type" ) )
		{
			return getType();
		}

		return null;
	}

	public abstract void toString( ThermStringifier builder );

	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}

	public String toString()
	{
		return new ThermStringifier().append( this ).toString();
	}
}

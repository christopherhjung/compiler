package therms;

import java.util.Objects;

import parser.EnginePlugin;
import parser.MathEngine;
import parser.ThermStringifier;

public abstract class Therm {

	public EnginePlugin getPlugin()
	{
		return null;
	}
	
	public <T> T get( String key, Class<T> type )
	{
		Object result = get( key );

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
		return Objects.equals( type, get( "type" ) );
	}

	public Object get( String key, Object... params )
	{
		if ( key.equals( "insert" ) || key.equals( "call" ) )
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
		if ( getPlugin() == null )
		{
			return 0;
		}
		
		return getPlugin().getEngine().getLevel( getPlugin() );
	}

	public String toString()
	{
		return new ThermStringifier().append( this ).toString();
	}
}

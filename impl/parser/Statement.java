package parser;

import java.util.Objects;

public abstract class Statement {

	public EnginePlugin getPlugin()
	{
		return null;
	}
	
	public <T> T get( String key, Class<T> type )
	{
		Object result = getImpl( key );

		if ( result != null && type.isAssignableFrom( result.getClass() ) )
		{
			return type.cast( result );
		}

		return null;
	}

	public Statement get( String key )
	{
		Object result = getImpl( key );

		if ( result != null && Statement.class.isAssignableFrom( result.getClass() ) )
		{
			return(Statement) result ;
		}

		return null;
	}
	
	public String getType()
	{
		return getPlugin().getName();
	}

	public boolean is( String type )
	{
		return Objects.equals( type, getImpl( "type" ) );
	}

	protected Object getImpl( String key )
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

	public abstract void toString( StatementStringifier builder );

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
		return new StatementStringifier().append( this ).toString();
	}
}

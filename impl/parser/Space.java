package parser;

import java.util.HashMap;
import java.util.Map;

public class Space {

	private Scope global;

	public Space()
	{
		global = new Scope( null );
	}

	public Scope getGlobalScope()
	{
		return global;
	}

	public static class Scope {

		private Scope parentScope;
		private Map<String, Statement> map;

		public Scope()
		{
			this( null );
		}
		
		public void setParentScope( Scope parentScope )
		{
			this.parentScope = parentScope;
		}
		
		public Scope getParentScope()
		{
			return parentScope;
		}

		public Scope( Scope parentScope )
		{
			this.parentScope = parentScope;
			this.map = new HashMap<>();
		}

		public Statement get( Object key )
		{
			if ( map.containsKey( key ) )
			{
				return map.get( key );
			}
			else if ( parentScope != null )
			{
				return parentScope.get( key );
			}

			return null;
		}

		public void set( Object key, Statement value )
		{
			map.put( getKey( key ), value );
		}

		public static String getKey( Object key )
		{
			if ( key instanceof Statement )
			{
				Statement therm = (Statement) key;
				if ( !therm.is( "name" ) )
				{
					throw new RuntimeException( "Only variables as key allowed" );
				}

				return therm.get( "value", String.class );
			}
			else
			{
				return key.toString();
			}
		}
	}
}

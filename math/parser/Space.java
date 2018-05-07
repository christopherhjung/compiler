package parser;

import java.util.HashMap;
import java.util.Map;

import therms.Therm;

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
		private Map<String, Therm> map;

		public Scope()
		{
			this( null );
		}

		public Scope( Scope parentScope )
		{
			this.parentScope = parentScope;
			this.map = new HashMap<>();
		}

		public Therm get( Object key )
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

		public void set( Object key, Therm value )
		{
			map.put( getKey( key ), value );
		}

		public String getKey( Object key )
		{
			if ( key instanceof Therm )
			{
				Therm therm = (Therm) key;
				if ( !therm.is( "variable" ) )
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

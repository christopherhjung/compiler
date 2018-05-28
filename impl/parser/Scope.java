package parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Scope {

	private static final int UNUSED = 0;
	private static final int PRESET = 1;
	private static final int HOLDING = 2;

	private Scope parent;
	private Map<String, Statement> map;
	private Map<String, VarAnchor> variables;
	private Map<Integer, String> register;
	private int[] registerUsage;
	private int varOffset = 0;

	public class VarAnchor {
		public String name;
		public int size;
		public int offset;
		public int register = -1;

		public VarAnchor( String name, int size, int offset )
		{
			this.name = name;
			this.size = size;
			this.offset = offset;
		}
	}

	public Scope()
	{
		this( null );
	}

	public Scope( Scope parentScope )
	{
		this.parent = parentScope;
		this.map = new HashMap<>();
		this.variables = new HashMap<>();
		this.register = new HashMap<>();
		registerUsage = new int[32];
	}

	public void unuseAll()
	{
		for ( int i = 0 ; i <= 25 ; i++ )
		{
			registerUsage[i] = UNUSED;
		}

		override( 25, 26 );
	}

	public int getVarOffset()
	{
		return varOffset;
	}

	public void useRegister( int register, int size )
	{
		for ( int i = 0 ; i < size ; i++ )
		{
			registerUsage[register - i] = HOLDING;
		}
	}

	public void registerVariable( String name, int size )
	{
		VarAnchor entry = new VarAnchor( name, size, varOffset );
		variables.put( name, entry );
		varOffset += size;
	}

	public void storeRegister( int register, String name )
	{
		this.register.put( register, name );
		VarAnchor anchor = variables.get( name );
		anchor.register = register;
	}

	private int getBestRegister( int size )
	{
		for ( int usage = 0 ; usage <= 1 ; usage++ )
		{
			int found = -1;
			int foundSize = 0;
			for ( int i = 25 ; i >= 2 ; )
			{
				if ( registerUsage[i] <= usage && !isRegisterMode( i, HOLDING ) )
				{
					if ( found < 0 )
					{
						found = i;
					}

					foundSize++;
				}
				else
				{
					foundSize = 0;
					found = -1;
				}

				if ( foundSize == size )
				{
					return found;
				}

				i -= 1 + size % 2;
			}
		}

		return -1;
	}

	public void override( int register, int size )
	{
		for ( VarAnchor anchor : variables.values() )
		{
			if ( anchor.register >= 0 && anchor.register + anchor.size > register && anchor.register < register + size )
			{
				for ( int pos = 0 ; pos < anchor.size ; pos++ )
				{
					if ( registerUsage[anchor.register - pos] == PRESET )
					{
						registerUsage[anchor.register - pos] = UNUSED;
					}
				}
				anchor.register = -1;
			}
		}
	}

	public boolean allocRegister( int register, int size )
	{
		for ( int offset = 0 ; offset < size ; offset++ )
		{
			int byteRegister = register - offset;

			if ( registerUsage[byteRegister] == HOLDING )
			{
				return false;
			}
		}

		override( register, size );
		for ( int offset = 0 ; offset < size ; offset++ )
		{
			int byteRegister = register - offset;
			registerUsage[byteRegister] = HOLDING;
		}

		return true;
	}

	public VarAnchor getByRegister( int register )
	{
		for ( VarAnchor anchor : variables.values() )
		{
			if ( anchor.register == register )
			{
				return anchor;
			}
		}
		return null;
	}

	public void freeRegister( int register, int size )
	{
		VarAnchor anchor = getByRegister( register );

		for ( int offset = 0 ; offset < size ; offset++ )
		{
			int byteRegister = register - offset;
			if ( anchor == null )
			{
				registerUsage[byteRegister] = UNUSED;
			}
			else
			{
				registerUsage[byteRegister] = PRESET;
			}
		}
	}

	public boolean isRegisterMode( int register, int mode )
	{
		boolean contains = registerUsage[register] == mode;

		if ( !contains && parent != null )
		{
			contains = parent.isRegisterMode( register, mode );
		}

		return contains;
	}

	public int allocRegister( int size )
	{
		int register = getBestRegister( size );

		if ( register >= 0 )
		{
			allocRegister( register, size );
		}

		return register;
	}

	public VarAnchor getVarAnchor( String name )
	{
		VarAnchor varEntry = variables.get( name );

		if ( varEntry == null && parent != null )
		{
			varEntry = parent.getVarAnchor( name );
		}

		return varEntry;
	}

	public void setParentScope( Scope parentScope )
	{
		this.parent = parentScope;
	}

	public Scope getParentScope()
	{
		return parent;
	}

	public Statement get( Object key )
	{
		if ( map.containsKey( key ) )
		{
			return map.get( key );
		}
		else if ( parent != null )
		{
			return parent.get( key );
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
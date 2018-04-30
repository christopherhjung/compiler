package therms;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import parser.ThermStringifier;
import tools.Utils;

public class Chain extends Therm {

	private final Therm outer;
	private final Therm[] inner;

	public Chain( Therm outer, Therm... inner )
	{
		this.outer = outer;
		this.inner = inner;
	}

	@Override
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "derivate" ) )
		{
			StringBuilder sb = new StringBuilder();
			sb.append( inner[0].execute( "derivate", params ) );
			sb.append( "*" );
			sb.append( outer.execute( "derivate" ) );
			sb.append( '(' );
			sb.append( inner[0] );
			sb.append( ')' );
		}

		return super.execute( key, params );
	}

	@Override
	public Therm derivate( Variable name )
	{
		return inner[0].derivate( name ).mul( new Chain( outer.derivate( null ), inner[0] ) );
	}

	@Override
	public Therm reduce( VarSet varSet, Therm... therms )
	{
		Therm[] newInner = new Therm[inner.length];

		for ( int i = 0 ; i < inner.length ; i++ )
		{
			newInner[i] = inner[i].reduce( varSet );
		}

		return outer.reduce( varSet, newInner );
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( super.equals( obj ) ) return true;
		if ( !(obj instanceof Chain) ) return false;

		Chain other = (Chain) obj;
		return inner.equals( other.inner ) && outer.equals( other.outer );
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		outer.toString( builder );
		builder.append( inner, "," );
	}

	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}
}

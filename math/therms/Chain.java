package therms;

import parser.ThermStringifier;

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
		else if ( key.equals( "reduce" ) )
		{
			Object[] reducedParams = new Object[inner.length];
			for ( int i = 0 ; i < inner.length ; i++ )
			{
				reducedParams[i] = inner[i].execute( "reduce" );
			}
			return outer.execute( key, reducedParams );
		}

		return super.execute( key, params );
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

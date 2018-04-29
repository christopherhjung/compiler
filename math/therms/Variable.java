package therms;

import parser.ThermStringifier;

public class Variable extends Therm {

	public final static Variable X = new Variable( "x" );

	private final String name;

	public Variable( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public Object execute( String key, Object... params )
	{
		if ( key.equals( "derivate" ) )
		{
			if ( params.length == 1 && params[0] instanceof Therm )
			{
				Therm derivateFor = (Therm) params[0];

				if ( derivateFor.execute( "type" ).equals( "variable" ) )
				{
					String name = derivateFor.get( "value", String.class );

					if ( this.name.equals( name ) )
					{
						return "1";
					}
					else
					{
						return "0";
					}
				}
			}
		}
		else if ( key.equals( "value" ) )
		{
			return name;
		}
		else if ( key.equals( "type" ) )
		{
			return "variable";
		}

		return super.execute( key, params );
	}

	@Override
	public Therm reduce( VarSet varSet, Therm... therms )
	{
		Therm value = varSet.getValue( this );
		return value != null ? value : this;
	}

	@Override
	public Therm derivate( Variable name )
	{
		if ( equals( name ) ) return Const.ONE;
		return Const.ZERO;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( super.equals( obj ) ) return true;
		if ( obj instanceof Variable ) return name.equals( ((Variable) obj).name );
		if ( obj instanceof String ) return name.equals( obj );

		return false;
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( name );
	}

	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
}

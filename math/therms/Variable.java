package therms;

import parser.ThermStringify;

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
	public double valueAt( VarSet varSet )
	{
		return varSet.getValue( this );
	}

	@Override
	public Therm derivate( Variable name )
	{
		if ( equals( name ) ) return Const.ONE;
		return Const.ZERO;
	}

	@Override
	public boolean contains( Therm var )
	{
		return var.equals( name );
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
	public void toString( ThermStringify builder )
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

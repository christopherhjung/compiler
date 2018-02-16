package functions;

import therms.Chain;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public abstract class Function extends Therm{
	/*
	private Therm inner;
	
	public Function( String name, Therm inner )
	{
		this.inner = inner;
	}*/
	
	@Override
	public double valueAt( VarSet varSet )
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	/*
	@Override
	public Therm derivate( Variable name )
	{
		return functionDerivate( inner ).mul( inner.derivate( name ) );
	}*/
	
	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		return replacer.equals( Variable.X ) ? new Chain( this, replacement ) : this;
	}
	
	@Override
	public boolean equals( Object obj )
	{
		return this.toString().equals( obj.toString() );
	}
}

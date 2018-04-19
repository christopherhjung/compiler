package functions;

import parser.MathEngine;
import therms.Chain;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public abstract class EnginePlugin extends Therm {

	public void enable( MathEngine engine )
	{

	}

	@EngineExecute
	public Therm execute( Therm var )
	{
		return new Chain(this, var);
	}	

	@Override
	public double valueAt( VarSet varSet )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * @Override public Therm derivate( Variable name ) { return
	 * functionDerivate( inner ).mul( inner.derivate( name ) ); }
	 */

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

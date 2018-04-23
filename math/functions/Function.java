package functions;

import parser.ThermStringifier;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Function extends Therm {

	private Therm[] inner;

	protected Function( Therm... inner )
	{
		this.inner = inner;
	}

	@Override
	public Therm derivate( Variable name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public double reduce( VarSet varSet )
	{
		// TODO Auto-generated method stub
		return 0;
	}

}

package therms;

import java.util.HashMap;
import java.util.Map;

public class VarSet {
	private final Map<Variable, Double> valueSet;

	public VarSet( Variable var, double value )
	{
		valueSet = new HashMap<>();
		valueSet.put( var, value );
	}

	public VarSet( Map<Variable, Double> valueSet )
	{
		this.valueSet = new HashMap<>( valueSet );
	}

	public double getValue( Variable var )
	{
		Double value = valueSet.get( var );
		if ( value == null ) throw new ArithmeticException( var + " not found" );
		return value;
	}

	public VarSet extend( Variable key, double value )
	{
		Map<Variable, Double> newValueSet = new HashMap<>();
		newValueSet.put( key, value );
		return new VarSet( newValueSet );
	}
}

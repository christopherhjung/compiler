package therms;

import java.util.HashMap;
import java.util.Map;

public class VarSet {
	private final Map<Variable, Therm> valueSet;

	public VarSet()
	{
		valueSet = new HashMap<>();
	}
	
	public VarSet( Variable var, Therm value )
	{
		valueSet = new HashMap<>();
		valueSet.put( var, value );
	}

	public VarSet( Map<Variable, Therm> valueSet )
	{
		this.valueSet = new HashMap<>( valueSet );
	}

	public Therm getValue( Variable var )
	{
		Therm value = valueSet.get( var );
		return value;
	}
}

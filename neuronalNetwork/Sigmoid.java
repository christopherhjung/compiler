
public class Sigmoid implements ActivationFunction{

	@Override
	public double eval( double value )
	{
		return 1 / (1 + Math.exp( -value ));
	}

	@Override
	public double derivate( double value )
	{
		return value * (1 - value);
	}

}

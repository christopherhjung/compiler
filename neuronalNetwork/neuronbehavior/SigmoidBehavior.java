package neuronbehavior;
public class SigmoidBehavior implements ActivationBehavior{

	@Override
	public double calcActivation( double value )
	{
		return 1 / (1 + Math.exp( -value ));
	}

	@Override
	public double calcBackwardDeriavte( double value )
	{
		return value * (1 - value);
	}

}

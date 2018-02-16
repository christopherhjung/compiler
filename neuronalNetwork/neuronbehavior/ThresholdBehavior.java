package neuronbehavior;

public class ThresholdBehavior implements NeuronBehavior {

	ThresholdBehavior()
	{}

	@Override
	public double computeActivation( double value )
	{
		return value > 0 ? 1 : 0;
	}

	@Override
	public double computeDerivative( double value )
	{
		return 0;
	}
}

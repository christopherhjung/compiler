package neuronbehavior;
public class SigmoidBehavior implements NeuronBehavior{

	@Override
	public double calcActivation( double value )
	{
		return 1 / (1 + Math.exp( -value ));
	}

	@Override
	public double calcDerivateFromOutput( double value )
	{
		return value * (1 - value);
	}

}

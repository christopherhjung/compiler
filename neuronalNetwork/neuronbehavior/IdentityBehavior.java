package neuronbehavior;

public class IdentityBehavior implements NeuronBehavior{

	IdentityBehavior(){}
	
	@Override
	public double computeActivation(double value) {
		return value;
	}

	@Override
	public double computeDerivative(double value) {
		return 1;
	}
}

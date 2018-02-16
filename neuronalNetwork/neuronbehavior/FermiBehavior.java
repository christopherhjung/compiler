package neuronbehavior;

public class FermiBehavior implements NeuronBehavior{

	FermiBehavior(){}
	
	@Override
	public double computeActivation(double value) {
		return 1/(1+Math.exp(-value));
	}

	@Override
	public double computeDerivative(double value) {
		double e = Math.exp(value);
		return e++/e/e;
	}
}

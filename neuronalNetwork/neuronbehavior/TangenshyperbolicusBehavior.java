package neuronbehavior;

public class TangenshyperbolicusBehavior implements NeuronBehavior{

	TangenshyperbolicusBehavior(){}
	
	@Override
	public double computeActivation(double value) {
		return (1+Math.tanh(value))/2;
	}

	@Override
	public double computeDerivative(double value) {
		double d = Math.tanh(value);
        return 1.0 - d * d;
	}
}

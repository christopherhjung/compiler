package neuronbehavior;

public interface NeuronBehavior {

	double calcActivation( double value );
	double calcDerivateFromOutput( double value );
	
}

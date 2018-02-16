package neuronbehavior;

public enum NeuronBehaviors implements NeuronBehavior{
	FERMI(new FermiBehavior()),
	IDENTITY(new IdentityBehavior()),
	THRESHOLD(new ThresholdBehavior()),
	TANGENSHYPERBOLICUS(new TangenshyperbolicusBehavior());
	
	private NeuronBehavior behavior;
	
	NeuronBehaviors( NeuronBehavior behavior ){
		this.behavior = behavior;
	}
	
	@Override
	public double computeActivation(double value) {
		return this.behavior.computeActivation(value);
	}

	@Override
	public double computeDerivative(double value) {
		return this.behavior.computeDerivative(value);
	}
}

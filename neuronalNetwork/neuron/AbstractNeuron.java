package neuron;

public class AbstractNeuron {	
	private double activationLevel;
		
	public double getActivationLevel(){
		return activationLevel;
	}
	
	protected void setActivationLevel( double activationLevel ){
		this.activationLevel = activationLevel;
	}	
}

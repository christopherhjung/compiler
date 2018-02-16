/*
 * Decompiled with CFR 0_118.
 */
package neuronbehavior;

public interface NeuronBehavior{
	
    public double computeActivation(double value);

    public double computeDerivative(double value);

    /*
	public NeuronBehavior copy(){
		try {
			return (NeuronBehavior)this.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	};*/
    
    
}


package neuron;

public class Weight {

	private double value = 1;
	// private double previous = 1;

	public void setValue(double newValue)
	{
		// previous = newValue;
		value = newValue;
	}

	public double getValue()
	{
		return value;
	}

}

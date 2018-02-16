package neuron;

import java.util.Arrays;

import neuronbehavior.NeuronBehavior;

public class NeuralLayer {
	private final Neuron[] neurons;

	public NeuralLayer( int size, NeuronBehavior behavior )
	{
		this( size, behavior, null );
	}

	public NeuralLayer( int size, NeuronBehavior behavior, NeuralLayer parentLayer )
	{
		neurons = new Neuron[size];
		for ( int i = 0 ; i < size ; i++ )
		{
			neurons[i] = new Neuron( parentLayer, behavior );
		}
	}

	public int getSize()
	{
		return neurons.length;
	}

	public Neuron[] getNeurons()
	{
		return neurons;
	}

	public Neuron getNeuron( int pos )
	{
		return neurons[pos];
	}

	public double[] getActivationLevels()
	{
		double[] activations = new double[getSize()];
		for ( int i = 0 ; i < getSize() ; i++ )
		{
			activations[i] = neurons[i].getActivationLevel();
		}
		return activations;
	}

	public void propagate()
	{
		for ( Neuron neuron : neurons )
		{
			neuron.propagate();
		}
	}

	@Override
	public String toString()
	{
		return Arrays.toString( this.neurons );
	}
}

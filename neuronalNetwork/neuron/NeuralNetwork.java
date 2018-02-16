package neuron;

import java.util.Arrays;

import neuronbehavior.NeuronBehaviors;

public class NeuralNetwork {
	private final NeuralLayer[] layers;

	public NeuralNetwork( int... layerSizes )
	{
		layers = new NeuralLayer[layerSizes.length];
		for ( int i = 0 ; i < layerSizes.length ; i++ )
		{
			if ( i == 0 )
			{
				// Input Layer
				layers[i] = new NeuralLayer( layerSizes[i], NeuronBehaviors.IDENTITY );
			}
			else
			{
				// Hidden or Output layer
				layers[i] = new NeuralLayer( layerSizes[i], NeuronBehaviors.FERMI, layers[i - 1] );
			}
		}
	}
	
	public NeuralNetwork( double[][] network, double[][][] weights )
	{
		layers = new NeuralLayer[layerSizes.length];
		for ( int i = 0 ; i < layerSizes.length ; i++ )
		{
			if ( i == 0 )
			{
				// Input Layer
				layers[i] = new NeuralLayer( ne, NeuronBehaviors.IDENTITY );
			}
			else
			{
				// Hidden or Output layer
				layers[i] = new NeuralLayer( layerSizes[i], NeuronBehaviors.FERMI, layers[i - 1] );
			}
		}
	} 

	public int size()
	{
		return layers.length;
	}

	public NeuralLayer getLayer( int layer )
	{
		return layers[layer];
	}

	/*
	 * public NeuralLayer getHiddenLayer( int layer ){ if(layer > getSize() -
	 * 3){ throw new IllegalArgumentException("Hidden Layer with index " + layer
	 * + " not found"); } return layers[layer+1]; }
	 */

	public NeuralLayer getInputLayer()
	{
		return layers[0];
	}

	public NeuralLayer getOutputLayer()
	{
		return layers[size() - 1];
	}

	public void propagate()
	{
		for ( int i = 1 ; i < size() ; i++ )
		{
			layers[i].propagate();
		}
	}

	public double[] propagate( double[] data )
	{
		int p = 0;
		for ( Neuron neuron : getInputLayer().getNeurons() )
		{
			neuron.setActivationLevel( data[p++] );
		}
		this.propagate();
		return this.getOutputLayer().getActivationLevels();
	}

	@Override
	public String toString()
	{
		return Arrays.toString( this.layers );
	}
}

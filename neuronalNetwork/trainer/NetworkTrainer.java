package trainer;

import java.util.Map;
import java.util.PriorityQueue;

import neuron.NeuralLayer;
import neuron.NeuralNetwork;
import neuron.Neuron;
import neuron.Weight;

public class NetworkTrainer {
	private final NeuralNetwork network;

	private static final int INPUT_LAYER = 0;
	private static final int BEGIN_HIDDEN_LAYERS = 1;

	private double[][] layerOutput;
	private double[][] layerInput;
	private double[][] delta;
	private double[][] previousBiasDelta;
	private double[][][] previousWeightDelta;

	public NetworkTrainer( NeuralNetwork network )
	{
		this.network = network;
	}

	public void train( TrainingLesson lession )
	{
		init();
		trainNetwork( lession );
	}

	private void init()
	{
		int layerCount = this.network.getSize();
		// Start dimensioning arrays
		previousBiasDelta = new double[layerCount][];
		delta = new double[layerCount][];
		layerOutput = new double[layerCount][];
		layerInput = new double[layerCount][];
		previousWeightDelta = new double[layerCount][][];

		for ( int l = 1 ; l < layerCount ; l++ )
		{
			PriorityQueue<String> s;
			NeuralLayer currentLayer = this.network.getLayer( l );
			NeuralLayer previousLayer = this.network.getLayer( l - 1 );
			int currentLayerSize = currentLayer.getSize();
			int previousLayerSize = previousLayer.getSize();

			previousBiasDelta[l] = new double[currentLayerSize];
			delta[l] = new double[currentLayerSize];
			layerOutput[l] = new double[currentLayerSize];
			layerInput[l] = new double[currentLayerSize];
			previousWeightDelta[l] = new double[currentLayerSize][];

			for ( int j = 0 ; j < currentLayerSize ; j++ )
			{
				Neuron neuron = currentLayer.getNeuron( j );

				neuron.setThreshold( Gaussian.getRandomGaussian() );
				previousBiasDelta[l][j] = 0.0;
				layerOutput[l][j] = 0.0;
				layerInput[l][j] = 0.0;
				delta[l][j] = 0.0;

				if ( currentLayer != this.network.getInputLayer() )
				{
					Map<Neuron, Weight> parents = neuron.getParents();
					previousWeightDelta[l][j] = new double[parents.size()];
					for ( Map.Entry<Neuron, Weight> entry : parents.entrySet() )
					{
						entry.getValue().setValue( Gaussian.getRandomGaussian() );
					}
				}
			}
		}
	}

	private void trainNetwork( TrainingLesson lession )
	{
		double[][] inputSamples = lession.getInputs();
		double[][] target = lession.getDesiredOutputs();

		int layerIndex = network.size() - 1;
		for ( int targetIndex = 0 ; targetIndex < target.length ; targetIndex++ )
		{
			double[] propagatedOutput = network.propagate( inputSamples[targetIndex] );

			
		}
		
		for ( int index = 0 ; index < propagatedOutput.length ; index++ )
		{
			
			double first = propagatedOutput[index] - target[targetIndex][index];
			network.getLayer( layerIndex )
		}

		/**
		 * int layerCount = this.network.getSize();
		 * 
		 * // Local variable double error = 0.0, sum = 0.0, weightDelta = 0.0,
		 * biasDelta = 0.0;
		 * 
		 * // Run the network double[] output = this.network.propagate(input);
		 * 
		 * // Back-propagate the error NeuralLayer previousLayer; for (int l =
		 * layerCount - 1; l > 0; l--) { NeuralLayer currentLayer =
		 * this.network.getLayer(l); int currentLayerSize =
		 * currentLayer.getSize();
		 * 
		 * // Output layer if (l == layerCount - 1) { for (int k = 0; k <
		 * currentLayerSize; k++) { delta[l][k] = output[k] - desired[k]; error
		 * += delta[l][k] * delta[l][k]; delta[l][k] *=
		 * currentLayer.getNeuron(k).getBehavior().computeDerivative(layerInput[l][k]);
		 * } } else // Hidden layer { int previousLayerSize =
		 * previousLayer.getSize();
		 * 
		 * for (int i = 0; i < currentLayerSize; i++) { sum = 0.0; for (int j =
		 * 0; j < previousLayerSize; j++) { sum +=
		 * previousLayer.getNeuron(i).getParents().get(key) sum += weight[l +
		 * 1][i][j] * delta[l + 1][j]; } sum *=
		 * currentLayer.getNeuron(k).getBehavior().computeDerivative(layerInput[l][k]);
		 * 
		 * delta[l][i] = sum; } } previousLayer = currentLayer; } /* // Update
		 * the weights and biases for (int l = 0; l < layerCount; l++) for(int
		 * i=0; i<(l==0 ? inputSize : layerSize[l-1]); i++) for (int j = 0; j <
		 * layerSize[l]; j++) { weightDelta = TrainingRate * delta[l][j] * (l ==
		 * 0 ? input[i] : layerOutput[l - 1][i]) + Momentum *
		 * previousWeightDelta[l][i][j]; weight[l][i][j] -= weightDelta;
		 * 
		 * previousWeightDelta[l][i][j] = weightDelta; }
		 * 
		 * for(int l=0; l<layerCount; l++) for (int i = 0; i < layerSize[l];
		 * i++) { biasDelta = TrainingRate * delta[l][i]; bias[l][i] -=
		 * biasDelta + Momentum * previousBiasDelta[l][i];
		 * 
		 * previousBiasDelta[l][i] = biasDelta; }
		 * 
		 * return error;
		 */
	}
}

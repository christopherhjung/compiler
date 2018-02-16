package trainer;

import matrix.Matrix;
import network.Network;
import neuronbehavior.NeuronBehavior;

public class NetworkTrainer {

	public static TrainResult train( Network network, TrainingLesson lession, double targetError )
	{
		double lernRate = lession.getLearningRate();
		Matrix[] weights = network.getWeights();
		Matrix bias = network.getBias();

		int iteration = 0;
		double error = 1;
		Matrix[] derivates = new Matrix[weights.length];
		double[] biasDerivatives = new double[weights.length];

		for ( ;; iteration++ )
		{
			error = ErrorMeasurement.squared( lession.getTargets(), network.propagate( lession.getSamples() ) );

			if ( error <= targetError || iteration >= lession.maxLernSteps() ) break;

			for ( int sampleIndex = 0 ; sampleIndex < lession.size() ; sampleIndex++ )
			{
				double[][] singleOut = network.propagateAll( lession.getSampel( sampleIndex ) );
				double[] previousGradients = null;
				for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
				{
					final double[] nextGradients = new double[weights[currentLayer].cols()];
					
					NeuronBehavior behavior = network.getNeuronBehaviour( currentLayer );
					
					if ( sampleIndex == 0 )
					{
						biasDerivatives[currentLayer] = 0;
					}
					
					for ( int neuronIndex = 0 ; neuronIndex < nextGradients.length ; neuronIndex++ )
					{
						double factor = 0;

						if ( currentLayer == weights.length - 1 )
						{
							factor = -(lession.getTarget( sampleIndex )[neuronIndex]
									- singleOut[singleOut.length - 1][neuronIndex]);
						}
						else
						{
							for ( int nextNeuronIndex = 0 ; nextNeuronIndex < previousGradients.length ; nextNeuronIndex++ )
							{
								factor += previousGradients[nextNeuronIndex]
										* weights[currentLayer + 1].get( neuronIndex, nextNeuronIndex );
							}
						}

						nextGradients[neuronIndex] = factor
								* behavior.calcDerivateFromOutput( singleOut[currentLayer + 1][neuronIndex] );

						biasDerivatives[currentLayer] += nextGradients[neuronIndex];
					}

					previousGradients = nextGradients;

					if ( derivates[currentLayer] == null )
					{
						derivates[currentLayer] = Matrix.ofSize( singleOut[currentLayer].length, nextGradients.length );
					}
					else if ( sampleIndex == 0 )
					{
						derivates[currentLayer].zero();
					}

					double[] layerOutput = singleOut[currentLayer];
					derivates[currentLayer].inc( ( u, v ) -> nextGradients[v] * layerOutput[u] );
				}
			}

			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				Matrix layerMatrix = weights[layer];
				derivates[layer].forEach( ( u, v, value ) -> layerMatrix.dec( u, v, lernRate * value ) );
				bias.dec( layer,0, lernRate * biasDerivatives[layer] );
			}
		}

		return new TrainResult( error, iteration );
	}
	
	
}

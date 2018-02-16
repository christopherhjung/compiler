import matrix.MapMatrix;
import matrix.Matrix;
import matrix.MultiplyMatrix;
import matrix.NumberMatrix;
import neuronbehavior.ActivationBehavior;
import neuronbehavior.SigmoidBehavior;
import trainer.ErrorMeasurement;
import trainer.TrainResult;
import trainer.TrainingLesson;

public class Network {

	private Matrix[] weights;
	private double[] bias;
	private ActivationBehavior neuronBehaviour = new SigmoidBehavior();

	public Network( int... layerSizes )
	{
		weights = new Matrix[layerSizes.length - 1];

		for ( int i = 0 ; i < layerSizes.length - 1 ; i++ )
		{
			weights[i] = Matrix.ofSize( layerSizes[i], layerSizes[i + 1] );
			weights[i].map( ( row, col ) -> Math.random() );
		}

		bias = new double[layerSizes.length - 1];
	}

	public Matrix propagate( Matrix input )
	{
		Matrix result = Matrix.copy( input );

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			Matrix layer = weights[layerIndex];
			double bia = bias[layerIndex];

			result = new MultiplyMatrix( result, layer );
			result = new MapMatrix( result, val -> neuronBehaviour.calcActivation( val + bia ) );
		}

		return result;
	}

	public double[] propagate( double... input )
	{
		double[] tempResults = input;

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			Matrix layer = weights[layerIndex];
			double[] nextTempResult = new double[layer.colSize()];

			for ( int v = 0 ; v < layer.colSize() ; v++ )
			{
				for ( int u = 0 ; u < layer.rowSize() ; u++ )
				{
					nextTempResult[v] += layer.get( u, v ) * tempResults[u];
				}

				nextTempResult[v] = neuronBehaviour.calcActivation( nextTempResult[v] + bias[layerIndex] );
			}

			tempResults = nextTempResult;
		}

		return tempResults;
	}

	public double[][] propagate( double[][] input )
	{
		double[][] result = new double[input.length][];
		for ( int i = 0 ; i < input.length ; i++ )
		{
			result[i] = propagate( input[i] );
		}

		return result;
	}

	public double[][] propagateAll( double[] input )
	{
		double[][] result = new double[weights.length + 1][];
		result[0] = input;

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			Matrix layer = weights[layerIndex];
			double[] nextTempResult = new double[layer.colSize()];

			for ( int v = 0 ; v < layer.colSize() ; v++ )
			{
				for ( int u = 0 ; u < layer.rowSize() ; u++ )
				{

					nextTempResult[v] += layer.get( u, v ) * result[layerIndex][u];
				}

				nextTempResult[v] = neuronBehaviour.calcActivation( nextTempResult[v] + bias[layerIndex] );
			}

			result[layerIndex + 1] = nextTempResult;
		}

		return result;
	}

	public TrainResult train( TrainingLesson lession, double targetError )
	{
		double lernRate = lession.getLearningRate();

		int iteration = 0;
		double error = 1;
		Matrix[] derivates = new Matrix[weights.length];
		double[] biasDerivatives = new double[weights.length];

		for ( ;; iteration++ )
		{
			error = ErrorMeasurement.squared( lession.getTargets(), propagate( lession.getSamples() ) );

			if ( error <= targetError || iteration >= lession.maxLernSteps() ) break;

			for ( int sampleIndex = 0 ; sampleIndex < lession.size() ; sampleIndex++ )
			{
				double[][] singleOut = propagateAll( lession.getSampel( sampleIndex ) );
				double[] previousGradients = null;
				for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
				{
					final double[] nextGradients = new double[weights[currentLayer].colSize()];

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
								* neuronBehaviour.calcBackwardDeriavte( singleOut[currentLayer + 1][neuronIndex] );

						if ( sampleIndex == 0 && neuronIndex == 0 )
						{
							biasDerivatives[currentLayer] = nextGradients[neuronIndex];
						}
						else
						{
							biasDerivatives[currentLayer] += nextGradients[neuronIndex];
						}
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
				bias[layer] -= lernRate * biasDerivatives[layer];
			}
		}

		return new TrainResult( error, iteration );
	}
}

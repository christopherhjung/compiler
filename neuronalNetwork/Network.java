import matrix.MapMatrix;
import matrix.Matrix;
import matrix.MultiplyMatrix;
import matrix.NumberMatrix;

public class Network {

	private Matrix[] weights;
	private double[] bias;
	private ActivationFunction activationFunction = new Sigmoid();
	
	public Network( int ... layerSizes )
	{
		weights = new Matrix[layerSizes.length - 1];

		for ( int i = 0 ; i < layerSizes.length - 1 ; i++ )
		{
			weights[i] = new NumberMatrix(layerSizes[i],layerSizes[i + 1]);
			weights[i].map( (row,col) -> Math.random() );
		}

		bias = new double[layerSizes.length - 1];
	}
	
	public Matrix propagate( Matrix input )
	{
		Matrix result = new NumberMatrix(input);

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			Matrix layer = weights[layerIndex];
			double bia = bias[layerIndex];
			
			result = new MultiplyMatrix( result, layer );
			result = new MapMatrix( result, val -> activationFunction.eval( val + bia ) );
		}

		return result;
	}
	
	public double[] propagate( double ... input )
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

				nextTempResult[v] = activationFunction.eval( nextTempResult[v] + bias[layerIndex] );
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

					nextTempResult[v] += layer.get(u,v) * result[layerIndex][u];
				}

				nextTempResult[v] = activationFunction.eval( nextTempResult[v] + bias[layerIndex] );
			}

			result[layerIndex + 1] = nextTempResult;
		}

		return result;
	}
	
	public void train( double[][] samples, double[][] targets, double lernRate, double targetError )
	{		
		int iteration = 0;
		Matrix[] derivates = new Matrix[weights.length];
		double[] biasDerivatives = new double[weights.length];
		
		for ( ;; iteration++ )
		{
			if ( error( targets, propagate( samples ) ) <= targetError || iteration >= 5000000 ) break;

			for ( int sampleIndex = 0 ; sampleIndex < samples.length ; sampleIndex++ )
			{
				double[][] singleOut = propagateAll( samples[sampleIndex] );
				double[] previousGradients = null;
				for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
				{
					final double[] nextGradients = new double[weights[currentLayer].colSize()];

					for ( int neuronIndex = 0 ; neuronIndex < nextGradients.length ; neuronIndex++ )
					{
						double factor = 0;

						if ( currentLayer == weights.length - 1 )
						{
							factor = -(targets[sampleIndex][neuronIndex]
									- singleOut[singleOut.length - 1][neuronIndex]);
						}
						else
						{
							for ( int nextNeuronIndex = 0 ; nextNeuronIndex < previousGradients.length ; nextNeuronIndex++ )
							{
								factor += previousGradients[nextNeuronIndex]
										* weights[currentLayer + 1].get( neuronIndex, nextNeuronIndex);
							}
						}

						nextGradients[neuronIndex] = factor
								* activationFunction.derivate( singleOut[currentLayer + 1][neuronIndex] );
						

						if(sampleIndex == 0 && neuronIndex == 0)
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
						derivates[currentLayer] = new NumberMatrix(singleOut[currentLayer].length,nextGradients.length);
					}
					else if( sampleIndex == 0 )
					{
						derivates[currentLayer].zero();
					}

					double[] layerOutput = singleOut[currentLayer];
					derivates[currentLayer].inc( (u,v) -> nextGradients[v] * layerOutput[u] );
				}
			}

			
			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				Matrix layerMatrix = weights[layer];
				derivates[layer].forEach( (u,v,value) -> layerMatrix.dec( u, v, lernRate * value ) );
				bias[layer] -= lernRate * biasDerivatives[layer];
			}
		}
		System.out.println( "Iterationen: " + iteration );
	}
	
	public static double error( double[][] current, double[][] targets )
	{
		double error = 0;

		for ( int sampleIndex = 0 ; sampleIndex < current.length ; sampleIndex++ )
		{
			for ( int index = 0 ; index < current[sampleIndex].length ; index++ )
			{
				error += 0.5 * (targets[sampleIndex][index] - current[sampleIndex][index])
						* (targets[sampleIndex][index] - current[sampleIndex][index]);
			}
		}

		return error;
	}
	
}


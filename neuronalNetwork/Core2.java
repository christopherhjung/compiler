import java.util.Arrays;

public class Core2 {

	public static double[][] propagateAll( double[] input, double[][][] weights, double[] bias )
	{
		double[][] result = new double[weights.length + 1][];
		result[0] = input;

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			double[][] layer = weights[layerIndex];
			double[] nextTempResult = new double[layer[layerIndex].length];

			for ( int v = 0 ; v < layer[0].length ; v++ )
			{
				for ( int u = 0 ; u < layer.length ; u++ )
				{

					nextTempResult[v] += layer[u][v] * result[layerIndex][u];
				}

				nextTempResult[v] = sgm( nextTempResult[v] + bias[layerIndex] );
			}

			result[layerIndex + 1] = nextTempResult;
		}

		return result;
	}

	public static double[] propagate( double[] input, double[][][] weights, double[] bias )
	{
		double[] tempResults = input;

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			double[][] layer = weights[layerIndex];
			double[] nextTempResult = new double[layer[layerIndex].length];

			for ( int v = 0 ; v < layer[0].length ; v++ )
			{
				for ( int u = 0 ; u < layer.length ; u++ )
				{

					nextTempResult[v] += layer[u][v] * tempResults[u];
				}

				nextTempResult[v] = sgm( nextTempResult[v] + bias[layerIndex] );
			}

			tempResults = nextTempResult;
		}

		return tempResults;
	}

	public static double[][] propagate( double[][] input, double[][][] weights, double[] bias )
	{
		double[][] result = new double[input.length][];
		for ( int i = 0 ; i < input.length ; i++ )
		{
			result[i] = propagate( input[i], weights, bias );
		}

		return result;
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

	public static double dotProduct( double[][][] array )
	{
		double result = 0;
		for ( double[][] a : array )
		{
			for ( double[] b : a )
			{
				for ( double c : b )
				{
					result += c * c;
				}
			}
		}

		return result;
	}

	public static void train( double[][] samples, double[][] targets, double[][][] weights, double[] bias,
			double lernRate, double targetError )
	{

		int iteration = 0;
		double[][][] derivates, lastDerivates = null;
		double lastG = -1;
		while ( true )
		{
			if ( error( targets, propagate( samples, weights, bias ) ) <= targetError || iteration > 5000000 ) break;

			derivates = new double[weights.length][][];

			for ( int sampleIndex = 0 ; sampleIndex < samples.length ; sampleIndex++ )
			{
				double[][] singleOut = propagateAll( samples[sampleIndex], weights, bias );
				// NextWall
				double[] nextGradients = null, previousGradients = null;
				for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
				{
					nextGradients = new double[weights[currentLayer][0].length];

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
										* weights[currentLayer + 1][neuronIndex][nextNeuronIndex];
							}
						}

						nextGradients[neuronIndex] = factor
								* sgmDerivateBackwards( singleOut[currentLayer + 1][neuronIndex] );
					}

					previousGradients = nextGradients;

					if ( derivates[currentLayer] == null )
					{
						derivates[currentLayer] = new double[singleOut[currentLayer].length][nextGradients.length];
					}

					for ( int u = 0 ; u < derivates[currentLayer].length ; u++ )
					{
						for ( int v = 0 ; v < derivates[currentLayer][u].length ; v++ )
						{
							derivates[currentLayer][u][v] += nextGradients[v] * singleOut[currentLayer][u];
						}
					}
				}
			}

			// double currentG = dotProduct( derivates );

			// if ( lastDerivates != null )
			// {
			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				for ( int u = 0 ; u < derivates[layer].length ; u++ )
				{
					for ( int v = 0 ; v < derivates[layer][u].length ; v++ )
					{

						// double beta = currentG / lastG;

						// derivates[layer][u][v] = derivates[layer][u][v] -
						// beta * lastDerivates[layer][u][v];

						weights[layer][u][v] -= lernRate * derivates[layer][u][v];

					}
				}
			}
			// }in

			// lastG = currentG;
			lastDerivates = derivates;

			iteration++;
		}
		System.out.println( "Iterationen: " + iteration );
	}

	public static double sgm( double x )
	{
		return 1 / (1 + Math.exp( -x ));
	}

	public static double sgmDerivateBackwards( double x )
	{
		return x * (1 - x);
	}

	public static double sgmDerivate2( double x )
	{
		double exp = Math.exp( x );
		return exp++ / exp / exp;
	}

	public static double[][][] generateNetwork( int... layerSizes )
	{
		double[][][] network = new double[layerSizes.length - 1][][];

		for ( int i = 0 ; i < layerSizes.length - 1 ; i++ )
		{
			network[i] = new double[layerSizes[i]][layerSizes[i + 1]];

			for ( int u = 0 ; u < network[i].length ; u++ )
			{
				for ( int v = 0 ; v < network[i][u].length ; v++ )
				{
					network[i][u][v] = Math.random();
				}
			}
		}

		return network;
	}
	
	public static void main( String[] args )
	{
		double[][][] network = generateNetwork(2,2,1);
		double[][] samples = new double[][]{{0,0},{1,0},{0,1},{1,1}};
		double[][] targets = new double[][]{{0},{1},{1},{1}};
		double[] bias = new double[]{0,0,0};
		train( samples, targets, network, bias, 0.5, 0.00001 );
		double[][] target = propagate( samples, network, bias );
		System.out.println( Arrays.deepToString( target ) );
		
		
		System.out.println( Arrays.deepToString( network ) );
	}

}

import java.util.Arrays;

import matrix.MultiplyMatrix;
import matrix.IgnoreMatrix;
import matrix.Matrix;
import matrix.NumberMatrix;
import matrix.TransposedMatrix;


public class Core {

	public static double[][] propagateAll( double[] input, Matrix[] weights, double[] bias )
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

				nextTempResult[v] = sgm( nextTempResult[v] + bias[layerIndex] );
			}

			result[layerIndex + 1] = nextTempResult;
		}

		return result;
	}

	public static double[] propagate( double[] input, Matrix[] weights, double[] bias )
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

				nextTempResult[v] = sgm( nextTempResult[v] + bias[layerIndex] );
			}

			tempResults = nextTempResult;
		}

		return tempResults;
	}

	public static double[][] propagate( double[][] input, Matrix[] weights, double[] bias )
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

	public static void train( double[][] samples, double[][] targets, Matrix[] weights, double[] bias,
			double lernRate, double targetError )
	{		
		int iteration = 0;
		Matrix[] derivates = new Matrix[weights.length];
		double[] biasDerivatives = new double[weights.length];
		
		for ( ;; iteration++ )
		{
			if ( error( targets, propagate( samples, weights, bias ) ) <= targetError || iteration >= 5000000 ) break;

			for ( int sampleIndex = 0 ; sampleIndex < samples.length ; sampleIndex++ )
			{
				double[][] singleOut = propagateAll( samples[sampleIndex], weights, bias );
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
								* sgmDerivateBackwards( singleOut[currentLayer + 1][neuronIndex] );
						

						if(sampleIndex == 0 && neuronIndex == 0){
							biasDerivatives[currentLayer] = nextGradients[neuronIndex];
						}else{
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
				
				
				//double layerDerivate = 0;
				//for( int neuron = 0 ; neuron < layerMatrix.rowSize() ; neuron++ ){
				//	layerDerivate += derivates[layer][neuron];
				//}
			}
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

	public static Matrix[] generateNetwork( int... layerSizes )
	{
		Matrix[] network = new Matrix[layerSizes.length - 1];

		for ( int i = 0 ; i < layerSizes.length - 1 ; i++ )
		{
			network[i] = new NumberMatrix(layerSizes[i],layerSizes[i + 1]);
			network[i].map( (row,col) -> Math.random() );
		}

		return network;
	}
	
	public static void main( String[] args )
	{
		Matrix[] network = generateNetwork(4,2,1);
		//double[][] samples = new double[][]{{0,0},{1,0},{0,1},{1,1}};
		//double[][] targets = new double[][]{{0},{1},{1},{0}};
		
		double[][] samples = new double[][]{
			{0,0,0,0},
			{0,0,0,1},
			{0,0,1,0},
			{0,0,1,1},
			{0,1,0,0},
			{0,1,0,1},
			{0,1,1,0},
			{0,1,1,1},
			{1,0,0,0},
			{1,0,0,1},
			{1,0,1,0}};
		double[][] targets = new double[][]{
			{0.0},
			{0.1},
			{0.2},
			{0.3},
			{0.4},
			{0.5},
			{0.6},
			{0.7},
			{0.8},
			{0.9},
			{1.0}};
		double[] bias = new double[]{0};
		train( samples, targets, network, bias, 0.1, 0.0000001 );
		double[][] target = propagate( samples, network, bias );
		System.out.println( Arrays.deepToString( target ) );
		System.out.println( Arrays.deepToString( network ) );
		System.out.println( Arrays.toString( bias ) );
		System.out.println( Arrays.toString( propagate( new double[]{1,1,1,1} , network, bias ) ) );
	}

}

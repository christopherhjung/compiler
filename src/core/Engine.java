package core;

import java.util.Arrays;
import java.util.Scanner;

import neuron.NeuralNetwork;
import parser.MathEngine;
import trainer.ErrorMeasurement;
import trainer.NetworkTrainer;
import trainer.TrainingLesson;

public class Engine {

	public static double[][] propagateDetail( double[] input, double[][][] weights, double[] bias )
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
			double lernRate, double targetError, int maxIterations )
	{

		int iteration = 0;
		double[][][] gradient;//, lastGradient = null;
		while ( true )
		{
			if ( error( targets, propagate( samples, weights, bias ) ) <= targetError || iteration > maxIterations ) break;
				
			gradient = new double[weights.length][][];
			
			/*
			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				if(gradient[layer] != null){
					for ( int u = 0 ; u < gradient[layer].length ; u++ )
					{
						for ( int v = 0 ; v < gradient[layer][u].length ; v++ )
						{
							gradient[layer][u][v] = 0;
						}
					}
				}
			}*/

			for ( int sampleIndex = 0 ; sampleIndex < samples.length ; sampleIndex++ )
			{
				double[][] singleOut = propagateDetail( samples[sampleIndex], weights, bias );
				
				// NextWall
				boolean init = false;
				double[] nextGradients = null, previousGradients = null;
				for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
				{
					nextGradients = new double[weights[currentLayer][0].length];

					for ( int neuronIndex = 0 ; neuronIndex < nextGradients.length ; neuronIndex++ )
					{
						double factor = 0;

						if ( !init )
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

					if ( gradient[currentLayer] == null )
					{
						gradient[currentLayer] = new double[singleOut[currentLayer].length][nextGradients.length];
					}

					for ( int u = 0 ; u < gradient[currentLayer].length ; u++ )
					{
						for ( int v = 0 ; v < gradient[currentLayer][u].length ; v++ )
						{
							gradient[currentLayer][u][v] += singleOut[currentLayer][u] * nextGradients[v];
						}
					}

					init = true;
					previousGradients = nextGradients;
				}
			}

			// double currentG = dotProduct( gradient );

			// if ( lastGradient != null )
			// {
			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				for ( int u = 0 ; u < gradient[layer].length ; u++ )
				{
					for ( int v = 0 ; v < gradient[layer][u].length ; v++ )
					{
						// double beta = currentG / lastG;

						// gradient[layer][u][v] = gradient[layer][u][v] - beta
						// * lastGradient[layer][u][v];

						weights[layer][u][v] -= lernRate * gradient[layer][u][v];
					}
				}
			}
			// }

			// lastG = currentG;
			// lastGradient = gradient;

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
		double[][][] weights = generateNetwork( 2, 2, 1 );

		double[] bias = new double[] {
				0, 0, 0,0,0,0,0,0,0,0,0,0
		};

		double[][] input = {
				{
						0, 0
				}, {
						1, 0
				}, {
						0, 1
				}, {
						1, 1
				}
		};

		double[][] target = {
				{
						0
				}, {
						1
				}, {
						1
				}, {
						0
				}
		};

		System.out.println( "Fehler vor Lernen: " + error( target, propagate( input, weights, bias ) ) );

		long start = System.currentTimeMillis();
		train( input, target, weights, bias, 0.25, 0.0001, 1000000 );
		long end = System.currentTimeMillis();

		System.out.println( end-start );
		
		System.out.println( "Fehler nach Lernen: " + error( target, propagate( input, weights, bias ) ) );
		System.out.println( "Gewichte nach Lernen" + Arrays.deepToString( weights ) );
		System.out.println( Arrays.deepToString( propagate( input, weights, bias ) ) );

		MathEngine engine = new MathEngine();

		Scanner scanner = new Scanner( System.in );
		while ( true )
		{
			System.out.print( "Query:" );
			String therm = scanner.nextLine();

			if ( therm.equals( "quit" ) )
			{
				scanner.close();
				break;
			}

			try
			{
				System.out.println( "Result:" + engine.eval( therm ).simplify() );
			}
			catch ( Throwable t )
			{
				System.out.println( t.getMessage() );
			}
		}
	}

}

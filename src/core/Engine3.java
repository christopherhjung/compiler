package core;

import java.util.Arrays;
import java.util.Scanner;

import parser.MathEngine;

public class Engine3 {

	public static double[][] propagateAll( double[] input, double[][][] weights, double[] bias )
	{
		double[][] result = new double[weights.length + 1][];
		result[0] = input;

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			double[][] layer = weights[layerIndex];
			double[] nextTempResult = new double[layer[0].length];

			for ( int v = 0 ; v < layer[0].length ; v++ )
			{
				for ( int u = 0 ; u < layer.length ; u++ )
				{

					nextTempResult[v] += 
							layer[u][v] * 
							result[layerIndex][u];
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
			double[] nextTempResult = new double[layer[0].length];

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

	public static void train( double[][] samples, double[][] targets, double[][][] weights, double[] bias )
	{

		double[][][] derivates = new double[weights.length][][];

		double[][] current = propagate( samples, weights, bias );

		double[][] singleOut = propagateAll( samples[0], weights, bias );

		double currentError = error( current, targets );

		for ( int sampleIndex = 0 ; sampleIndex < samples.length ; sampleIndex++ )
		{

			// NextWall
			boolean init = false;
			double[][] nextMatrix = null, previousMatrix = null;
			for ( int currentLayer = weights.length - 1 ; currentLayer >= 0 ; currentLayer-- )
			{
				nextMatrix = new double[weights[currentLayer].length][weights[currentLayer][0].length];

				for ( int neuronIndex = 0 ; neuronIndex < nextMatrix[0].length ; neuronIndex++ )
				{
					double factor = 0;

					if ( !init )
					{
						factor = -(targets[sampleIndex][neuronIndex] - current[sampleIndex][neuronIndex]);
					}
					else
					{
						for ( int nextNeuronIndex = 0 ; nextNeuronIndex < previousMatrix[0].length ; nextNeuronIndex++ )
						{
							factor += previousMatrix[neuronIndex][nextNeuronIndex]
									/ singleOut[currentLayer + 1][neuronIndex]
									* weights[currentLayer + 1][neuronIndex][nextNeuronIndex];
						}
					}

					for ( int prevNeuronIndex = 0 ; prevNeuronIndex < nextMatrix.length ; prevNeuronIndex++ )
					{
						nextMatrix[prevNeuronIndex][neuronIndex] = factor
								* singleOut[currentLayer][prevNeuronIndex]
								* sgmDerivateBackwards( singleOut[currentLayer + 1][neuronIndex] );
					}
				}

				init = true;
				previousMatrix = nextMatrix;

				if ( derivates[currentLayer] == null )
				{
					derivates[currentLayer] = nextMatrix;
				}
				else
				{
					for ( int u = 0 ; u < derivates[currentLayer].length ; u++ )
					{
						for ( int v = 0 ; v < derivates[currentLayer][u].length ; v++ )
						{
							derivates[currentLayer][u][v] += nextMatrix[u][v];
						}
					}
				}
			}

			// System.out.println( Arrays.deepToString( derivates ) );

			double lernRate = 0.25;

			for ( int layer = 0 ; layer < weights.length ; layer++ )
			{
				double[][] currentDirection = derivates[layer];

				for ( int u = 0 ; u < currentDirection.length ; u++ )
				{
					for ( int v = 0 ; v < currentDirection[u].length ; v++ )
					{
						weights[layer][u][v] -= lernRate * currentDirection[u][v];
					}
				}
			}

		}

		// dE/doutlast
		//

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

	public static void main( String[] args )
	{
		// double[][][] weights = new double[][][] { { { 0.15, 0.2,0.2 }, {
		// 0.25, 0.3,0.2 } }, { { 0.4, 0.45 }, { 0.5, 0.55 }, { 0.5, 0.55 } } };
		double[][][] weights = new double[][][] { { { 0.15, 0.25,0.2,0.3,0.2 }, { 0.2, 0.3,0.2,0.3,0.2 } }, { { 0.4, 0.5 }, { 0.45, 0.55 }, { 0.45, 0.55 }, { 0.45, 0.55 }, { 0.45, 0.55 } } };
		// double[][][] weights = new double[][][] { { { 0.1497, 0.199 }, {
		// 0.2497, 0.2999 } }, { { 0.3589, 0.408666 }, { 0.51130, 0.56137 } } };
		double[] bias = new double[] { 0.35, 0.6 };
		double[][] input = {{ 0.05, 0.1 }};//,{0.5,0.5} };
		double[][] target = {{ 0.01, 0.99 }};//,{0.5,0.5}};

		// System.out.println( Arrays.toString( propagate( input, weights, bias
		// ) ) );
		System.out.println( "ssss" + Arrays.deepToString( propagate( input, weights, bias ) ) );
		System.out.println( "Fehler vor Lernen: "
				+ error( target, propagate( input, weights, bias ) ) );

		for ( int i = 0 ; i < 3000000 ; i++ )
		{
			train( input, target, weights, bias );
		}

		System.out.println( "Fehler nach Lernen: " + error( target, propagate( input, weights, bias ) ) );
		System.out.println( "Gewichte nach Lernen" + Arrays.deepToString( weights ) );
		System.out.println( Arrays.deepToString( propagate( input, weights, bias ) ) );

		/*
		 * NeuralNetwork network = new NeuralNetwork( 2, 4, 1 ); NetworkTrainer
		 * trainer = new NetworkTrainer( network ); TrainingLesson lession = new
		 * TrainingLesson( new double[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1
		 * } }, new double[][] { { 0 }, { 1 }, { 1 }, { 0 } }, 0.1 ); //
		 * trainer.train(lession);
		 * 
		 * double[][][] weights = new double[][][]{ { {0.15,0.2}, {0.25,0.3} },{
		 * {0.4,0.45}, {0.5,0.55} } };
		 * 
		 * 
		 * 
		 * System.out.println( ErrorMeasurement.getErrorSquareSum( network,
		 * lession ) );
		 * 
		 * trainer.train( lession );
		 */

		// System.out.println(Arrays.toString(network.getLayer(0).getActivationLevels()));
		// System.out.println(Arrays.toString(network.getLayer(1).getActivationLevels()));
		// System.out.println(Arrays.toString(network.getLayer(2).getActivationLevels()));

		/*
		 * try { InputStream stream = new FileInputStream(new
		 * File("networkConfig/network.xml"));
		 * NeuralNetworkFactory.from(stream); } catch (FileNotFoundException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		MathEngine engine = new MathEngine();

		Scanner scanner = new Scanner( System.in );
		while ( true )
		{
			System.out.print( "Query:" );
			String therm = scanner.nextLine();
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

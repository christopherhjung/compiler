/*
 * Decompiled with CFR 0_118.
 */
package trainer;

import java.util.stream.Stream;

import neuron.NeuralNetwork;

public class ErrorMeasurement {
	
	
	public static double getErrorAbsoluteSum( NeuralNetwork neuralNetwork, TrainingLesson trainingLesson )
	{
		double[][] arrd = trainingLesson.getInputs();
		double[][] arrd2 = trainingLesson.getDesiredOutputs();
		double d = 0.0;
		int n = 0;
		while ( n < arrd.length )
		{
			double[] arrd3 = neuralNetwork.propagate( arrd[n] );
			int n2 = 0;
			while ( n2 < arrd3.length )
			{
				double d2 = 0.0;
				d2 = arrd2[n][n2] - arrd3[n2];
				d2 = Math.abs( d2 );
				d += d2;
				++n2;
			}
			++n;
		}
		return d;
	}

	public static double getErrorEuclideanSum( NeuralNetwork neuralNetwork, TrainingLesson trainingLesson )
	{
		double[][] arrd = trainingLesson.getInputs();
		double[][] arrd2 = trainingLesson.getDesiredOutputs();
		double d = 0.0;
		int n = 0;
		while ( n < arrd.length )
		{
			double[] arrd3 = neuralNetwork.propagate( arrd[n] );
			int n2 = 0;
			while ( n2 < arrd3.length )
			{
				double d2 = 0.0;
				d2 = arrd2[n][n2] - arrd3[n2];
				d2 *= d2;
				d2 = Math.sqrt( d2 );
				d += d2;
				++n2;
			}
			++n;
		}
		return d;
	}

	public static double getErrorSquaredPercentagePrechelt( NeuralNetwork neuralNetwork, TrainingLesson trainingLesson )
	{
		double[][] arrd = trainingLesson.getInputs();
		double[][] arrd2 = trainingLesson.getDesiredOutputs();
		double d = 0.0;
		double d2 = Double.MAX_VALUE;
		double d3 = Double.MIN_VALUE;
		int n = 0;
		while ( n < arrd.length )
		{
			double[] arrd3 = neuralNetwork.propagate( arrd[n] );
			int n2 = 0;
			while ( n2 < arrd3.length )
			{
				if ( arrd2[n][n2] < d2 )
				{
					d2 = arrd2[n][n2];
				}
				if ( arrd2[n][n2] > d3 )
				{
					d3 = arrd2[n][n2];
				}
				double d4 = 0.0;
				d4 = arrd2[n][n2] - arrd3[n2];
				d4 *= d4;
				d4 = Math.sqrt( d4 );
				d += d4;
				++n2;
			}
			++n;
		}
		return d *= 100.0 * ((d3 - d2) / (double) (arrd2[0].length * arrd.length));
	}

	public static double getErrorSquareSum( NeuralNetwork network, TrainingLesson trainingLesson )
	{
		double[][] inputs = trainingLesson.getInputs();
		double[][] rightOutputs = trainingLesson.getDesiredOutputs();
		double error = 0.0;
		 
		for (int currentTest = 0; currentTest < inputs.length; currentTest++ )
		{
			double[] currentOutput = network.propagate( inputs[currentTest] );
			
			for ( int index = 0; index < currentOutput.length ; index++ )
			{
				double temp = rightOutputs[currentTest][index] - currentOutput[index];
				error += temp * temp;
			}
		}
		return error;
	}

	public static double getErrorRootMeanSquareSum( NeuralNetwork neuralNetwork, TrainingLesson trainingSampleLesson )
	{
		double[][] arrd = trainingSampleLesson.getInputs();
		double[][] arrd2 = trainingSampleLesson.getDesiredOutputs();
		double d = 0.0;
		int n = 0;
		while ( n < arrd.length )
		{
			double[] arrd3 = neuralNetwork.propagate( arrd[n] );
			int n2 = 0;
			while ( n2 < arrd3.length )
			{
				double d2 = 0.0;
				d2 = arrd2[n][n2] - arrd3[n2];
				d2 *= d2;
				d += d2;
				++n2;
			}
			++n;
		}
		d /= (double) arrd2[0].length;
		d = Math.sqrt( d );
		return d;
	}
}

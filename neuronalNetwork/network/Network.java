package network;
import matrix.MapMatrix;
import matrix.Matrix;
import matrix.MultiplyMatrix;
import neuronbehavior.NeuronBehavior;
import neuronbehavior.SigmoidBehavior;
import trainer.ErrorMeasurement;
import trainer.TrainResult;
import trainer.TrainingLesson;

public class Network {

	private Matrix[] weights;
	private Matrix bias;
	private NeuronBehavior neuronBehaviour = new SigmoidBehavior();

	public Network( int... layerSizes )
	{
		weights = new Matrix[layerSizes.length - 1];

		for ( int i = 0 ; i < layerSizes.length - 1 ; i++ )
		{
			weights[i] = Matrix.ofSize( layerSizes[i], layerSizes[i + 1] );
			weights[i].map( ( row, col ) -> Math.random() );
		}

		bias = Matrix.ofSize(layerSizes.length - 1);
	}

	public Matrix propagate( Matrix input )
	{
		Matrix result = Matrix.copy( input );

		for ( int layerIndex = 0 ; layerIndex < weights.length ; layerIndex++ )
		{
			Matrix layer = weights[layerIndex];
			double bia = bias.get( layerIndex, 1 );

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
			double[] nextTempResult = new double[layer.cols()];

			for ( int v = 0 ; v < layer.cols() ; v++ )
			{
				for ( int u = 0 ; u < layer.rows() ; u++ )
				{
					nextTempResult[v] += layer.get( u, v ) * tempResults[u];
				}

				nextTempResult[v] = neuronBehaviour.calcActivation( nextTempResult[v] + bias.get( layerIndex ) );
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
			double[] nextTempResult = new double[layer.cols()];

			for ( int v = 0 ; v < layer.cols() ; v++ )
			{
				for ( int u = 0 ; u < layer.rows() ; u++ )
				{

					nextTempResult[v] += layer.get( u, v ) * result[layerIndex][u];
				}

				nextTempResult[v] = neuronBehaviour.calcActivation( nextTempResult[v] + bias.get( layerIndex ) );
			}

			result[layerIndex + 1] = nextTempResult;
		}

		return result;
	}

	public Matrix[] getWeights()
	{
		return weights;
	}
	
	public Matrix getBias()
	{
		return bias;
	}
	
	public NeuronBehavior getNeuronBehaviour( int layer )
	{
		return neuronBehaviour;
	}
}

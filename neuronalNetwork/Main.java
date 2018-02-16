import java.util.Arrays;

import matrix.MultiplyMatrix;
import matrix.IgnoreMatrix;
import matrix.Matrix;
import matrix.NumberMatrix;
import matrix.TransposedMatrix;
import trainer.TrainResult;
import trainer.TrainingLesson;


public class Main {

	
	public static void main( String[] args )
	{
		Network network = new Network(4,2,1);
		
		double[][] samples = new double[][]{
			{0,0,0,0},
			{0,0,0,1},
			{0,0,1,0},
			{0,0,1,1},
			{0,1,0,0},
			{0,1,0,1},
			{0,1,1,0},
			{0,1,1,1},
			{1,0,0,0}};
			
		double[][] targets = new double[][]{
			{0.0},
			{0.1},
			{0.2},
			{0.3},
			{0.4},
			{0.5},
			{0.6},
			{0.7},
			{0.8}};
			
		TrainingLesson lession = new TrainingLesson( samples, targets, 0.1 );
		
		TrainResult result = network.train( lession, 0.001 );

		System.out.println( result );
		
		double[][] target = network.propagate( samples );
		System.out.println( Arrays.deepToString( target ) );
	}

}

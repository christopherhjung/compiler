import java.util.Arrays;

import matrix.MultiplyMatrix;
import matrix.IgnoreMatrix;
import matrix.Matrix;
import matrix.NumberMatrix;
import matrix.TransposedMatrix;


public class Core3 {

	
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
		
		network.train( samples, targets, 0.1, 0.001 );

		double[][] target = network.propagate( samples );
		System.out.println( Arrays.deepToString( target ) );
		
		long start = System.nanoTime();
		double[] test = network.propagate( 1,0,0,1 );
		long end = System.nanoTime();
		
		System.out.println( end - start );
		
		
		System.out.println( Arrays.toString( test ) );
	}

}

package maths;

import crap.MatrixD;
import crap.VectorD;
import therms.ArrayPolynom;
import therms.Polynom;

public class PolynomFitter {

	public static Polynom fit( double[] x, double[] y )
	{
		return fit( x, y, x.length - 1 );
	}

	public static Polynom fit( double[] x, double[] y, int grad )
	{
		double[][] A = new double[x.length][grad + 1];

		for ( int j = 0 ; j < x.length ; j++ )
		{
			for ( int i = 0 ; i < grad + 1 ; i++ )
			{
				A[j][i] = i == 0 ? 1 : x[j] * A[j][i - 1];
			}
		}

		double[][] AT = MatrixD.transpose( A, null );
		double[][] ATA = MatrixD.multiply( AT, A );
		double[] ATY = VectorD.multiply( AT, y );

		return new ArrayPolynom( MatrixD.getSolutions( ATA, ATY ) );
	}
	
}

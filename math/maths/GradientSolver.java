package maths;
import crap.MatrixD;
import crap.VectorD;

public class GradientSolver {

	public static final double EPSILON = 1e-40;
	public static final double EPSILON_SQUARED = EPSILON * EPSILON;

	public static VectorD solve( MatrixD A, VectorD b )
	{
		return solve( A, b, new VectorD( b.size() ) );
	}

	public static VectorD solve( MatrixD A, VectorD b, VectorD start )
	{
		MatrixD transpose = A.transpose( null );
		transpose.multiply( b, b );
		transpose.multiply( A, A );
		
		VectorD x = start;
		VectorD p = new VectorD( b.size() );
		VectorD r = A.multiply( x, null );
		VectorD rLast = new VectorD( b.size() );

		b.sub( r, r );
		p.load( r );
		VectorD temp = null;
		while ( r.dot( r ) > EPSILON_SQUARED )
		{
			temp = A.multiply( p, temp );
			double lambda = r.dot( p ) / p.dot( temp );

			rLast.load( r );
			r.sub( temp.scale( lambda ), r );
			p.scale( lambda, temp ).add( x, x );

			double beta;

			if ( false )
			{
				// fletcher-reeves
				beta = r.dot( r ) / rLast.dot( rLast );
			}
			else if ( true )
			{
				// polak-ribiere
				beta = r.dot( r.sub( rLast, temp ) ) / rLast.dot( rLast );
			}
			else
			{
				// hestens-stiefel
				beta = r.dot( r.sub( rLast, temp ) ) / p.dot( r.sub( rLast, temp ) );
			}

			p.scale( beta ).add( r, p );
		}

		return x;
	}

	public static VectorD solve2( MatrixD A, VectorD b, VectorD start )
	{
		// Matrix transA = A.transponiert( null );
		// A = A.multiply( transA, null );
		// b = Vector.multiply( transA, b, null );

		System.out.println( "A" + A );
		System.out.println( "b" + b );

		VectorD x = start;
		VectorD p = new VectorD( b.size() );
		VectorD r = A.multiply( x, null );

		b.sub( r, r );
		p.load( r );

		double dotR = r.dot( r );

		VectorD temp = null;
		for ( int i = 0 ;; i++ )
		{
			if ( dotR < EPSILON_SQUARED )
			{
				break;
			}

			temp = A.multiply( p, temp );
			double lambda = r.dot( p ) / p.dot( temp );

			r.sub( temp.scale( lambda ), r );
			p.scale( lambda, temp ).add( x, x );

			double nextDotR = r.dot( r );

			p.scale( nextDotR / dotR ).add( r, p );

			dotR = nextDotR;
		}

		return x;
	}
}

package matrix;

public class MatrixUtils {
	public static Matrix fromArray( double[] array ){
		return new NumberMatrix( array );
	}
	
	public static Matrix fromArray( double[][] matrix ){
		return new NumberMatrix( matrix );
	}
	
	public static Matrix ofSize( int rows, int cols ){
		return new NumberMatrix( rows, cols );
	}
	
	public static Matrix copy( Matrix matrix ){
		return new NumberMatrix( matrix );
	}
}

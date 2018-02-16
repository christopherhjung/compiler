package matrix;

public class MultiplyMatrix extends Matrix{

	private Matrix left;
	private Matrix right;
	
	public MultiplyMatrix( Matrix left, Matrix right ){
		if(left.cols() != right.rows()){
			throw new RuntimeException("Matrices are not for multiply");
		}
		
		this.left = left;
		this.right = right;
	}

	@Override
	public double get( int row, int col )
	{
		double result = 0;
		for( int i = left.cols() - 1 ; i >= 0 ; i-- ){
			result += left.get( row, i ) * right.get( i, col );
		}
		
		return result;
	}

	@Override
	public void set( int row, int col, double value )
	{
		throw new RuntimeException("NEE");
	}

	@Override
	public int rows()
	{
		return left.rows();
	}

	@Override
	public int cols()
	{
		return right.cols();
	}
}

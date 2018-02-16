package matrix;

public class NumberMatrix extends Matrix{

	private final int cols;
	private final int rows;
	
	private final double values[][];
	
	NumberMatrix( int rows , int cols ){
		this.rows = rows;
		this.cols = cols;
		values = new double[rows][cols];
	}
	
	NumberMatrix( Matrix matrix )
	{
		this.rows = matrix.rows();
		this.cols = matrix.cols();
		values = new double[this.rows][this.cols];
		matrix.forEach( (row,col,val) -> values[row][col] = val );
	}
	
	NumberMatrix( double[][] matrix )
	{
		this.rows = matrix.length;
		this.cols = matrix[0].length;
		values = new double[this.rows][this.cols];
		
		for( int row = 0 ; row < rows ; row++ ){
			System.arraycopy( matrix, 0, values[row], 0, cols );
		}
	}

	NumberMatrix( double[] row )
	{
		this.cols = row.length;
		this.rows = 1;
		values = new double[this.rows][this.cols];
		System.arraycopy( row, 0, values[0], 0, cols );
	}
	
	@Override
	public double get( int row, int col )
	{
		return values[row][col];
	}
	
	@Override
	public void set( int row, int col, double value )
	{
		values[row][col] = value;
	}
	
	@Override
	public int cols()
	{
		return cols;
	}
	
	@Override
	public int rows()
	{
		return rows;
	}
}

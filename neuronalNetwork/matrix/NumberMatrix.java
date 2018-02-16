package matrix;
import java.util.Arrays;
import java.util.function.BiFunction;

public class NumberMatrix extends Matrix{

	private final int columns;
	private final int rows;
	
	private final double values[][];
	
	public NumberMatrix( int rows , int columns ){
		this.rows = rows;
		this.columns = columns;
		values = new double[rows][columns];
	}
	
	public NumberMatrix( Matrix matrix )
	{
		this.rows = matrix.rowSize();
		this.columns = matrix.colSize();
		values = new double[this.rows][this.columns];
		matrix.forEach( (row,col,val) -> values[row][col] = val );
	}
	
	public NumberMatrix( double[][] matrix )
	{
		this.rows = matrix.length;
		this.columns = matrix[0].length;
		values = new double[this.rows][this.columns];
		
		for( int row = 0 ; row < rows ; row++ ){
			System.arraycopy( matrix, 0, values[row], 0, columns );
		}
	}

	public NumberMatrix( double[] row )
	{
		this.columns = row.length;
		this.rows = 1;
		values = new double[this.rows][this.columns];
		System.arraycopy( row, 0, values[0], 0, columns );
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
	public int colSize()
	{
		return columns;
	}
	
	@Override
	public int rowSize()
	{
		return rows;
	}
}

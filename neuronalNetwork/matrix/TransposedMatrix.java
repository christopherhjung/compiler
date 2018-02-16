package matrix;

public class TransposedMatrix extends Matrix{

	private Matrix matrix;
	
	public TransposedMatrix( Matrix matrix )
	{
		this.matrix = matrix;
	}
	
	@Override
	public double get( int row, int col )
	{
		return matrix.get( col, row );
	}

	@Override
	public void set( int row, int col, double value )
	{
		matrix.set( col, row, value );
	}

	@Override
	public int rowSize()
	{
		return matrix.colSize();
	}

	@Override
	public int colSize()
	{
		return matrix.rowSize();
	}

}

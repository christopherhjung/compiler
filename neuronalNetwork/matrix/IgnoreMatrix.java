package matrix;

public class IgnoreMatrix extends Matrix{

	private Matrix matrix;
	private int ignoreRow;
	private int ignoreColumn;
	
	public IgnoreMatrix( int ignoreRow , int ignoreColumn , Matrix matrix ){
		this.ignoreRow = ignoreRow;
		this.ignoreColumn = ignoreColumn;
		this.matrix = matrix;
	}
	
	@Override
	public double get( int row, int col )
	{
		if( row >= ignoreRow ){
			row++;
		}
		
		if( col >= ignoreColumn ){
			col++;
		}
		
		return matrix.get( row, col );
	}
	
	@Override
	public void set( int row, int col, double value )
	{
		if( row > ignoreRow ){
			row++;
		}
		
		if( col > ignoreColumn ){
			col++;
		}
		
		matrix.set( row, col, value );
	}
	
	@Override
	public int rowSize()
	{
		int parentRows = matrix.rowSize();
		if( 0 <= ignoreRow && ignoreRow < parentRows ){
			parentRows--;
		}
		
		return parentRows;
	}
	
	@Override
	public int colSize()
	{
		int parentColumns = matrix.colSize();
		if( 0 <= ignoreColumn && ignoreColumn < parentColumns ){
			parentColumns--;
		}
		
		return parentColumns;
	}
}

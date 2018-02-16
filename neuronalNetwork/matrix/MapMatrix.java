package matrix;

import java.util.function.Function;

public class MapMatrix extends Matrix{

	private Matrix matrix;
	private Function<Double, Double> func;
	
	public MapMatrix( Matrix matrix, Function<Double, Double> func ){
		this.matrix = matrix;
		this.func = func;
	}

	@Override
	public double get( int row, int col )
	{
		return func.apply( matrix.get( row, col ) );
	}

	@Override
	public void set( int row, int col, double value )
	{
		matrix.set( row, col, value / func.apply( matrix.get( row, col ) ) );
		
	}

	@Override
	public int rowSize()
	{
		return matrix.rowSize();
	}

	@Override
	public int colSize()
	{
		return matrix.colSize();
	}
	
	
	
}

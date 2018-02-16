package matrix;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public abstract class Matrix {

	public abstract double get( int row , int col );
	
	public abstract void set( int row, int col, double value );
	
	public void dec( int row, int col, double value ){
		set(row,col, get(row,col) - value);
	}
	
	public void inc( int row, int col, double value ){
		set(row,col, get(row,col) + value);
	}
	
	public void zero(){
		map((row,col)->0.0);
	}
	
	public abstract int rowSize();
	
	public abstract int colSize();
		
	public void forEach( BiConsumer<Integer, Integer> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				func.accept( row,col );
			}
		}
	}
	
	public void forEach( TriConsumer<Integer, Integer, Double> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				func.accept( row,col, get(row,col) );
			}
		}
	}
	
	public void map( BiFunction<Integer, Integer, Double> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				set(row,col,func.apply( row, col ));
			}
		}
	}
	
	public double sum( BiFunction<Integer,Integer, Double> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		double sum = 0;
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				sum += func.apply( row, col );
			}
		}
		
		return sum;
	}
	
	public void inc( BiFunction<Integer, Integer, Double> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				inc(row,col,func.apply( row, col ));
			}
		}
	}
	
	public void dec( BiFunction<Integer, Integer, Double> func )
	{
		int rows = rowSize();
		int columns = colSize();
		
		for( int row = 0 ; row < rows ; row++ ){
			for( int col = 0 ; col < columns ; col++ ){
				dec(row,col,func.apply( row, col ));
			}
		}
	}
	
	public NumberMatrix copy(){
		return new NumberMatrix( this );
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "[" );
		for( int row = 0 ; row < rowSize() ; row++ ){
			//if(row>0)
			//	sb.append( "" );
			sb.append( "[" );
			for( int col = 0 ; col < colSize() ; col++ ){
				if(col > 0)
					sb.append( "," );
				sb.append( get( row, col ) );
			}
			sb.append( "]" );
		}
		sb.append( "]" );
		return sb.toString();
	}
}

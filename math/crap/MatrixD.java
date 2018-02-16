package crap;

import java.util.Arrays;
import java.util.Iterator;

public class MatrixD implements RowHandlerDouble {

	private double[][] values;

	public MatrixD( MatrixD matrix )
	{
		load( matrix );
	}

	public MatrixD( double[][] values )
	{
		load( values );
	}

	public MatrixD( int rows, int columns )
	{
		if ( rows <= 0 || columns <= 0 )
		{
			throw new RuntimeException( "Rows or Columns 0 is not allowed" );
		}

		this.values = new double[rows][columns];
	}

	public int rowSize()
	{
		return values == null ? 0 : values.length;
	}

	public int columnSize()
	{
		return values == null ? 0 : values[0].length;
	}

	public double[][] getValues()
	{
		return values;
	}

	public MatrixD load( MatrixD matrix )
	{
		load( matrix.values );
		return matrix;
	}

	public MatrixD load( double[][] values )
	{
		if ( values.length != columnSize() || values[0].length != rowSize() )
		{
			this.values = new double[values.length][values[0].length];
		}

		for ( int i = 0 ; i < values.length ; i++ )
		{
			System.arraycopy( values[i], 0, this.values[i], 0, values[i].length );
		}

		return this;
	}

	private MatrixD set( double[][] matrix )
	{
		this.values = matrix;
		return this;
	}

	public MatrixD subMatrix( int ignoreRow, int ignoreColumn, MatrixD dest )
	{
		double[][] subMatrix = null;
		if ( dest != null )
		{
			subMatrix = dest.values;
		}

		subMatrix = subMatrix( ignoreRow, ignoreColumn, subMatrix );

		return dest.set( subMatrix );
	}

	public double[][] subMatrix( int ignoreRow, int ignoreColumn, double[][] dest )
	{
		int rows = rowSize() - (ignoreRow >= 0 && ignoreRow < dest.length ? 1 : 0);
		int columns = columnSize() - (ignoreColumn >= 0 && ignoreColumn < dest[0].length ? 1 : 0);

		if ( values == dest )
		{
			throw new RuntimeException( "dest is same as src" );
		}

		if ( dest == null || dest.length != rows && dest[0].length != columns )
		{
			dest = new double[rows][columns];
		}

		for ( int row = 0, currentRow = 0 ; row < values.length ; row++ )
		{
			if ( row == ignoreRow )
			{
				continue;
			}

			for ( int column = 0, currentColumn = 0 ; column < values[row].length ; column++ )
			{
				if ( column == ignoreColumn )
				{
					continue;
				}

				dest[currentRow][currentColumn] = values[row][column];
				currentColumn++;
			}
			currentRow++;
		}

		return dest;
	}

	public MatrixD normalize()
	{
		return normalize( this, this );
	}

	public static MatrixD normalize( MatrixD src, MatrixD dest )
	{
		normalize( src.values, dest.values );
		return dest;
	}

	public static void normalize( double[][] src, double[][] dest )
	{
		if ( src != dest )
		{
			if ( dest == null )
			{
				dest = clone( src );
			}
			else
			{
				load( src, dest );
			}
		}

		double invers = 1;
		for ( int i = 0 ; i < dest.length ; i++ )
		{
			c: for ( int y = i + 1 ; y < dest.length ; y++ )
			{
				int switchRow = dest.length - 1;
				while ( dest[i][i] == 0 )
				{
					if ( switchRow <= i ) continue c;

					double[] temp = dest[i];
					dest[i] = dest[switchRow];
					dest[switchRow--] = temp;
					invers *= -1;
				}

				double factor = dest[y][i] / dest[i][i];
				for ( int x = i ; x < dest[y].length ; x++ )
				{
					dest[y][x] = dest[y][x] - factor * dest[i][x];
				}
			}
		}

		if ( invers < 0 )
		{
			for ( int i = 0 ; i < dest[0].length ; i++ )
			{
				dest[dest.length - 1][i] *= invers;
			}
		}
	}

	public double determinant()
	{
		return determinant( this );
	}

	public static double determinant( MatrixD matrix )
	{
		return determinant( matrix.values );
	}

	public static double determinant( double[][] matrix )
	{
		int size = matrix.length;
		if ( size != matrix[0].length ) throw new ArithmeticException( "Matrix need to be a square" );

		double[][] dp = clone( matrix );

		double result = 1;
		for ( int i = 0 ; i < size ; i++ )
		{
			for ( int y = i + 1 ; y < size ; y++ )
			{
				int switchRow = size - 1;
				while ( dp[i][i] == 0 )
				{
					if ( switchRow <= i ) return 0;

					double[] temp = dp[i];
					dp[i] = dp[switchRow];
					dp[switchRow--] = temp;
					result *= -1;
				}

				double factor = dp[y][i] / dp[i][i];
				for ( int x = i ; x < size ; x++ )
				{
					dp[y][x] += -factor * dp[i][x];
				}
			}
			result *= dp[i][i];
		}

		return result;
	}

	public MatrixD multiply( MatrixD other, MatrixD dest )
	{
		double[][] result = multiply( this.values, other.values );

		if ( dest == null )
		{
			return new MatrixD( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static MatrixD multiply( MatrixD matrixA, MatrixD matrixB, MatrixD dest )
	{
		return dest.set( multiply( matrixA.values, matrixB.values ) );
	}

	public static double[][] multiply( double[][] matrixA, double[][] matrixB )
	{
		if ( matrixA[0].length != matrixB.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		double[][] result = new double[matrixA.length][matrixB[0].length];

		for ( int y = 0 ; y < matrixA.length ; y++ )
		{
			for ( int x = 0 ; x < matrixB[0].length ; x++ )
			{
				double element = 0;
				for ( int i = 0 ; i < matrixA[0].length ; i++ )
				{
					element += matrixA[y][i] * matrixB[i][x];
				}
				result[y][x] = element;
			}
		}
		return result;
	}

	public VectorD multiply( VectorD vector, VectorD dest )
	{
		double[] result = multiply( this.values, vector.getValues() );

		if ( dest == null )
		{
			return new VectorD( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static double[] multiply( double[][] matrixA, double[] vector )
	{
		if ( matrixA[0].length != vector.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		double[] result = new double[matrixA.length];

		for ( int y = 0 ; y < matrixA.length ; y++ )
		{
			double element = 0;
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				element += matrixA[y][i] * vector[i];
			}
			result[y] = element;
		}
		return result;
	}

	public MatrixD transpose( MatrixD dest )
	{
		if ( dest == null )
		{
			dest = new MatrixD( columnSize(), rowSize() );
		}

		transpose( values, dest.values );
		return dest;
	}

	public static double[][] transpose( double[][] src, double[][] dest )
	{
		if ( dest == null )
		{
			dest = new double[src[0].length][src.length];
		}

		for ( int i = 0 ; i < src.length ; i++ )
		{
			for ( int j = 0 ; j < src[i].length ; j++ )
			{
				dest[j][i] = src[i][j];
			}
		}
		return dest;
	}

	/*
	 * public static double[] getSolutions( double[][] matrix ) { normalize(
	 * matrix, matrix ); double[] result = new double[matrix.length]; for ( int
	 * i = matrix.length - 1 ; i >= 0 ; i-- ) { double element =
	 * matrix[i][matrix[i].length - 1]; for ( int j = i + 1 ; j < matrix.length
	 * ; j++ ) { element -= matrix[i][j] * result[j]; } result[i] = element /
	 * matrix[i][i]; } return result; }
	 */

	public static VectorD getSolutions( MatrixD matrix, VectorD vector )
	{
		followToIdentity( matrix, vector );
		return vector;
	}

	public static double[] getSolutions( double[][] matrix, double[] vector )
	{
		VectorD vec = new VectorD( vector );
		followToIdentity( matrix, vec );
		return vec.getValues();
	}

	public MatrixD identity()
	{
		identity( values );
		return this;
	}

	public static double[][] identity( double[][] matrix )
	{
		for ( int row = 0 ; row < matrix.length ; row++ )
		{
			for ( int col = 0 ; col < matrix.length ; col++ )
			{
				matrix[row][col] = row == col ? 1 : 0;
			}
		}
		return matrix;
	}

	public MatrixD inverse()
	{
		MatrixD inverse = new MatrixD( rowSize(), columnSize() );
		inverse.identity();
		followToIdentity( values, inverse );
		return inverse;
	}

	public MatrixD inverse( MatrixD dest )
	{
		if ( dest == null )
		{
			dest = new MatrixD( inverse() );
		}
		else
		{
			dest.set( inverse().values );
		}

		return dest;
	}

	public static double[][] inverse( double[][] matrix )
	{
		MatrixD inverse = new MatrixD( matrix.length, matrix[0].length );
		inverse.identity();
		followToIdentity( matrix, inverse );
		return inverse.values;
	}

	public void followToIdentity( RowHandlerDouble handler )
	{
		followToIdentity( this, handler );
	}

	public static void followToIdentity( MatrixD matrix, RowHandlerDouble handler )
	{
		followToIdentity( matrix.values, handler );
	}

	public static void followToIdentity( double[][] matrix, RowHandlerDouble handler )
	{
		matrix = clone( matrix );
		int rows = matrix.length;
		double temp = 0;

		for ( int row = 0 ; row < rows ; row++ )
		{
			if ( matrix[row][row] == 0 )
			{
				for ( int subRow = row + 1 ; subRow < rows ; )
				{
					if ( matrix[subRow][row] != 0 )
					{
						add( matrix, subRow, row, 1 );
						handler.add( subRow, row, 1 );
						break;
					}
					subRow++;
					if ( subRow == rows )
					{
						throw new ArithmeticException( "Not possible to identify matrix" );
					}
				}
			}

			for ( int col = 0 ; col < row ; col++ )
			{
				if ( matrix[row][col] != 0 )
				{
					temp = -matrix[row][col] / matrix[col][col];
					add( matrix, col, row, temp );
					handler.add( col, row, temp );
				}
			}
		}

		for ( int row = rows - 1 ; row >= 0 ; row-- )
		{
			temp = 1 / matrix[row][row];
			mul( matrix, row, temp );
			handler.mul( row, temp );
			for ( int subRow = row - 1 ; subRow >= 0 ; subRow-- )
			{
				temp = -matrix[subRow][row] / matrix[row][row];
				add( matrix, row, subRow, temp );
				handler.add( row, subRow, temp );
			}
		}
	}

	@Override
	public void mul( int row, double factor )
	{
		mul( values, row, factor );
	}

	@Override
	public void add( int fromRow, int toRow, double factor )
	{
		add( values, fromRow, toRow, factor );
	}

	public static void mul( double[][] matrix, int row, double factor )
	{
		for ( int col = 0 ; col < matrix[row].length ; col++ )
		{
			matrix[row][col] *= factor;
		}
	}

	public static void add( double[][] matrix, int fromRow, int toRow, double factor )
	{
		for ( int col = 0 ; col < matrix[fromRow].length ; col++ )
		{
			matrix[toRow][col] += matrix[fromRow][col] * factor;
		}
	}

	public static int getRang( double[][] matrix )
	{
		normalize( matrix, matrix );

		for ( int i = matrix.length ; i > 0 ; i-- )
		{
			for ( int x = 0 ; x < matrix[0].length ; x++ )
			{
				if ( Math.abs( matrix[i - 1][x] ) > 1e-6 ) return i;
			}
		}
		return 1;
	}

	public static double[][] clone( double[][] src )
	{
		return load( src, new double[src.length][src[0].length] );
	}

	public static double[][] load( double[][] src, double[][] target )
	{
		for ( int i = 0 ; i < src.length ; i++ )
		{
			System.arraycopy( src[i], 0, target[i], 0, src[i].length );
		}

		return target;
	}

	public MatrixD sub( MatrixD matrix, MatrixD dest )
	{
		if ( dest == null )
		{
			dest = new MatrixD( rowSize(), columnSize() );
		}

		double[][] storage = dest.values;

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			for ( int x = 0 ; x < this.values[y].length ; x++ )
			{
				storage[y][x] = this.values[y][x] - matrix.values[y][x];
			}
		}
		return dest;
	}

	public MatrixD add( MatrixD matrix, MatrixD dest )
	{
		if ( dest == null )
		{
			dest = new MatrixD( rowSize(), columnSize() );
		}

		double[][] storage = dest.values;

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			for ( int x = 0 ; x < this.values[y].length ; x++ )
			{
				storage[y][x] = this.values[y][x] + matrix.values[y][x];
			}
		}
		return dest;
	}

	public MatrixD invert()
	{
		return invert( this );
	}

	public static MatrixD invert( MatrixD matrix )
	{
		double[][] inverted = new double[matrix.rowSize()][matrix.columnSize()];
		return invert( matrix, new MatrixD( inverted ) );
	}

	public static MatrixD invert( MatrixD src, MatrixD dest )
	{
		double determinantInv = 1 / src.determinant();
		double[][] subMatrix = null;

		for ( int row = 0 ; row < src.rowSize() ; row++ )
		{
			for ( int column = 0 ; column < src.columnSize() ; column++ )
			{
				subMatrix = src.subMatrix( row, column, subMatrix );
				dest.values[column][row] = (((row + column) & 1) * 2 - 1) * determinantInv * determinant( subMatrix );
			}
		}

		return dest;
	}

	public Iterator<Iterator<Double>> iterator()
	{
		return new RowIterator();
	}

	public Iterator<Iterator<Double>> iterator( int ignoreRow, int ignoreColumn )
	{
		return new RowIterator( ignoreRow, ignoreColumn );
	}

	public class RowIterator implements Iterator<Iterator<Double>> {

		private int ignoreRow = -1;
		private int ignoreColumn = -1;
		private int currentRow = -1;

		public RowIterator()
		{}

		public RowIterator( int ignoreRow, int ignoreColumn )
		{
			this.ignoreRow = ignoreRow;
			this.ignoreColumn = ignoreColumn;
		}

		@Override
		public boolean hasNext()
		{
			if ( currentRow + 1 == ignoreRow )
			{
				if ( currentRow + 2 < values.length )
				{
					return true;
				}
			}
			else if ( currentRow + 1 < values.length )
			{
				return true;
			}

			return false;
		}

		@Override
		public Iterator<Double> next()
		{
			currentRow++;
			if ( currentRow == ignoreRow )
			{
				currentRow++;
			}

			return new ValueIterator();
		}

		public class ValueIterator implements Iterator<Double> {

			int currentColumn = -1;

			@Override
			public boolean hasNext()
			{
				if ( currentColumn + 1 == ignoreColumn )
				{
					if ( currentColumn + 2 < values[currentRow].length )
					{
						return true;
					}
				}
				else if ( currentColumn + 1 < values[currentRow].length )
				{
					return true;
				}

				return false;
			}

			@Override
			public Double next()
			{
				currentColumn++;
				if ( currentColumn == ignoreColumn )
				{
					currentColumn++;
				}

				return values[currentRow][currentColumn];
			}
		}
	}

	@Override
	public String toString()
	{
		return Arrays.deepToString( values );
	}
}

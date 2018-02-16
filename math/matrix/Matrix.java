package matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.ThermStringify;
import therms.Additional;
import therms.Const;
import therms.Multiply;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class Matrix extends Therm implements RowHandler {

	private Therm[][] values;

	public Matrix( Matrix matrix )
	{
		load( matrix );
	}

	public Matrix( Therm[][] values )
	{
		load( values );
	}

	public Matrix( int rows, int columns )
	{
		if ( rows <= 0 || columns <= 0 )
		{
			throw new RuntimeException( "Rows or Columns 0 is not allowed" );
		}

		this.values = new Therm[rows][columns];
	}

	public int rowSize()
	{
		return values.length;
	}

	public int columnSize()
	{
		return values[0].length;
	}
	
	Therm getValue( int row , int col ){
		return values[row][col];
	}

	public Matrix load( Matrix matrix )
	{
		load( matrix.values );
		return matrix;
	}

	public Matrix load( Therm[][] values )
	{
		if ( this.values == null || values.length != columnSize() || values[0].length != rowSize() )
		{
			this.values = new Therm[values.length][values[0].length];
		}

		for ( int i = 0 ; i < values.length ; i++ )
		{
			System.arraycopy( values[i], 0, this.values[i], 0, values[i].length );
		}
		return this;
	}

	private Matrix set( Therm[][] matrix )
	{
		this.values = matrix;
		return this;
	}

	public Matrix subMatrix( int ignoreRow, int ignoreColumn, Matrix dest )
	{
		Therm[][] subMatrix = null;
		if ( dest != null )
		{
			subMatrix = dest.values;
		}

		subMatrix = subMatrix( ignoreRow, ignoreColumn, this.values, subMatrix );

		return dest.set( subMatrix );
	}

	public static Therm[][] subMatrix( int ignoreRow, int ignoreColumn, Therm[][] src, Therm[][] dest )
	{
		int rows = src.length - (ignoreRow >= 0 ? 1 : 0);
		int columns = src[0].length - (ignoreColumn >= 0 ? 1 : 0);
		if ( dest == null || dest.length != rows && dest[0].length != columns )
		{
			dest = new Therm[rows][columns];
		}

		for ( int row = 0, currentRow = 0 ; row < src.length ; row++ )
		{
			if ( row == ignoreRow )
			{
				continue;
			}

			for ( int column = 0, currentColumn = 0 ; column < src[row].length ; column++ )
			{
				if ( column == ignoreColumn )
				{
					continue;
				}

				dest[currentRow][currentColumn] = src[row][column];
				currentColumn++;
			}
			currentRow++;
		}

		return dest;
	}

	public Matrix normalize()
	{
		return normalize( this, this );
	}

	public static Matrix normalize( Matrix src, Matrix dest )
	{
		normalize( src.values, dest.values );
		return dest;
	}

	public static void normalize( Therm[][] src, Therm[][] dest )
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

		boolean invers = false;
		for ( int i = 0 ; i < dest.length ; i++ )
		{
			c: for ( int y = i + 1 ; y < dest.length ; y++ )
			{
				int switchRow = dest.length - 1;
				while ( dest[i][i].equals( Const.ZERO ) )
				{
					if ( switchRow <= i ) continue c;

					Therm[] temp = dest[i];
					dest[i] = dest[switchRow];
					dest[switchRow--] = temp;
					invers = !invers;
				}

				Therm factor = Const.MINUS_ONE.mul( dest[y][i] ).div( dest[i][i] );
				for ( int x = i ; x < dest[y].length ; x++ )
				{
					dest[y][x] = dest[y][x].add( factor.mul( dest[i][x] ) );
				}
			}
		}

		if ( invers )
		{
			for ( int i = 0 ; i < dest[0].length ; i++ )
			{
				dest[dest.length - 1][i] = Const.MINUS_ONE.mul( dest[dest.length - 1][i] );
			}
		}
	}

	public Therm trueDeterminant()
	{
		return trueDeterminant( this );
	}

	public static Therm trueDeterminant( Matrix matrix )
	{
		return trueDeterminant( matrix.values );
	}

	public static Therm trueDeterminant( Therm[][] matrix )
	{
		if ( matrix.length == 2 )
		{
			return matrix[0][0].mul( matrix[1][1] ).sub( matrix[1][0].mul( matrix[0][1] ) );
		}
		else if ( matrix.length == 1 )
		{
			return matrix[0][0];
		}

		List<Therm> result = new ArrayList<>();
		Therm[][] subMatrix = null;
		for ( int column = 0 ; column < matrix[0].length ; column++ )
		{
			if ( !matrix[0][column].equals( Const.ZERO ) )
			{
				subMatrix = subMatrix( 0, column, matrix, subMatrix );
				result.add( ((column & 1) == 0 ? Const.ONE : Const.MINUS_ONE)
						.mul( matrix[0][column] )
						.mul( trueDeterminant( subMatrix ) ) );
			}
		}
		return new Additional( result ).simplify();
	}

	public Therm determinant()
	{
		return determinant( this );
	}

	public static Therm determinant( Matrix matrix )
	{
		return determinant( matrix.values );
	}

	public static Therm determinant( Therm[][] matrix )
	{
		int size = matrix.length;
		if ( size != matrix[0].length ) throw new ArithmeticException( "Matrix need to be a square" );

		Therm[][] dp = clone( matrix );

		List<Therm> result = new ArrayList<>();
		boolean invert = false;
		for ( int i = 0 ; i < size ; i++ )
		{
			for ( int y = i + 1 ; y < size ; y++ )
			{
				int switchRow = size - 1;
				while ( dp[i][i].equals( Const.ZERO ) )
				{
					if ( switchRow <= i ) return Const.ZERO;

					Therm[] temp = dp[i];
					dp[i] = dp[switchRow];
					dp[switchRow--] = temp;
					invert = !invert;
				}

				Therm factor = Const.MINUS_ONE.mul( dp[y][i] ).mul( dp[i][i].pow( Const.MINUS_ONE ) );
				for ( int x = i ; x < size ; x++ )
				{
					dp[y][x] = dp[y][x].add( factor.mul( dp[i][x] ) );
				}
			}

			result.add( dp[i][i] );
		}

		if ( invert ) result.add( 0, Const.MINUS_ONE );

		return new Multiply( result );
	}

	public Matrix multiply( Matrix other, Matrix dest )
	{
		Therm[][] result = multiply( this.values, other.values );

		if ( dest == null )
		{
			return new Matrix( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static Matrix multiply( Matrix matrixA, Matrix matrixB, Matrix dest )
	{
		return dest.set( multiply( matrixA.values, matrixB.values ) );
	}

	public static Therm[][] multiply( Therm[][] matrixA, Therm[][] matrixB )
	{
		if ( matrixA[0].length != matrixB.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		Therm[][] result = new Therm[matrixA.length][matrixB[0].length];

		for ( int y = 0 ; y < matrixA.length ; y++ )
		{
			for ( int x = 0 ; x < matrixB[0].length ; x++ )
			{
				List<Therm> elements = new ArrayList<>();
				for ( int i = 0 ; i < matrixA[0].length ; i++ )
				{
					elements.add( new Multiply( matrixA[y][i], matrixB[i][x] ) );
				}
				result[y][x] = new Additional( elements );
			}
		}
		return result;
	}

	public Vector multiply( Vector vector, Vector dest )
	{
		Therm[] result = multiply( this.values, vector.getValues() );

		if ( dest == null )
		{
			return new Vector( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static Therm[] multiply( Therm[][] matrixA, Therm[] vector )
	{
		if ( matrixA[0].length != vector.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		Therm[] result = new Therm[matrixA.length];

		for ( int y = 0 ; y < matrixA.length ; y++ )
		{
			List<Therm> elements = new ArrayList<>();
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				elements.add( new Multiply( matrixA[y][i], vector[i] ) );
			}
			result[y] = new Additional( elements );
		}
		return result;
	}

	public Matrix transpose( Matrix dest )
	{
		if ( dest == null )
		{
			dest = new Matrix( columnSize(), rowSize() );
		}

		transpose( values, dest.values );
		return dest;
	}

	public static Therm[][] transpose( Therm[][] src, Therm[][] dest )
	{
		if ( dest == null )
		{
			dest = new Therm[src[0].length][src.length];
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

	public static Vector getSolutions( Matrix matrix, Vector vector )
	{
		followToIdentity( matrix, vector );
		return vector;
	}

	public static Therm[] getSolutions( Therm[][] matrix, Therm[] vector )
	{
		Vector vec = new Vector( vector );
		followToIdentity( matrix, vec );
		return vec.getValues();
	}

	public Matrix identity()
	{
		identity( values );
		return this;
	}

	public static Therm[][] identity( Therm[][] matrix )
	{
		for ( int row = 0 ; row < matrix.length ; row++ )
		{
			for ( int col = 0 ; col < matrix.length ; col++ )
			{
				matrix[row][col] = row == col ? Const.ONE : Const.ZERO;
			}
		}
		return matrix;
	}

	public Matrix inverse()
	{
		Matrix inverse = new Matrix( rowSize(), columnSize() );
		inverse.identity();
		followToIdentity( values, inverse );
		return inverse;
	}

	/*
	 * public MatrixTherm inverse( MatrixTherm dest ) { if ( dest == null ) {
	 * dest = new MatrixTherm( inverse() ); } else { dest.set( inverse().values
	 * ); }
	 * 
	 * return dest; }
	 */

	public static Therm[][] inverse( Therm[][] matrix )
	{
		Matrix inverse = new Matrix( matrix.length, matrix[0].length );
		inverse.identity();
		followToIdentity( matrix, inverse );
		return inverse.values;
	}

	public Matrix trueInverse()
	{
		return trueInverse( this );
	}

	public static Matrix trueInverse( Matrix matrix )
	{
		Therm[][] inverted = new Therm[matrix.rowSize()][matrix.columnSize()];
		return trueInverse( matrix, new Matrix( inverted ) );
	}

	public static Matrix trueInverse( Matrix src, Matrix dest )
	{
		if ( !src.isSquare() )
		{
			throw new ArithmeticException( "Only square Matrices are inversible" );
		}

		Therm determinant = src.trueDeterminant();

		Therm[][] subMatrix = null;

		for ( int row = 0 ; row < src.rowSize() ; row++ )
		{
			for ( int column = 0 ; column < src.columnSize() ; column++ )
			{
				subMatrix = subMatrix( row, column, src.values, subMatrix );
				if ( ((row + column) & 1) == 0 )
				{
					dest.values[column][row] = trueDeterminant( subMatrix ).div( determinant );
				}
				else
				{
					dest.values[column][row] = Const.MINUS_ONE.mul( trueDeterminant( subMatrix ) ).div( determinant );
				}

			}
		}

		return dest;
	}

	public void followToIdentity( RowHandler handler )
	{
		followToIdentity( this, handler );
	}

	public static void followToIdentity( Matrix matrix, RowHandler handler )
	{
		followToIdentity( matrix.values, handler );
	}

	public static void followToIdentity( Therm[][] matrix, RowHandler handler )
	{
		matrix = clone( matrix );
		int rows = matrix.length;
		Therm temp = null;

		for ( int row = 0 ; row < rows ; row++ )
		{
			if ( matrix[row][row].equals( Const.ZERO ) )
			{
				for ( int subRow = row + 1 ; subRow < rows ; )
				{
					if ( !matrix[subRow][row].equals( Const.ZERO ) )
					{
						add( matrix, subRow, row, Const.ONE );
						handler.add( subRow, row, Const.ONE );
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
				if ( !matrix[row][col].equals( Const.ZERO ) )
				{
					temp = Const.MINUS_ONE.mul( matrix[row][col] ).div( matrix[col][col] );
					add( matrix, col, row, temp );
					handler.add( col, row, temp );
				}
			}
		}

		for ( int row = rows - 1 ; row >= 0 ; row-- )
		{
			temp = Const.ONE.div( matrix[row][row] );
			mul( matrix, row, temp );
			handler.mul( row, temp );
			for ( int subRow = row - 1 ; subRow >= 0 ; subRow-- )
			{
				temp = Const.MINUS_ONE.mul( matrix[subRow][row] ).div( matrix[row][row] );
				add( matrix, row, subRow, temp );
				handler.add( row, subRow, temp );
			}
		}
	}

	@Override
	public void mul( int row, Therm factor )
	{
		mul( values, row, factor );
	}

	@Override
	public void add( int fromRow, int toRow, Therm factor )
	{
		add( values, fromRow, toRow, factor );
	}

	public static void mul( Therm[][] matrix, int row, Therm factor )
	{
		for ( int col = 0 ; col < matrix[row].length ; col++ )
		{
			matrix[row][col] = matrix[row][col].mul( factor );
		}
	}

	public static void add( Therm[][] matrix, int fromRow, int toRow, Therm factor )
	{
		for ( int col = 0 ; col < matrix[fromRow].length ; col++ )
		{
			matrix[toRow][col] = matrix[toRow][col].add( matrix[fromRow][col].mul( factor ) );
		}
	}

	/*
	 * public static int getRang( Therm[][] matrix ) { normalize( matrix, matrix
	 * );
	 * 
	 * for ( int i = matrix.length ; i > 0 ; i-- ) { for ( int x = 0 ; x <
	 * matrix[0].length ; x++ ) { if ( Math.abs( matrix[i - 1][x] ) > 1e-6 )
	 * return i; } } return 1; }
	 */

	public Therm[][] clone()
	{
		return load( this.values, new Therm[this.values.length][this.values[0].length] );
	}

	public static Therm[][] clone( Therm[][] src )
	{
		return load( src, new Therm[src.length][src[0].length] );
	}

	public static Therm[][] load( Therm[][] src, Therm[][] target )
	{
		for ( int i = 0 ; i < src.length ; i++ )
		{
			System.arraycopy( src[i], 0, target[i], 0, src[i].length );
		}

		return target;
	}

	public Matrix sub( Matrix matrix, Matrix dest )
	{
		if ( dest == null )
		{
			dest = new Matrix( rowSize(), columnSize() );
		}

		Therm[][] storage = dest.values;

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			for ( int x = 0 ; x < this.values[y].length ; x++ )
			{
				storage[y][x] = this.values[y][x].sub( matrix.values[y][x] );
			}
		}
		return dest;
	}

	public Matrix add( Matrix matrix, Matrix dest )
	{
		if ( dest == null )
		{
			dest = new Matrix( rowSize(), columnSize() );
		}

		Therm[][] storage = dest.values;

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			for ( int x = 0 ; x < this.values[y].length ; x++ )
			{
				storage[y][x] = this.values[y][x].add( matrix.values[y][x] );
			}
		}
		return dest;
	}

	@Override
	public Therm derivate( Variable name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double valueAt( VarSet varSet )
	{
		return 0;
	}

	@Override
	public Matrix simplify()
	{
		Therm[][] newMatrix = new Therm[rowSize()][columnSize()];

		for ( int row = 0 ; row < newMatrix.length ; row++ )
		{
			for ( int column = 0 ; column < newMatrix[row].length ; column++ )
			{
				newMatrix[row][column] = values[row][column].simplify();
			}
		}

		return new Matrix( newMatrix );
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Therm contractMultiply( Therm therm )
	{
		if ( therm instanceof Vector )
		{
			Vector other = (Vector) therm;
			return this.multiply( other, null );
		}
		else if ( therm instanceof Matrix )
		{
			Matrix other = (Matrix) therm;
			return this.multiply( other, null );
		}

		return super.contractMultiply( therm );
	}

	@Override
	public void toString( ThermStringify builder )
	{
		builder.append( Arrays.deepToString( values ).replaceAll( "\\[", "{" ).replaceAll( "\\]", "}\n" ) );
	}

	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}

	public boolean isSquare()
	{
		return rowSize() == columnSize();
	}
}

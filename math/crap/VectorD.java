package crap;

import java.util.Arrays;

public class VectorD implements RowHandlerDouble{

	private double[] values;
	
	public VectorD( int size )
	{
		this.values = new double[size];
	}

	public VectorD( double[] values )
	{
		this.values = values;
	}

	public double[] getValues()
	{
		return values;
	}

	public VectorD set( double[] values )
	{
		this.values = values;
		return this;
	}
	
	public int size()
	{
		return values.length;
	}

	public VectorD load( VectorD other )
	{
		if ( this.values.length != other.values.length )
		{
			throw new RuntimeException( "different size" );
		}

		System.arraycopy( other.values, 0, this.values, 0, values.length );
		return this;
	}

	public VectorD sub( VectorD vector, VectorD dest )
	{
		if ( dest == null )
		{
			dest = new VectorD( this.values.length );
		}

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			dest.values[y] = this.values[y] - vector.values[y];
		}
		return dest;
	}

	public VectorD add( VectorD vector, VectorD dest )
	{
		if ( dest == null )
		{
			dest = new VectorD( this.values.length );
		}

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			dest.values[y] = this.values[y] + vector.values[y];
		}
		return dest;
	}

	public double dot( VectorD other )
	{
		if ( this.values.length != other.values.length )
		{
			throw new RuntimeException( "different size" );
		}

		double result = 0;
		for ( int y = 0 ; y < values.length ; y++ )
		{
			result += values[y] * other.values[y];
		}
		return result;
	}

	public VectorD transponiertMultiply( MatrixD matrix, VectorD dest )
	{
		return multiply(this,matrix,dest);
	}
	
	public static VectorD multiply( VectorD vector, MatrixD matrix, VectorD dest )
	{
		double[] result =  multiply(vector.values,matrix.getValues());
		if ( dest == null )
		{
			return new VectorD( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static double[] multiply( double[] vector, double[][] matrix )
	{
		if ( vector.length != matrix.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		double[] result = new double[vector.length];

		for ( int y = 0 ; y < matrix[0].length ; y++ )
		{
			double element = 0;
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				element += vector[i] * matrix[i][y];
			}
			result[y] = element;
		}
		return result;
	}

	public static VectorD multiply( MatrixD matrix, VectorD vector, VectorD dest )
	{
		if(dest == null){
			dest = new VectorD(matrix.rowSize());
		}
		
		dest.set( multiply(matrix.getValues(),vector.values) );
		return dest;
	}
		
	public static double[] multiply( double[][] matrix, double[] vector )
	{
		if ( vector.length != matrix[0].length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		double[] result = new double[vector.length];

		for ( int y = 0 ; y < matrix.length ; y++ )
		{
			double element = 0;
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				element += vector[i] * matrix[y][i];
			}
			result[y] = element;
		}
		return result;
	}
	
	public VectorD scale( double scale )
	{
		return scale( scale, this );
	}

	public VectorD scale( double scale, VectorD dest )
	{
		return scale(this,scale,dest);
	}
	
	public static VectorD scale( VectorD vector, double scale, VectorD dest )
	{
		if ( dest == null )
		{
			dest = new VectorD( vector.size() );
		}

		for ( int y = 0 ; y < dest.values.length ; y++ )
		{
			dest.values[y] = vector.values[y] * scale;
		}
		return dest;
	}

	public double lengthSquared()
	{
		return dot( this );
	}
	
	public double length()
	{
		return Math.sqrt( lengthSquared() );
	}

	@Override
	public void add( int fromRow, int toRow, double factor )
	{
		values[toRow] += values[fromRow] * factor;
	}

	@Override
	public void mul( int row, double factor )
	{
		values[row] *= factor;
	}

	@Override
	public String toString()
	{
		return Arrays.toString( this.values );
	}
}

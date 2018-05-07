package matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import functions.AddPlugin.Additional;
import functions.ConstPlugin.Const;
import functions.MulPlugin.Multiply;
import functions.VariablePlugin.Variable;
import parser.ThermStringifier;
import therms.Therm;
import therms.VarSet;

public class Vector extends Therm implements RowHandler {

	private Therm[] values;

	public Vector( int size )
	{
		values = new Therm[size];
		Arrays.fill( values, Const.ZERO );
	}

	public Vector( double[] values )
	{
		this.values = new Therm[values.length];
		for ( int i = 0 ; i < values.length ; i++ )
		{
			this.values[i] = new Const( values[i] );
		}
	}

	public Vector( Therm[] values )
	{
		this.values = values;
	}

	public Therm[] getValues()
	{
		return values;
	}

	public Vector set( Therm[] values )
	{
		this.values = values;
		return this;
	}

	public int size()
	{
		return values.length;
	}

	public Vector load( Vector other )
	{
		if ( this.values.length != other.values.length )
		{
			throw new RuntimeException( "different size" );
		}

		System.arraycopy( other.values, 0, this.values, 0, values.length );
		return this;
	}

	public Vector sub( Vector vector, Vector dest )
	{
		if ( dest == null )
		{
			dest = new Vector( this.values.length );
		}

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			dest.values[y] = new Additional( this.values[y], new Multiply( Const.MINUS_ONE, vector.values[y] ) );
		}
		return dest;
	}

	public Vector add( Vector vector, Vector dest )
	{
		if ( dest == null )
		{
			dest = new Vector( this.values.length );
		}

		for ( int y = 0 ; y < this.values.length ; y++ )
		{
			dest.values[y] = this.values[y].add( vector.values[y] );
		}
		return dest;
	}

	public Therm dot( Vector other )
	{
		if ( this.values.length != other.values.length )
		{
			throw new RuntimeException( "different size" );
		}

		List<Therm> result = new ArrayList<>();
		for ( int y = 0 ; y < values.length ; y++ )
		{
			result.add( values[y].mul( other.values[y] ) );
		}
		return new Additional( result );
	}

	public Vector transponiertMultiply( Matrix matrix, Vector dest )
	{
		return multiply( this, matrix, dest );
	}

	public static Vector multiply( Vector vector, Matrix matrix, Vector dest )
	{
		Therm[] result = multiply( vector.values, matrix.getValues() );
		if ( dest == null )
		{
			return new Vector( result );
		}
		else
		{
			return dest.set( result );
		}
	}

	public static Therm[] multiply( Therm[] vector, Therm[][] matrix )
	{
		if ( vector.length != matrix.length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		Therm[] result = new Therm[vector.length];

		for ( int y = 0 ; y < matrix[0].length ; y++ )
		{
			List<Therm> additional = new ArrayList<>();
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				additional.add( vector[i].mul( matrix[i][y] ) );
			}
			result[y] = new Additional( additional );
		}
		return result;
	}

	public static Vector multiply( Matrix matrix, Vector vector, Vector dest )
	{
		if ( dest == null )
		{
			dest = new Vector( matrix.rows() );
		}

		dest.set( multiply( matrix.getValues(), vector.values ) );
		return dest;
	}

	public static Therm[] multiply( Therm[][] matrix, Therm[] vector )
	{
		if ( vector.length != matrix[0].length )
		{
			throw new ArithmeticException( "The multiply of these 2 matrix is not possible" );
		}

		Therm[] result = new Therm[vector.length];

		for ( int y = 0 ; y < matrix.length ; y++ )
		{
			List<Therm> additional = new ArrayList<>();
			for ( int i = 0 ; i < vector.length ; i++ )
			{
				additional.add( vector[i].mul( matrix[y][i] ) );
			}
			result[y] = new Additional( additional );
		}
		return result;
	}

	public Vector scale( Const scale )
	{
		return scale( scale, this );
	}

	public Vector scale( Const scale, Vector dest )
	{
		return scale( this, scale, dest );
	}

	public static Vector scale( Vector vector, Const scale, Vector dest )
	{
		if ( dest == null )
		{
			dest = new Vector( vector.size() );
		}

		for ( int y = 0 ; y < dest.values.length ; y++ )
		{
			dest.values[y] = vector.values[y].mul( scale );
		}
		return dest;
	}

	public Therm lengthSquared()
	{
		return dot( this );
	}

	public Therm length()
	{
		return lengthSquared().pow( new Const( 0.5 ) );
	}

	@Override
	public void add( int fromRow, int toRow, Therm factor )
	{
		values[toRow] = values[toRow].add( values[fromRow].mul( factor ) );
	}

	@Override
	public void mul( int row, Therm factor )
	{
		values[row] = values[row].mul( factor );
	}

	@Override
	public Therm derivate( Variable name )
	{
		Therm[] newTherms = new Therm[size()];
		for ( int i = 0 ; i < newTherms.length ; i++ )
		{
			newTherms[i] = values[i].derivate( name );
		}
		return new Vector( newTherms );
	}

	@Override
	public double reduce( VarSet varSet )
	{
		return 0;
	}

	@Override
	public Therm replace( Therm replacer, Therm replacement )
	{
		return null;
	}

	@Override
	public Therm simplify()
	{
		Therm[] therms = new Therm[size()];

		for ( int i = 0 ; i < size() ; i++ )
		{
			therms[i] = values[i].simplify();
		}
		
		return new Vector( therms );
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		builder.append( Arrays.toString( this.values ).replaceAll( "\\[", "{" ).replaceAll( "\\]", "}" ));
	}
	
	@Override
	public int getLevel()
	{
		return FUNCTION_LEVEL;
	}
}

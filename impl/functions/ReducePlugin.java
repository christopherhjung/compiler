package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import parser.EnginePlugin;
import parser.Statement;
import tools.Utils;

public class ReducePlugin extends EnginePlugin {

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "reduce" ) )
		{
			return reduce( (Statement) params[0] );
		}

		return super.handle( key, params );
	}

	@Override
	public String getName()
	{
		return "function.name.reduce";
	}

	private Statement reduce( Statement therm )
	{
		if ( therm.is( "add" ) )
		{
			Statement left = therm.get( "left", Statement.class );
			Statement right = therm.get( "right", Statement.class );

			Statement temp = addReduce( reduce( left ), reduce( right ) );

			if ( temp != null )
			{
				return temp;
			}

		}
		else if ( therm.is( "sub" ) )
		{
			Statement left = therm.get( "left", Statement.class );
			Statement right = therm.get( "right", Statement.class );

			Statement temp = subReduce( reduce( left ), reduce( right ) );

			if ( temp != null )
			{
				return temp;
			}
			/*
			 * List<Therm> list = fetchElements( therm, t -> t.is( "add" ) );
			 * 
			 * 
			 * 
			 * List<Therm> reducedTherms = new ArrayList<>();
			 * 
			 * loop: for ( int i = 0 ; i < list.size() ; i++ ) { Therm current =
			 * reduce( list.get( i ) );
			 * 
			 * for ( int j = 0 ; j < reducedTherms.size() ; j++ ) { Therm
			 * reducedTherm = addReduce( reducedTherms.get( j ), current );
			 * 
			 * if ( reducedTherm != null ) { reducedTherms.set( j, reducedTherm
			 * ); continue loop; } else { reducedTherm = addReduce( current,
			 * reducedTherms.get( j ) );
			 * 
			 * if ( reducedTherm != null ) { reducedTherms.set( j, reducedTherm
			 * ); continue loop; } } }
			 * 
			 * reducedTherms.add( current ); } System.out.println( reducedTherms
			 * ); return alternatingEval( reducedTherms, "+" );
			 */
		}
		else if ( therm.is( "mul" ) )
		{
			Statement left = therm.get( "left", Statement.class );
			Statement right = therm.get( "right", Statement.class );

			Statement temp = mulReduce( reduce( left ), reduce( right ) );

			if ( temp != null )
			{
				return temp;
			}
		}
		else if ( therm.is( "exponent" ) )
		{
			Statement basis = therm.get( "left", Statement.class );
			Statement exponent = therm.get( "right", Statement.class );

			return powReduce( reduce( basis ), reduce( exponent ) );
		}
		else if ( therm.is( "negate" ) )
		{
			Statement inner = therm.get( "value", Statement.class );
			return eval( "-", reduce( inner ) );
		}
		else if ( therm.is( "divide" ) )
		{
			Statement numerators = therm.get( "left", Statement.class );
			Statement denominators = therm.get( "right", Statement.class );

			return divReduce( reduce( numerators ), reduce( denominators ) );
		}
		else if ( therm.is( "method" ) )
		{
			Statement[] params = therm.get( "params", Statement[].class );
			Statement value = therm.get( "value", Statement.class );

			value = reduce( value );

			List<Object> list = new ArrayList<>();
			list.add( "(" );
			Utils.alternating( params, ",", o -> list.add( o ) );
			list.add( ")->" );
			list.add( value );

			return eval( list );
		}

		return therm;
	}

	private Statement divReduce( Statement left, Statement right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			int leftValue = left.get( "value", Integer.class );
			int rightValue = right.get( "value", Integer.class );

			return eval( leftValue / rightValue );
		}

		if ( left.is( "exponent" ) && right.is( "variable" ) )
		{
			Statement basis = left.get( "left", Statement.class );

			if ( equals( basis, right ) )
			{
				Statement exponent = left.get( "right", Statement.class );
				return eval( basis, "^(", addReduce( exponent, eval( -1 ) ), ")" );
			}
		}

		return eval( left, "/", right );
	}

	private Statement powReduce( Statement basis, Statement exponent )
	{
		if ( exponent.is( "const" ) )
		{
			Integer exponentValue = exponent.get( "value", Integer.class );
			if ( basis.is( "const" ) )
			{
				Integer basisValue = basis.get( "value", Integer.class );
				return eval( Math.pow( basisValue, exponentValue ) );
			}
			else if ( exponentValue == 1 )
			{
				return basis;
			}
			else if ( exponentValue == 0 )
			{
				return eval( 1 );
			}
		}

		return eval( basis, "^", exponent );
	}

	private Statement mulReduce( Statement left, Statement right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			int leftValue = left.get( "value", Integer.class );
			int rightValue = right.get( "value", Integer.class );

			return eval( leftValue * rightValue );
		}

		if ( left.is( "exponent" ) && right.is( "variable" ) )
		{
			Statement basis = left.get( "left", Statement.class );

			if ( equals( basis, right ) )
			{
				Statement exponent = left.get( "right", Statement.class );
				return eval( basis, "^(", addReduce( exponent, eval( 1 ) ), ")" );
			}
		}


		if ( left.is( "const" ) )
		{
			int leftValue = left.get( "value", Integer.class );
			if ( leftValue == 0 )
			{
				return left;
			}
			else if ( leftValue == 1 )
			{
				return right;
			}
		}

		if ( right.is( "const" ) )
		{
			int rightValue = right.get( "value", Integer.class );
			if ( rightValue == 0 )
			{
				return right;
			}
			else if ( rightValue == 1 )
			{
				return left;
			}
		}

		return eval( left, "*", right );
	}

	private Statement subReduce( Statement left, Statement right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			int leftValue = left.get( "value", Integer.class );
			int rightValue = right.get( "value", Integer.class );

			return eval( leftValue - rightValue );
		}

		return null;
	}
	
	private Statement addReduce( Statement left, Statement right )
	{
		Statement result = addReduceImpl( left, right );
		if ( result == null )
		{
			result = addReduceImpl( right, left );
		}

		return result;
	}

	private Statement addReduceImpl( Statement left, Statement right )
	{
		if ( left.is( "const" ) )
		{
			if ( right.is( "const" ) )
			{
				int leftValue = left.get( "value", Integer.class );
				int rightValue = right.get( "value", Integer.class );

				return eval( leftValue + rightValue );
			}
			else if ( left.get( "value", Integer.class ) == 0 )
			{
				return right;
			}
		}

		if ( equals( left, right ) )
		{
			return eval( "2*", left );
		}

		if ( left.is( "add" ) )
		{
			Statement tempLeft = left.get( "left", Statement.class );
			Statement tempRight = left.get( "right", Statement.class );

			Statement result = switching( tempLeft, tempRight, ( a, b ) ->
			{
				Statement temp = addReduce( right, a );
				
				if ( temp != null )
				{
					return eval( temp, "+", tempRight );
				}
				
				return null;
			} );

			if ( result != null )
			{
				return result;
			}
		}

		if ( left.is( "mul" ) )
		{
			Statement mulLeft = left.get( "left", Statement.class );
			Statement mulRight = left.get( "right", Statement.class );

			Statement result = switching( mulLeft, mulRight, ( a, b ) ->
			{
				if ( equals( a, right ) && b.is( "const" ) )
				{
					return eval( addReduce( b, eval( 1 ) ), "*", a );
				}

				return null;
			} );

			if ( result != null )
			{
				return result;
			}

			/*
			 * if ( equals( mulLeft, right ) && mulRight.is( "const" ) ) {
			 * return eval( addReduce( mulRight, eval( 1 ) ), "*", mulLeft ); }
			 * else if ( equals( mulRight, right ) && mulLeft.is( "const" ) ) {
			 * return eval( addReduce( mulLeft, eval( 1 ) ), "*", mulRight ); }
			 */
		}

		if ( right.is( "sub" ) )
		{
			Statement tempLeft = right.get( "left", Statement.class );
			Statement tempRight = right.get( "right", Statement.class );

			Statement temp = addReduce( left, tempLeft );

			if ( temp != null )
			{
				return eval( temp, "-", tempRight );
			}
			else
			{
				temp = subReduce( left, tempRight );
				if ( temp != null )
				{
					return eval( tempLeft, "+", temp );
				}
			}
		}

		return null;
	}

	private <T> T switching( T left, T right, BiFunction<T, T, T> function )
	{
		T result = function.apply( left, right );
		if ( result == null )
		{
			result = function.apply( right, left );
		}

		return result;
	}

	public static boolean equals( Object leftObj, Object rightObj )
	{
		if ( !(leftObj instanceof Statement) )
		{
			if ( rightObj instanceof Statement )
			{
				return false;
			}
			else
			{
				return leftObj.equals( rightObj );
			}
		}

		Statement left = (Statement) leftObj;
		Statement right = (Statement) rightObj;

		String type = right.get( "type", String.class );
		if ( !left.is( type ) )
		{
			return false;
		}

		Object leftValue = left.get( "value" );
		Object rightValue = right.get( "value" );

		if ( leftValue != null && rightValue != null )
		{
			return equals( leftValue, rightValue );
		}

		Statement leftLeft = left.get( "left", Statement.class );
		Statement leftRight = left.get( "right", Statement.class );
		Statement rightLeft = right.get( "left", Statement.class );
		Statement rightRight = right.get( "right", Statement.class );

		if ( leftLeft != null && leftRight != null && rightLeft != null && rightRight != null )
		{
			return equals( leftRight, rightRight ) && equals( leftLeft, rightLeft );
		}

		return false;
	}
}
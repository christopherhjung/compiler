package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathProgram;
import therms.Therm;
import tools.ListComparer;
import tools.ReflectionUtils;
import tools.Utils;

public class ReducePlugin extends EnginePlugin {

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "reduce" ) )
		{
			return reduce( (Therm) params[0] );
		}

		return super.handle( key, params );
	}

	@Override
	public String getName()
	{
		return "function.variable.reduce";
	}

	private Therm reduce( Therm therm )
	{
		if ( therm.is( "add" ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			Therm temp = addReduce( reduce( left ), reduce( right ) );

			if ( temp != null )
			{
				return temp;
			}

		}
		else if ( therm.is( "sub" ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			Therm temp = subReduce( reduce( left ), reduce( right ) );

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
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			Therm temp = mulReduce( reduce( left ), reduce( right ) );

			if ( temp != null )
			{
				return temp;
			}
		}
		else if ( therm.is( "exponent" ) )
		{
			Therm basis = therm.get( "left", Therm.class );
			Therm exponent = therm.get( "right", Therm.class );

			return powReduce( reduce( basis ), reduce( exponent ) );
		}
		else if ( therm.is( "negate" ) )
		{
			Therm inner = therm.get( "value", Therm.class );
			return eval( "-", reduce( inner ) );
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "left", Therm.class );
			Therm denominators = therm.get( "right", Therm.class );

			return divReduce( reduce( numerators ), reduce( denominators ) );
		}
		else if ( therm.is( "method" ) )
		{
			Therm[] params = therm.get( "params", Therm[].class );
			Therm value = therm.get( "value", Therm.class );

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

	private Therm divReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue / rightValue );
		}

		if ( left.is( "exponent" ) && right.is( "variable" ) )
		{
			Therm basis = left.get( "left", Therm.class );

			if ( equals( basis, right ) )
			{
				Therm exponent = left.get( "right", Therm.class );
				return eval( basis, "^(", addReduce( exponent, eval( -1 ) ), ")" );
			}
		}

		return eval( left, "/", right );
	}

	private Therm powReduce( Therm basis, Therm exponent )
	{
		if ( exponent.is( "const" ) )
		{
			Double exponentValue = exponent.get( "value", Double.class );
			if ( basis.is( "const" ) )
			{
				Double basisValue = basis.get( "value", Double.class );
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

	private Collection<Therm> split( Therm therm, String method )
	{
		if ( therm.is( method ) )
		{
			return (List<Therm>) therm.get( "value" );
		}
		else
		{
			return Collections.singleton( therm );
		}
	}

	private Therm mulReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue * rightValue );
		}

		if ( left.is( "exponent" ) && right.is( "variable" ) )
		{
			Therm basis = left.get( "left", Therm.class );

			if ( equals( basis, right ) )
			{
				Therm exponent = left.get( "right", Therm.class );
				return eval( basis, "^(", addReduce( exponent, eval( 1 ) ), ")" );
			}
		}


		if ( left.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
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
			double rightValue = right.get( "value", Double.class );
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

	private Therm alternatingEval( List<Therm> therms, String limiter )
	{
		List<Object> objs = new ArrayList<>( therms );

		for ( int i = objs.size() - 1 ; i > 0 ; i-- )
		{
			objs.add( i, limiter );
		}

		return eval( objs );
	}

	private Therm subReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue - rightValue );
		}

		return null;
	}
	
	private Therm addReduce( Therm left, Therm right )
	{
		Therm result = addReduceImpl( left, right );
		if ( result == null )
		{
			result = addReduceImpl( right, left );
		}

		return result;
	}

	private Therm addReduceImpl( Therm left, Therm right )
	{
		if ( left.is( "const" ) )
		{
			if ( right.is( "const" ) )
			{
				double leftValue = left.get( "value", Double.class );
				double rightValue = right.get( "value", Double.class );

				return eval( leftValue + rightValue );
			}
			else if ( left.get( "value", Double.class ) == 0 )
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
			Therm tempLeft = left.get( "left", Therm.class );
			Therm tempRight = left.get( "right", Therm.class );

			Therm result = switching( tempLeft, tempRight, ( a, b ) ->
			{
				Therm temp = addReduce( right, a );
				
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
			Therm mulLeft = left.get( "left", Therm.class );
			Therm mulRight = left.get( "right", Therm.class );

			Therm result = switching( mulLeft, mulRight, ( a, b ) ->
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
			Therm tempLeft = right.get( "left", Therm.class );
			Therm tempRight = right.get( "right", Therm.class );

			Therm temp = addReduce( left, tempLeft );

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
		if ( !(leftObj instanceof Therm) )
		{
			if ( rightObj instanceof Therm )
			{
				return false;
			}
			else
			{
				return leftObj.equals( rightObj );
			}
		}

		Therm left = (Therm) leftObj;
		Therm right = (Therm) rightObj;

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

		Therm leftLeft = left.get( "left", Therm.class );
		Therm leftRight = left.get( "right", Therm.class );
		Therm rightLeft = right.get( "left", Therm.class );
		Therm rightRight = right.get( "right", Therm.class );

		if ( leftLeft != null && leftRight != null && rightLeft != null && rightRight != null )
		{
			return equals( leftRight, rightRight ) && equals( leftLeft, rightLeft );
		}

		return false;
	}
}
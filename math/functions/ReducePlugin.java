package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathProgram;
import therms.Therm;
import tools.ListComparer;
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

	private List<Therm> fetchElements( Therm therm, String type )
	{
		List<Therm> list = new ArrayList<>();
		fetchElements( therm, type, list );
		return list;
	}

	private void fetchElements( Therm therm, String type, List<Therm> therms )
	{
		if ( therm.is( type ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );
			fetchElements( left, type, therms );
			fetchElements( right, type, therms );
		}
		else
		{
			therms.add( therm );
		}
	}

	private Therm reduce( Therm therm )
	{
		if ( therm.is( "add" ) )
		{
			List<Therm> list = fetchElements();

			List<Therm> reducedTherms = new ArrayList<>();

			loop: for ( int i = 0 ; i < list.size() ; i++ )
			{
				Therm current = reduce( list.get( i ) );

				for ( int j = 0 ; j < reducedTherms.size() ; j++ )
				{
					Therm reducedTherm = addReduce( reducedTherms.get( j ), current );

					if ( reducedTherm != null )
					{
						reducedTherms.set( j, reducedTherm );
						continue loop;
					}
					else
					{
						reducedTherm = addReduce( current, reducedTherms.get( j ) );

						if ( reducedTherm != null )
						{
							reducedTherms.set( j, reducedTherm );
							continue loop;
						}
					}
				}

				reducedTherms.add( current );
			}
			System.out.println( reducedTherms );
			return alternatingEval( reducedTherms, "+" );
		}
		else if ( therm.is( "mul" ) )
		{
			List<Therm> list = (List<Therm>) therm.execute( "value" );

			Therm result = reduce( list.get( 0 ) );

			for ( int i = 1 ; i < list.size() ; i++ )
			{
				result = mulReduce( result, reduce( (Therm) list.get( i ) ) );
			}

			return result;
		}
		else if ( therm.is( "exponent" ) )
		{
			Therm basis = therm.get( "left", Therm.class );
			Therm exponent = therm.get( "right", Therm.class );

			Therm reducedBasis = reduce( basis );
			Therm reducedExponent = reduce( exponent );

			return powReduce( reducedBasis, reducedExponent );
		}
		else if ( therm.is( "negate" ) )
		{
			Therm inner = therm.get( "value", Therm.class );
			inner = reduce( inner );

			return eval( "-", inner );
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "left", Therm.class );
			Therm denominators = therm.get( "right", Therm.class );

			numerators = reduce( numerators );
			denominators = reduce( denominators );

			return divReduce( numerators, denominators );
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
			return (List<Therm>) therm.execute( "value" );
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

		if ( left.is( "mul" ) || right.is( "mul" ) )
		{
			List<Therm> newOne = new ArrayList<>();

			newOne.addAll( split( left, "mul" ) );
			newOne.addAll( split( right, "mul" ) );

			return alternatingEval( newOne, "*" );
		}

		if ( left.is( "add" ) || right.is( "add" ) )
		{
			Collection<Therm> leftElements = split( left, "add" );
			Collection<Therm> rightElements = split( right, "add" );

			List<Therm> therms = new ArrayList<>();
			for ( Therm leftInner : leftElements )
			{
				for ( Therm rightInner : rightElements )
				{
					therms.add( mulReduce( leftInner, rightInner ) );
				}
			}

			return alternatingEval( therms, "+" );
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

	private Therm addReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue + rightValue );
		}

		if ( left.is( "add" ) || right.is( "add" ) )
		{
			List<Therm> newOne = new ArrayList<>();

			newOne.addAll( split( left, "add" ) );
			newOne.addAll( split( right, "add" ) );

			return alternatingEval( newOne, "+" );
		}

		if ( left.is( "const" ) )
		{
			if ( left.get( "value", Double.class ) == 0 )
			{
				return right;
			}
		}

		return null;
	}

	public static boolean equals( Therm left, Therm right )
	{
		String type = right.get( "type", String.class );
		if ( !left.is( type ) )
		{
			return false;
		}

		if ( left.is( "const" ) || left.is( "variable" ) )
		{
			return left.execute( "value" ).equals( right.execute( "value" ) );
		}
		else if ( left.is( "negate" ) )
		{
			return equals( (Therm) left.execute( "value" ), (Therm) right.execute( "value" ) );
		}

		if ( left.is( "add" ) || left.is( "mul" ) )
		{
			List<Therm> leftTherms = (List<Therm>) left.execute( "value" );
			List<Therm> rightTherms = (List<Therm>) right.execute( "value" );

			return ListComparer.containsSame( leftTherms, rightTherms, ( a, b ) -> equals( a, b ) ? 0 : 1 );
		}

		if ( left.is( "exponent" ) )
		{
			Therm leftExponent = left.get( "exponent", Therm.class );
			Therm leftBasis = left.get( "basis", Therm.class );
			Therm rightExponent = right.get( "exponent", Therm.class );
			Therm rightBasis = right.get( "basis", Therm.class );

			return equals( leftExponent, rightExponent ) && equals( leftBasis, rightBasis );
		}

		return false;
	}
}
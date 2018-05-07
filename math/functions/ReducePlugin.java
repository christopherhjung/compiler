package functions;

import java.util.List;

import therms.Therm;

public class ReducePlugin extends EnginePlugin {

	@EngineExecute
	public Therm execute( Therm therm )
	{
		/*
		 * Therm result = (Therm) therm.execute( "reduce" );
		 * 
		 * if ( result == null ) { return therm; }
		 */

		Therm result = get( "reduce", therm );

		return result;
	}

	public Therm get( String event, Therm... therms )
	{
		if ( event.equals( "reduce" ) )
		{
			if ( therms.length == 1 )
			{
				Therm result = reduce( therms[0] );

				return result;
			}
			else
			{
				// fail
			}
		}
		else if ( event.equals( "mulreduce" ) )
		{
			if ( therms.length == 2 )
			{
				return mulReduce( therms[0], therms[1] );
			}
			else
			{
				// fail
			}
		}
		else if ( event.equals( "addreduce" ) )
		{
			if ( therms.length == 2 )
			{
				return addReduce( therms[0], therms[1] );
			}
			else
			{
				// fail
			}
		}

		return null;
	}

	private Therm reduce( Therm therm )
	{
		if ( therm.is( "add" ) )
		{
			List<Therm> list = (List<Therm>) therm.execute( "value" );

			Therm result = reduce( list.get( 0 ) );

			for ( int i = 1 ; i < list.size() ; i++ )
			{
				result = addReduce( result, reduce( (Therm) list.get( i ) ) );
			}

			return result;
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
			Therm basis = therm.get( "basis", Therm.class );
			Therm exponent = therm.get( "exponent", Therm.class );

			Therm reducedBasis = reduce( basis );
			Therm reducedExponent = reduce( exponent );

			return powReduce( reducedBasis, reducedExponent );
		}
		else if ( therm.is( "negate" ) )
		{
			Therm inner = therm.get( "value", Therm.class );
			inner = reduce( inner );

			if ( inner.is( "const" ) )
			{
				Double value = inner.get( "value", Double.class );

				if ( value < 0 )
				{
					return eval( -value );
				}
				else
				{
					return eval( value );
				}
			}

			return eval( "-", inner );
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "numerators", Therm.class );
			Therm denominators = therm.get( "denominators", Therm.class );

			numerators = reduce( numerators );
			denominators = reduce( denominators );

			return divReduce( numerators, denominators );
		}
		else if ( therm.is( "method" ) )
		{
			Therm value = therm.get( "value", Therm.class );
			Therm param = therm.get( "param", Therm.class );

			value = reduce( value );

			return eval( param,"->" ,value );
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
				return eval( 0 );
			}
		}

		return eval( basis, "^", exponent );
	}

	private Therm mulReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue * rightValue );
		}
		else if ( left.is( "const" ) )
		{
			if ( left.get( "value", Double.class ) == 0 )
			{
				return left;
			}
		}
		else if ( right.is( "const" ) )
		{
			if ( right.get( "value", Double.class ) == 0 )
			{
				return right;
			}
		}

		return eval( left, "*", right );
	}

	private Therm addReduce( Therm left, Therm right )
	{
		if ( left.is( "const" ) && right.is( "const" ) )
		{
			double leftValue = left.get( "value", Double.class );
			double rightValue = right.get( "value", Double.class );

			return eval( leftValue + rightValue );
		}
		else if ( left.is( "const" ) )
		{
			if ( left.get( "value", Double.class ) == 0 )
			{
				return right;
			}
		}
		else if ( right.is( "const" ) )
		{
			if ( right.get( "value", Double.class ) == 0 )
			{
				return left;
			}
		}

		return eval( left, "+", right );
	}

}
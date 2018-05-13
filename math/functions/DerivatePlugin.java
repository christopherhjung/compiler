package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.EngineExecute;
import parser.EnginePlugin;
import parser.ParseException;
import therms.Therm;

public class DerivatePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function.variable.derivate";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "derivate" ) )
		{
			if ( params.length == 1 )
			{
				return execute( (Therm) params[0] );
			}
			else if ( params.length == 2 )
			{
				return execute( (Therm) params[0], (Therm) params[1] );
			}
		}

		return super.handle( key, params );
	}

	public Therm execute( Therm therm )
	{
		Therm method = getEngine().currentScope.get( therm );

		if ( method != null && method.is( "method" ) )
		{
			return execute( method.get( "value", Therm.class ), (Therm)((Object[])method.execute( "params" ))[0] );
		}

		return null;
	}

	public Therm execute( Therm therm, Therm var )
	{
		if ( !var.is( "variable" ) )
		{
			return null;
		}

		return eval( var, "->", derivate( therm, var ) );
	}

	public Therm derivate( Therm therm, Therm var )
	{
		if ( therm.is( "add" ) )
		{
			List<Therm> list = (List<Therm>) therm.execute( "value" );

			List<Object> builder = new ArrayList<>();
			for ( Therm element : list )
			{
				if ( builder.size() > 0 )
				{
					builder.add( '+' );
				}

				builder.add( derivate( element, var ) );
			}

			return eval( builder );
		}
		else if ( therm.is( "mul" ) )
		{
			List<Therm> list = (List<Therm>) therm.execute( "value" );

			ArrayList<Object> builder = new ArrayList<>();
			for ( int i = 0 ; i < list.size() ; i++ )
			{
				if ( i > 0 )
				{
					builder.add( '+' );
				}

				for ( int j = 0 ; j < list.size() ; j++ )
				{
					if ( j > 0 )
					{
						builder.add( '*' );
					}

					Therm temp = list.get( j );

					if ( i == j )
					{
						temp = derivate( temp, var );
					}

					builder.add( temp );
				}
			}

			return eval( builder );
		}
		else if ( therm.is( "const" ) )
		{
			return eval( 0 );
		}
		else if ( therm.is( "exponent" ) )
		{
			// return eval( this, "*", "derivate(", exponent, "*", "log(",
			// basis, "),", Utils.concat( params, "," ), ")" );

			Therm basis = (Therm) therm.execute( "basis" );
			Therm exponent = (Therm) therm.execute( "exponent" );

			return eval( exponent, "*", basis, "^(", exponent, "-1)" );
		}
		else if ( therm.is( "variable" ) )
		{
			String name = therm.get( "value", String.class );

			String derivateVar = var.get( "value", String.class );
			return eval( derivateVar.equals( name ) ? 1 : 0 );
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "numerators", Therm.class );
			Therm denominators = therm.get( "denominators", Therm.class );

			return eval( "(", denominators, "*", derivate( numerators, var ), "-", numerators, "*",
					derivate( denominators, var ), ")/", denominators, "^2" );
		}

		return (Therm) handle( "derivate", therm, var );
	}
}

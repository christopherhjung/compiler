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
		therm = eval( "update(", therm, ")" );

		if ( therm != null && therm.is( "method" ) )
		{
			return execute( therm.get( "value", Therm.class ), (Therm) ((Object[]) therm.execute( "params" ))[0] );
		}

		return (Therm) super.handle( "derivate", therm );
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
			Therm basis = (Therm) therm.execute( "left" );
			Therm exponent = (Therm) therm.execute( "right" );

			if ( exponent.is( "const" ) )
			{
				return eval( exponent, "*", basis, "^(", exponent, "-1)" );
			}
			else
			{
				return eval( therm, "*", derivate( eval( exponent, "*log(", basis, ")" ), var ) );
			}
		}
		else if ( therm.is( "variable" ) )
		{
			String name = therm.get( "value", String.class );

			String derivateVar = var.get( "value", String.class );
			return eval( derivateVar.equals( name ) ? 1 : 0 );
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "left", Therm.class );
			Therm denominators = therm.get( "right", Therm.class );

			return eval( "(", denominators, "*", derivate( numerators, var ), "-", numerators, "*",
					derivate( denominators, var ), ")/", denominators, "^2" );
		}
		else if ( therm.is( "chain" ) )
		{
			Therm[] params = therm.get( "params", Therm[].class );
			Therm method = therm.get( "method", Therm.class );

			return eval( derivate( params[0], var ), "*", execute( method ), "(", params[0], ")" );
		}

		return (Therm) super.handle( "derivate", therm, var );
	}
}

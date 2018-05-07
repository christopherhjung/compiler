package functions;

import java.util.ArrayList;
import java.util.List;

import parser.ParseException;
import therms.Therm;

public class DerivatePlugin extends EnginePlugin {

	@EngineExecute
	public Therm execute( Therm therm ){
		Therm method = getEngine().globalScope.get( therm );
		
		if ( method != null && method.is( "method" ) )
		{
			return execute( method.get( "value", Therm.class ), method.get( "param", Therm.class ) );
		}
		
		return null;
	}
	
	@EngineExecute
	public Therm execute( Therm therm, Therm var )
	{
		if ( !var.is( "variable" ) )
		{
			return null;
		}

		return eval(var,"->",derivate( therm, var ));
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
			if ( derivateVar.equals( name ) )
			{
				return eval( 1 );
			}
			else
			{
				return eval( 0 );
			}
		}
		else if ( therm.is( "divide" ) )
		{
			Therm numerators = therm.get( "numerators", Therm.class );
			Therm denominators = therm.get( "denominators", Therm.class );

			return eval( "(", denominators, "*", derivate( numerators, var ), "-", numerators, "*",
					derivate( denominators, var ), ")/", denominators, "^2" );
		}

		throw new ParseException( "Unknow derivate to" + therm.execute( "type" ) );
	}
}

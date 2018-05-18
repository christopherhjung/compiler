package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import parser.EnginePlugin;
import parser.Space.Scope;
import therms.Therm;
import tools.Run;
import tools.Utils;

public class UpdatePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function.variable.update";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "update" ) && params[0] != null )
		{
			return update( (Therm) params[0] );
		}

		return super.handle( key, params );
	}

	private Therm update( Therm therm )
	{
		if ( therm.is( "assignment" ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			Therm newRight = update( right );
			left.execute( "assign", newRight );

			return newRight;
		}

		if ( therm.is( "method" ) )
		{

			Therm[] params = therm.get( "params", Therm[].class );
			Therm inner = therm.get( "value", Therm.class );

			Set<String> varSet = new HashSet<>();
			for ( Therm param : params )
			{
				if ( param.is( "variable" ) )
				{
					varSet.add( param.get( "value", String.class ) );
				}
			}

			getEngine().enterScope( new Scope( getEngine().currentScope ) {
				@Override
				public Therm get( Object key )
				{
					if ( varSet.contains( key ) )
					{
						return (Therm) key;
					}

					return super.get( key );
				}
			} );

			List<Object> objs = new ArrayList<>();
			objs.add( "(" );
			Utils.alternating( params, ",", o -> objs.add( o ) );
			objs.add( ")->" );
			objs.add( update( inner ) );

			getEngine().leaveScope();

			return eval( objs );
		}

		if ( therm.is( "chain" ) )
		{
			Therm method = therm.get( "method", Therm.class );
			Therm[] params = therm.get( "params", Therm[].class );

			return Run.safe( () ->
			{
				Therm[] updatedTherms = new Therm[params.length];
				for ( int i = 0 ; i < params.length ; i++ )
				{
					updatedTherms[i] = update( params[i] );
				}

				Therm result = (Therm) method.execute( "call", updatedTherms );
				if ( result == null )
				{
					List<Object> objs = new ArrayList<>();
					objs.add( update( method ) );
					objs.add( "(" );
					Utils.alternating( updatedTherms, ",", o -> objs.add( o ) );
					objs.add( ")" );
					return eval( objs );
				}

				return update( result );
			}, therm );
		}

		if ( therm.is( "variable" ) )
		{
			Therm result = getEngine().currentScope.get( therm );

			if ( result == null )
			{
				throw new RuntimeException( "Variable " + therm.get( "value", String.class ) + " not found" );
			}

			return result;
		}

		if ( therm.is( "add" ) )
		{
			List<Therm> therms = (List<Therm>) therm.execute( "value" );
			return eval( therms.stream().map( e -> update( e ) ).collect( Utils.alternatingCollector( "+" ) ) );
		}

		if ( therm.is( "mul" ) )
		{
			List<Therm> therms = (List<Therm>) therm.execute( "value" );
			return eval( therms.stream().map( e -> update( e ) ).collect( Utils.alternatingCollector( "*" ) ) );
		}

		if ( therm.is( "exponent" ) )
		{
			Therm basis = therm.get( "left", Therm.class );
			Therm exponent = therm.get( "right", Therm.class );
			return eval( update( basis ), "^", update( exponent ) );
		}

		if ( therm.is( "negate" ) )
		{
			Therm inner = therm.get( "value", Therm.class );
			return eval( "-", update( inner ) );
		}

		if ( therm.is( "divide" ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			left = update( left );
			right = update( right );

			return eval( left, "/", right );
		}

		return therm;
	}
}

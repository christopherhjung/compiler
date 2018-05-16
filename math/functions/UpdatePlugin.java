package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import parser.EnginePlugin;
import therms.Therm;
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
		if ( key.equals( "update" ) )
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

			System.out.println( right );
			Therm newRight = update( right );
			left.execute( "assign", newRight );
			//eval("assign(",left,",",newRight,")")
			
			return newRight;
		}

		if ( therm.is( "chain" ) )
		{
			Therm method = therm.get( "method", Therm.class );
			Therm[] params = therm.get( "params", Therm[].class );
			return (Therm) method.execute( "call", params );
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
			return eval( therms.stream().map( e -> update(e) ).collect( Utils.alternatingCollector( "+" ) ) );
		}

		if ( therm.is( "mul" ) )
		{
			List<Therm> therms = (List<Therm>) therm.execute( "value" );
			return eval( therms.stream().map( e -> update(e) ).collect( Utils.alternatingCollector( "*" ) ) );
		}

		if ( therm.is( "exponent" ) )
		{
			Therm basis = therm.get( "basis", Therm.class );
			Therm exponent = therm.get( "exponent", Therm.class );
			return eval( update( basis ), "^", update( exponent ) );
		}
		
		

		return therm;
	}
}

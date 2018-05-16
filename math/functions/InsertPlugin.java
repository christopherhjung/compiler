package functions;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Util;

import functions.FunctionPlugin.Chain;
import parser.EnginePlugin;
import therms.Therm;
import tools.Utils;

public class InsertPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function.variable.insert";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "insert" ) )
		{
			return insert( params[0] );
		}

		return params[0];
	}

	public Object insert( Object obj )
	{
		if ( obj instanceof Therm )
		{
			return insert( (Therm) obj );
		}

		return obj;
	}

	public Therm insert( Therm therm )
	{
		if ( therm.is( "varibale" ) )
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
			List<Object> list = new ArrayList<>();
			List<Therm> therms = (List<Therm>) therm.execute( "value" );

			Utils.alternating( therms, "+", o -> list.add( o ) );
			return eval( list );
		}

		if ( therm.is( "mul" ) )
		{
			List<Object> list = new ArrayList<>();
			List<Therm> therms = (List<Therm>) therm.execute( "value" );
			Utils.alternating( therms, "*", o -> list.add( o ) );

			return eval( list );
		}

		if ( therm.is( "exponent" ) )
		{
			Therm basis = therm.get( "basis", Therm.class );
			Therm exponent = therm.get( "exponent", Therm.class );
			return eval( insert( basis ), "^", insert( exponent ) );
		}

		if ( therm.is( "chain" ) )
		{
			Therm[] outer = (Therm[]) therm.get( "outer", Object.class );
			Therm inner = therm.get( "inner", Therm.class );

			List<Object> list = new ArrayList<>();
			list.add( "(" );
			list.add( inner );
			list.add( ")" );
			list.add( "(" );
			Utils.alternating( outer, ",", o -> list.add( insert( o ) ) );
			list.add( ")" );

			return eval( list );
		}

		if ( therm.is( "assignment" ) )
		{
			Therm left = therm.get( "left", Therm.class );
			Therm right = therm.get( "right", Therm.class );

			Therm newRight = insert( right );
			left.execute( "assign", newRight );
			return newRight;
		}

		return therm;
	}
}

package functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import parser.EnginePlugin;
import parser.Space.Scope;
import parser.Statement;
import tools.Run;
import tools.Utils;

public class UpdatePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function.name.update";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "update" ) && params[0] != null )
		{
			return update( (Statement) params[0] );
		}

		return super.handle( key, params );
	}

	private Statement update( Statement therm )
	{
		if ( therm.is( "assign" ) )
		{
			Statement left = therm.get( "left", Statement.class );
			Statement right = therm.get( "right", Statement.class );

			Statement newRight = update( right );
			left.get( "assign", newRight );

			return newRight;
		}

		if ( therm.is( "method" ) )
		{
			Statement[] params = therm.get( "params", Statement[].class );
			Statement inner = therm.get( "value", Statement.class );

			Set<String> varSet = new HashSet<>();
			for ( Statement param : params )
			{
				if ( param.is( "name" ) )
				{
					varSet.add( param.get( "value", String.class ) );
				}
			}

			getEngine().enterScope( new Scope( getEngine().currentScope ) {
				@Override
				public Statement get( Object key )
				{
					if ( varSet.contains( key ) )
					{
						return (Statement) key;
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
			Statement method = therm.get( "method", Statement.class );
			Statement[] params = therm.get( "params", Statement[].class );

			return Run.safe( () ->
			{
				Statement[] updatedTherms = new Statement[params.length];
				for ( int i = 0 ; i < params.length ; i++ )
				{
					updatedTherms[i] = update( params[i] );
				}

				Statement result = (Statement) method.get( "call", updatedTherms );
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

		if ( therm.is( "name" ) )
		{
			Statement result = getEngine().currentScope.get( therm );

			if ( result == null )
			{
				result = therm;
			}

			return result;
		}

		Statement left = therm.get( "left", Statement.class );
		Statement right = therm.get( "right", Statement.class );

		if ( left != null && right != null )
		{
			Statement newLeft = update(left);
			Statement newRight = update(right);
			if( left == newLeft && right == newRight ){
				return therm;
			}
			
			String linker = therm.get( "linker", String.class );
			return eval( newLeft, linker, newRight );
		}

		Statement inner = therm.get( "value", Statement.class );
		if ( inner != null )
		{
			Statement updated = update( inner );

			if ( inner != updated )
			{
				String pre = therm.get( "pre", String.class );
				String post = therm.get( "post", String.class );
				return eval( pre, updated, post );
			}
			return therm;
		}

		return therm;
	}
}

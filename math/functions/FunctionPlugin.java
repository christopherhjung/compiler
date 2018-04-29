package functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.MathParser;
import parser.MathProgram;
import parser.ParseException;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;

public class FunctionPlugin extends EnginePlugin {

	private HashMap<String, EnginePlugin> plugins = new HashMap<>();

	{
		plugins.put( "sin", new SinPlugin() );
		plugins.put( "reduce", new ReducePlugin() );
		plugins.put( "derivate", new DerivatePlugin() );
	}
	
	@Override
	public void onStart( MathProgram program )
	{
		plugins.values().forEach( plugin -> plugin.onStart( program ) );
	}

	@Override
	public Therm handle( MathParser parser )
	{
		Therm therm = null;

		parser.eat( ' ' );
		if ( parser.is( Character::isAlphabetic ))
		{
			StringBuilder builder = new StringBuilder();

			while ( parser.isAlpha() )
			{
				builder.append( parser.nextChar() );
			}

			String methodName = builder.toString();

			List<Therm> therms = new ArrayList<>();

			if ( parser.eat( '(' ) )
			{
				
				for ( ; parser.isNot( ')' ) ; )
				{
					Therm param = parser.parseWithLevelReset();
					therms.add( param );
					parser.eat( ',' ); 
				}

				parser.eat( ')' );
			}
			
			
			Class<?>[] classes = new Class<?>[therms.size()];
			Object[] thermsArr = new Therm[therms.size()];

			for ( int i = 0 ; i < therms.size() ; i++ )
			{
				thermsArr[i] = therms.get( i );
				classes[i] = thermsArr[i].getClass();
			}
			EnginePlugin function = plugins.get( methodName.toLowerCase() );

			if ( function != null )
			{
				List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith( function.getClass(),
						EngineExecute.class );
				Method method = ReflectionUtils.findBestMethod( methods, classes );
				therm = ReflectionUtils.safeInvoke( null, Therm.class, function, method, thermsArr );
			}

			if ( therm == null )
			{
				therm = new Variable( methodName );

				if ( thermsArr.length == 1 )
				{
					therm = therm.mul( (Therm) thermsArr[0] );
				}
				else if ( thermsArr.length > 1 )
				{
					throw new ParseException( parser );
				}
			}
		}

		return therm;
	}

}

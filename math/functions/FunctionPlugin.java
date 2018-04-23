package functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.MathParser;
import parser.ParseException;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;

public class FunctionPlugin extends EnginePlugin {

	private HashMap<String, EnginePlugin> plugins = new HashMap<>();

	{
		plugins.put( "sin", new SinPlugin() );
	}

	@Override
	public Therm handle( MathParser engine )
	{

		Therm therm = null;

		engine.eat( ' ' );
		if ( engine.getLevel() == 3 && engine.isAlpha() )
		{
			StringBuilder builder = new StringBuilder();

			while ( engine.isAlpha() )
			{
				builder.append( engine.nextChar() );
			}

			String methodName = builder.toString();

			List<Therm> therms = new ArrayList<>();

			if ( engine.eat( '(' ) )
			{
				int saveLevel = engine.getLevel();
				engine.setLevel( 1 );
				
				for ( ; engine.isNot( ')' ) ; )
				{
					Therm param = engine.parseTest();
					therms.add( param );
					engine.eat( ',' ); 
				}
				
				engine.setLevel( saveLevel );

				engine.eat( '(' );
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
				therm = ReflectionUtils.safeInvoke( Therm.class, null, method, function, thermsArr );
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
					throw new ParseException( engine );
				}
			}
		}

		return therm;
	}

}

package tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {

	public static Method findBestMethod( List<Method> methods, Class<?>[] types )
	{
		int bestMatches = -1;
		Method bestMatch = null;

		loop: for ( Method method : methods )
		{
			Class<?>[] methodTypes = method.getParameterTypes();

			if ( methodTypes.length == types.length )
			{
				int currentMatching = 0;
				for ( int i = 0 ; i < types.length ; i++ )
				{
					if ( !methodTypes[i].isAssignableFrom( types[i] ) )
					{
						continue loop;
					}

					if ( methodTypes[i].equals( types[i] ) ) currentMatching++;
				}

				if ( currentMatching > bestMatches )
				{
					bestMatches = currentMatching;
					bestMatch = method;
				}
			}
		}

		return bestMatch;
	}

	public static List<Method> getMethodsAnnotatedWith( final Class<?> type,
			final Class<? extends Annotation> annotation )
	{
		// ReflectionStream.of(type).methodsUntil(Object.class).hasAnnotation(annotation).collect(Collectors.toList())

		final List<Method> methods = new ArrayList<Method>();
		Class<?> clazz = type;
		while ( clazz != Object.class )
		{
			for ( final Method method : clazz.getDeclaredMethods() )
			{
				if ( method.isAnnotationPresent( annotation ) )
				{

					Annotation annotInstance = method.getAnnotation( annotation );

					// TODO process annotInstance
					methods.add( method );
				}
			}
			// move to the upper class in the hierarchy in search for more
			// methods
			clazz = clazz.getSuperclass();
		}
		return methods;
	}

	public static <T> T as( Object obj, Class<T> clazz )
	{
		if ( clazz.isInstance( obj ) )
		{
			return clazz.cast( obj );
		}

		return null;
	}

	public static <T> T safeInvoke( T defaultValue,  Class<T> clazz, Object obj, Method method, Object... args )
	{
		return Run.safe( () -> method == null ? null : as( method.invoke( obj, args ), clazz ), defaultValue );
	}
}

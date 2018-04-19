package tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import therms.Therm;

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

		if ( bestMatch == null ) throw new NoClassDefFoundError();

		return bestMatch;
	}
	
	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
	    //ReflectionStream.of(type).methodsUntil(Object.class).hasAnnotation(annotation).collect(Collectors.toList())
		
		final List<Method> methods = new ArrayList<Method>();
	    Class<?> klass = type;
	    while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
	        
	        for (final Method method : klass.getDeclaredMethods()) {
	            if (method.isAnnotationPresent(annotation)) {

	                Annotation annotInstance = method.getAnnotation(annotation);
	                
	                
	                // TODO process annotInstance
	                methods.add(method);
	            }
	        }
	        // move to the upper class in the hierarchy in search for more methods
	        klass = klass.getSuperclass();
	    }
	    return methods;
	}
}

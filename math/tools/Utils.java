package tools;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class Utils {

	public static <T> String concat( T[] elements, String limiter )
	{
		StringBuilder sb = new StringBuilder();
		return concat( sb, elements, limiter );
	}

	public static <T> String concat( StringBuilder sb, T[] elements, String limiter )
	{
		for ( int i = 0 ; i < elements.length ; i++ )
		{
			if ( i > 0 )
			{
				sb.append( limiter );
			}

			sb.append( elements[i] );
		}

		return sb.toString();
	}
	
	public static <T> T getOrInsert( int key, List<T> collection, IntFunction<T> generator ){
		T element = collection.get( key );
		
		if(element == null){
			element = generator.apply( key );
			collection.set( key, element );
		}
		
		return element;
	}
	
	public static <K,T> T getOrInsert( K key, Map<K,T> map, Function<K,T> generator ){
		T element = map.get( key );
		
		if(element == null){
			element = generator.apply( key );
			map.put( key, element );
		}
		
		return element;
	}
}

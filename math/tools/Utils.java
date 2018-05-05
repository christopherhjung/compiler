package tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

public class Utils {

	public static <T> Iterator<T> toIterator( T[] array )
	{
		return new Iterator<T>() {

			int position = 0;

			@Override
			public boolean hasNext()
			{
				return position < array.length;
			}

			@Override
			public T next()
			{
				return array[position];
			}
		};
	}

	public static <T> List<T> toList( Iterable<T> iterable )
	{
		List<T> list = new ArrayList<>();
		for ( T element : iterable )
		{
			list.add( element );
		}
		return list;
	}

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

	public static <T> T getOrInsert( int key, List<T> collection, IntFunction<T> generator )
	{
		T element = collection.get( key );

		if ( element == null )
		{
			element = generator.apply( key );
			collection.set( key, element );
		}

		return element;
	}

	public static <K, T> T getOrInsert( K key, Map<K, T> map, Function<K, T> generator )
	{
		T element = map.get( key );

		if ( element == null )
		{
			element = generator.apply( key );
			map.put( key, element );
		}

		return element;
	}
}

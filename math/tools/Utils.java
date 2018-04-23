package tools;

import java.util.function.Function;

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
}

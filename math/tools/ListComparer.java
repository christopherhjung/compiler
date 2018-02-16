package tools;

import java.util.List;

public class ListComparer {

	public static boolean containsSame( List<? extends Object> a, List<? extends Object> b )
	{
		if ( a.size() != b.size() )
		{
			return false;
		}

		int size = a.size();
		int pairs = 0;
		boolean[] used = new boolean[size];

		for ( int i = 0 ; i < size ; i++ )
		{
			for ( int j = 0 ; j < size ; j++ )
			{
				if( !used[j] && a.get( i ).equals( b.get( j ) ) ){
					used[j] = true;
					pairs++;
					break;
				}
			}
		}

		return pairs == size;
	}

}

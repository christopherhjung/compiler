package tools;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeSet;

public class ListComparer {

	public static <T> boolean containsSame( List<T> a, List<T> b, Comparator<T> comp )
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
				if( !used[j] && comp.compare(a.get( i ), b.get( j ) ) == 0 ){
					used[j] = true;
					pairs++;
					break;
				}
			}
		}

		return pairs == size;
	}
}

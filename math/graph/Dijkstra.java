package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dijkstra {

	public static double inf = Double.POSITIVE_INFINITY;

	public static void main( String[] args )
	{
		Graph graph = new MatrixGraph( new double[][] { 
			{ 0, 10, 5, inf, inf }, 
			{ inf, 0, 2, 1, inf },
			{ 3, 2, 0, 9, 2 }, 
			{ inf, inf, inf, 0, 4 }, 
			{ 7, inf, inf, 6, 0 } } );

		PathResult result = search( graph, 0 );

		System.out.println( result );
	}

	public static PathResult search( Graph graph, int start )
	{
		double dis[] = new double[graph.size()];
		int pre[] = new int[graph.size()];
		Heap<Integer> heap = new Heap<>();

		Arrays.fill( dis, Double.POSITIVE_INFINITY );
		Arrays.fill( pre, -1 );

		for ( int i = 0 ; i < graph.size() ; i++ )
		{
			heap.add( i, Double.POSITIVE_INFINITY );
		}

		dis[start] = 0.0;
		heap.changePriority( start, 0 );

		while ( !heap.isEmpty() )
		{
			int u = heap.pop();

			for ( int v = 0 ; v < graph.size() ; v++ )
			{
				double alternative = dis[u] + graph.getWeight( u, v );
				if ( dis[v] > alternative )
				{
					dis[v] = alternative;
					pre[v] = u;
					heap.changePriority( v, dis[v] );
				}
			}
		}

		return new PathResult() {

			@Override
			public Path getPath( int from, int to )
			{
				if ( from != start ) throw new RuntimeException( "nicht unterstützt" );

				List<Integer> list = new ArrayList<>();

				while ( to >= 0 )
				{
					list.add( 0, to );
					to = pre[to];
				}

				return new Path( list );
			}

			@Override
			public String toString()
			{
				return Arrays.toString( dis ) + " - " + Arrays.toString( pre );
			}
		};
	}

}

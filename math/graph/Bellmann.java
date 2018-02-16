package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bellmann {

	public static double inf = Double.POSITIVE_INFINITY;

	public static void main( String[] args )
	{
		Graph graph = new MatrixGraph( new double[][] { { 0, -1, inf, Double.POSITIVE_INFINITY },
				{ 10, 0, 1, Double.POSITIVE_INFINITY }, { 2, Double.POSITIVE_INFINITY, 0, -1 },
				{ Double.POSITIVE_INFINITY, 2, Double.POSITIVE_INFINITY, 0 } } );

		graph = new MatrixGraph( new double[][] { 
			{ 0, 6, 7, inf, inf }, 
			{ inf, 0, 8, 5, -4 }, 
			{ inf, inf, 0, -3, 9 },
			{ inf, -2, inf, 0, inf }, 
			{ 2, inf, inf, 7, 0 } 
		} );

		PathResult result = search( graph, 4 );

		System.out.println( result );
	}

	public static PathResult search( Graph graph, int start )
	{
		final double dis[] = new double[graph.size()];
		final int pre[] = new int[graph.size()];
		final boolean[] changed = new boolean[graph.size()];

		Arrays.fill( dis, Double.POSITIVE_INFINITY );
		Arrays.fill( pre, -1 );
		
		dis[start] = 0;
		changed[start] = true;

		boolean hasProgress = true;
		for ( int i = 0 ; hasProgress && i < graph.size() - 1 ; i++ )
		{
			hasProgress = false;
			for ( int u = 0 ; u < graph.size() ; u++ )
			{
				if( changed[u] ){
					for ( int v = 0 ; v < graph.size() ; v++ )
					{
						double alternative = dis[u] + graph.getWeight( u, v );
						if ( dis[v] > alternative )
						{
							dis[v] = alternative;
							pre[v] = u;
							changed[v] = true;
							hasProgress = true;
						}
					}
				}
			}
		}

		return new PathResult() {

			@Override
			public Path getPath( int from, int to )
			{
				if ( from != start ) throw new RuntimeException( "nicht unterstützt" );

				List<Integer> list = new ArrayList<>();

				while ( to > 0 )
				{ 
					list.add( 0, to );

					if ( to != from && pre[to] < 0 ) return null;

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

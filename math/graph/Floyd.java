package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Floyd {
	
	public static double inf = Double.POSITIVE_INFINITY;

	public static void main( String[] args )
	{
		Graph graph = new MatrixGraph( new double[][] { { 0, -1, inf , Double.POSITIVE_INFINITY },
				{ 10, 0, 1, Double.POSITIVE_INFINITY }, { 2, Double.POSITIVE_INFINITY, 0, -1 },
				{ Double.POSITIVE_INFINITY, 2, Double.POSITIVE_INFINITY, 0 } } );
		
		graph = new MatrixGraph( new double[][] { 
				{ 0, 1,inf , inf , inf },
				{ inf , 0 , 2, -5 , inf },
				{ inf , 2 , 0 , inf , 1 },
				{inf , inf , 2 , 0 , inf},
				{inf,inf,inf,inf,0}
		});

		PathResult result = search( graph );

		System.out.println( result.getPath( 0, 4 ) );
	}

	public static PathResult search( Graph graph )
	{
		final double dis[][] = new double[graph.size()][graph.size()];
		final int pre[][] = new int[graph.size()][graph.size()];

		for ( int u = 0 ; u < graph.size() ; u++ )
		{
			for ( int v = 0 ; v < graph.size() ; v++ )
			{
				dis[u][v] = graph.getWeight( u, v );
				pre[u][v] = dis[u][v] == Double.POSITIVE_INFINITY ? -1 : u;
			}
		}

		double nextDis[][] = new double[graph.size()][graph.size()];
		int nextPre[][] = new int[graph.size()][graph.size()];

		for ( int z = 0 ; z < graph.size() ; z++ )
		{
			for ( int u = 0 ; u < graph.size() ; u++ )
			{
				for ( int v = 0 ; v < graph.size() ; v++ )
				{
					if ( dis[u][v] > dis[u][z] + dis[z][v] )
					{
						nextDis[u][v] = dis[u][z] + dis[z][v];
						nextPre[u][v] = pre[z][v];
					}
					else
					{
						nextDis[u][v] = dis[u][v];
						nextPre[u][v] = pre[u][v];
					}
				}
			}

			for ( int row = 0 ; row < graph.size() ; row++ )
			{
				double[] tempDis = dis[row];
				dis[row] = nextDis[row];
				nextDis[row] = tempDis;

				int[] tempPre = pre[row];
				pre[row] = nextPre[row];
				nextPre[row] = tempPre;
			}
		}

		return new PathResult() {

			@Override
			public Path getPath( int from, int to )
			{
				List<Integer> list = new ArrayList<>();

				list.add( to );
				while ( true )
				{
					list.add( 0, pre[from][to] );
					if ( pre[from][to] == from ) break;
					to = pre[from][to];
				}

				return new Path( list );
			}
		};
	}
}

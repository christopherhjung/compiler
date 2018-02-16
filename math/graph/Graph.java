package graph;

import java.util.Collection;

public abstract class Graph {

	public abstract double getWeight( int u, int v );

	public abstract int size();

	public static Graph fromBinaryMatrix( boolean[][] matrix )
	{
		return new Graph() {

			@Override
			public double getWeight( int u, int v )
			{
				return matrix[u][v] ? 1 : 0;
			}

			@Override
			public int size()
			{
				return matrix.length;
			}

		};
	}
	
	public static Graph fromWeight( double[][] matrix )
	{
		return new Graph() {

			@Override
			public double getWeight( int u, int v )
			{
				return matrix[u][v];
			}

			@Override
			public int size()
			{
				return matrix.length;
			}

		};
	}

	public static Graph fromList( int[][] list )
	{
		return new Graph() {
			@Override
			public double getWeight( int u, int v )
			{
				for ( int i = 0 ; i < list[u].length ; i++ )
				{
					if ( list[u][i] == v ) return 1;
				}
				return 0;
			}

			@Override
			public int size()
			{
				return list.length;
			}

		};
	}
	
	public static Graph fromCollection( Collection<? super Collection< ? >> list )
	{
		return new Graph() {
			@Override
			public double getWeight( int u, int v )
			{
				return list.contains( u ) ? 1 : 0;
			}

			@Override
			public int size()
			{
				return list.size();
			}
		};
	}

}

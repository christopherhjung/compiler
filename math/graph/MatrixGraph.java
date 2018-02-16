package graph;

public class MatrixGraph extends Graph {

	private double[][] matrix;

	public MatrixGraph( double[][] matrix )
	{
		this.matrix = matrix;
	}

	@Override
	public double getWeight( int u, int w )
	{
		return matrix[u][w];
	}

	@Override
	public int size()
	{
		return matrix.length;
	}

}

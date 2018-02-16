package graph;

import java.util.List;

public class Path {

	private List<Integer> list;

	public Path( List<Integer> list )
	{
		this.list = list;
	}

	public double getLength()
	{
		return list.size() - 1;
	}

	@Override
	public String toString()
	{
		return list.toString();
	}
}

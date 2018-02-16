package graph;

import java.util.Arrays;

public class SearchResult {

	private Double[] dis;
	private Integer[] pre;

	public SearchResult( Double[] dis, Integer[] pre )
	{
		this.dis = dis;
		this.pre = pre;
	}
	
	@Override
	public String toString()
	{
		return Arrays.toString( dis ) + "\n" + Arrays.toString( pre );
	}

}

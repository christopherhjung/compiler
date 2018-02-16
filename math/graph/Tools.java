package graph;

public class Tools {

	public static void swap( Object[] array, int a, int b )
	{
		Object temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( int[] array, int a, int b )
	{
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( double[] array, int a, int b )
	{
		double temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( long[] array, int a, int b )
	{
		long temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( char[] array, int a, int b )
	{
		char temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( byte[] array, int a, int b )
	{
		byte temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	public static void swap( float[] array, int a, int b )
	{
		float temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

}

package maths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Coefficents {

	private static List<int[]> coefficents = new ArrayList<>();

	static
	{
		// init with first coefficent
		coefficents.add( new int[] { 1 } );
		
		// generate first 10 coefficients
		get( 10 );
	}

	public static int[] get( int grad )
	{
		if ( coefficents.size() <= grad )
		{
			int[] nearest = coefficents.get( coefficents.size() - 1 );

			int[] nextCoefficents = null;
			for ( ; coefficents.size() <= grad ; )
			{
				nextCoefficents = new int[nearest.length + 1];
				nextCoefficents[0] = 1;
				nextCoefficents[nearest.length] = 1;
				for ( int j = 1 ; j < nearest.length ; j++ )
				{
					nextCoefficents[j] = nearest[j - 1] + nearest[j];
				}
				coefficents.add( nextCoefficents );
				nearest = nextCoefficents;
			}

			return nextCoefficents;
		}
		return coefficents.get( grad );
	}
	
	public static int[] generate( int n ){
		int[] result = new int[n + 1];
		result[0] = 1;
		
		for( int i = 0 ; i < n ; i++){
			result[i+1] = result[i] * (n-i) / (i+1);
		}
		return result;
	}
}

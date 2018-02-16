package trainer;

public class TrainResult {

	private static String FORMAT_RESULT = "Nach %d Schritten wurde eine Fehlergenauigkeit von %f erreicht";
	
	private double error;
	private int iterations;
	
	public TrainResult(double error, int iterations)
	{
		this.error = error;
		this.iterations = iterations;
	}
	
	@Override
	public String toString()
	{
		return String.format( FORMAT_RESULT, iterations, error );
	}
}

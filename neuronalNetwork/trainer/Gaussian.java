package trainer;

import java.util.Random;

public class Gaussian
{
	private static Random gen = new Random();

	public static double getRandomGaussian()
	{
		return getRandomGaussian(0.0, 1.0);
	}

	public static double getRandomGaussian(double mean, double stddev)
	{
		double u, v, s, t;

		do
		{
			u = 2 * gen.nextDouble() - 1;
			v = 2 * gen.nextDouble() - 1;
		} while (u * u + v * v > 1 || (u == 0 && v == 0));

		s = u * u + v * v;
		t = Math.sqrt((-2.0 * Math.log(s)) / s);

		double val1 = stddev * u * t + mean;
		double val2 = stddev * v * t + mean;
		
		return val1;
	}
}
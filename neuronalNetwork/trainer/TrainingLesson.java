package trainer;

public class TrainingLesson {

	private double[][] samples;
	private double[][] targets;
	private double lernRate;
	private int maxLernSteps = 5000000;

	public TrainingLesson( double[][] samples, double[][] targets, double learnRate )
	{
		if ( samples.length != targets.length )
		{
			throw new IllegalArgumentException( "samples.length != targets.length" );
		}

		this.samples = samples;
		this.targets = targets;
		this.lernRate = learnRate;
	}

	public double getLearningRate()
	{
		return lernRate;
	}

	public double[][] getSamples()
	{
		return samples;
	}

	public double[] getSampel( int index )
	{
		return samples[index];
	}

	public double[][] getTargets()
	{
		return targets;
	}

	public double[] getTarget( int index )
	{
		return targets[index];
	}

	public int size()
	{
		return samples.length;
	}

	public int maxLernSteps()
	{
		return maxLernSteps;
	}
}

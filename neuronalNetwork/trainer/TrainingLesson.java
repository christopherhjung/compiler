package trainer;

public class TrainingLesson {

	private double[][] inputs;
	private double[][] desire;
	private double lernRate;
	
	public TrainingLesson(double[][] inputs, double[][] desire, double learnRate) {
		this.inputs = inputs;
		this.desire = desire;
		this.lernRate = learnRate;
	}

	public double getKonvergenz() {
		return lernRate;
	}

	public double[][] getInputs() {
		return inputs;
	}

	public double[][] getDesiredOutputs() {
		return desire;
	}

}

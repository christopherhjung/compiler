package neuron;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NeuralNetworkConfig {

	Map<Integer,NeuronConfig> connects;
	
	
	
	
	private static class NeuronConfig{
		List<Integer> parents;
		int layer;
		
		public NeuronConfig( int layer ){
			parents = new ArrayList<>();
			this.layer = layer;
		}
	}
}

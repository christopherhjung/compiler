package neuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import neuronbehavior.NeuronBehavior;
import neuronbehavior.NeuronBehaviors;

public class Neuron {

	private final Map<Neuron, Weight> parents;
	private final NeuronBehavior behavior;
	private final List<Neuron> children;

	private double activationLevel = 1;
	private double threshold = 0;

	public Neuron()
	{
		this( NeuronBehaviors.FERMI );
	}

	public Neuron( NeuronBehavior behavior )
	{
		this.behavior = behavior;
		this.parents = new HashMap<>();
		this.children = new ArrayList<>();
	}

	public Neuron( NeuronBehavior behavior, List<Neuron> parents )
	{
		this( behavior );
		for ( Neuron parent : parents )
		{
			attachInput( parent );
		}
	}

	public Neuron( List<Neuron> parents )
	{
		this();
		for ( Neuron parent : parents )
		{
			attachInput( parent );
		}
	}

	public Neuron( NeuralLayer parentLayer )
	{
		this();
		if ( parentLayer != null )
		{
			for ( Neuron parent : parentLayer.getNeurons() )
			{
				attachInput( parent );
			}
		}
	}

	public Neuron( NeuralLayer parentLayer, NeuronBehavior behavior )
	{
		this( behavior );
		if ( parentLayer != null )
		{
			for ( Neuron parent : parentLayer.getNeurons() )
			{
				attachInput( parent );
			}
		}
	}

	public void attachInput( Neuron parent )
	{
		parents.put( parent, new Weight() );
		parent.children.add( this );
	}

	public void attachOutput( Neuron child )
	{
		children.add( child );
		child.parents.put( this, new Weight() );
	}

	public double getActivationLevel()
	{
		return activationLevel;
	}

	protected void setActivationLevel( double activationLevel )
	{
		this.activationLevel = activationLevel;
	}

	public NeuronBehavior getBehavior()
	{
		return this.behavior;
	}

	public Map<Neuron, Weight> getParents()
	{
		return this.parents;
	}

	public List<Neuron> getChildren()
	{
		return this.children;
	}

	public void propagate()
	{
		double sum = 0;
		for ( Entry<Neuron, Weight> entry : this.parents.entrySet() )
		{
			sum += entry.getKey().getActivationLevel() * entry.getValue().getValue();
		}

		this.activationLevel = this.behavior.computeActivation( sum - threshold );
	}

	public void setThreshold( double threshold )
	{
		this.threshold = threshold;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "Neuron{" );
		sb.append( "parents:" );
		for ( Map.Entry<Neuron, Weight> entry : this.parents.entrySet() )
		{
			sb.append( entry.getKey().hashCode() );
			sb.append( "," );
		}
		sb.append( "children" );
		for ( Neuron child : this.children )
		{
			sb.append( child.hashCode() );
			sb.append( "," );
		}
		sb.append( "}" );
		return sb.toString();
	}
}

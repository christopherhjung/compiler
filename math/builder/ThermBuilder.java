package builder;

import java.util.Collection;

import therms.Therm;

public interface ThermBuilder {

	public Therm build();

	public void add( Therm therm );

	default void addAll( Collection<? extends Therm> c )
	{
		for ( Therm e : c )
		{
			add( e );
		}
	}
}

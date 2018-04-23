package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class SinPlugin extends EnginePlugin {

	private final Sin instance = new Sin();
	private Therm derivate = null;

	@Override
	public void enable( MathParser engine )
	{
		derivate = engine.eval( "cos" );
	}

	@Override
	public Therm handle( MathParser engine )
	{
		
	}

	@EngineExecute
	public Therm execute()
	{
		return instance;
	}

	@EngineExecute
	public Therm execute( Therm therm )
	{
		return new Chain( instance, therm );
	}

	public class Sin extends Therm {

		private Sin()
		{}

		@Override
		public Therm derivate( Variable name )
		{
			return derivate;
		}

		@Override
		public Therm reduce( VarSet varSet, Therm... therms )
		{
			if ( therms.length == 1 )
			{
				if ( therms[0] instanceof Const )
				{
					Const value = (Const) therms[0];
					return new Const( Math.sin( value.getValue() ) );
				}

				return new Chain( this, therms[0] );
			}

			throw new IllegalArgumentException( "Wrong Arguments" );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "sin" );
		}
	}
}
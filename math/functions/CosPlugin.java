package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class CosPlugin extends EnginePlugin {

	private final Cos instance = new Cos();
	private Therm derivate = null;

	@Override
	public void onAttach( MathParser engine )
	{
		derivate = engine.eval( "-sin" );
	}

	public boolean handle( String message )
	{
		if ( message.equals( "derivate" ) )
		{

		}
		return false;
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

	public class Cos extends Therm {

		private Cos()
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
					return new Const( Math.cos( value.getValue() ) );
				}

				return new Chain( this, therms[0] );
			}

			throw new IllegalArgumentException( "Wrong Arguments" );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "cos" );
		}
	}
}
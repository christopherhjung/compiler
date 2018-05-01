package functions;

import parser.MathEngine;
import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;

public class SinPlugin extends EnginePlugin {

	private final Sin instance = new Sin();
	private Therm derivate = null;

	@Override
	public void onStart( MathEngine engine )
	{
		derivate = engine.eval( "cos" );
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
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "reduce" ) && params.length == 1 )
			{

				if ( params[0] instanceof Const )
				{
					Const value = (Const) params[0];
					return new Const( Math.sin( value.getValue() ) );
				}

				return new Chain( this, (Therm) params[0] );
			}
			else if ( key.equals( "derivate" ) )
			{
				return derivate;
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "sin" );
		}
	}
}
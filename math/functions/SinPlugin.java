package functions;

import functions.FunctionPlugin.Chain;
import parser.EngineExecute;
import parser.EnginePlugin;
import parser.PluginExtention;
import parser.ThermStringifier;
import therms.Therm;

public class SinPlugin implements PluginExtention {

	private final Sin instance = new Sin();

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
				Therm therm = (Therm) params[0];
				if ( therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );
					return eval( Math.sin( value ) );
				}

				return new Chain( this, (Therm) params[0] );
			}
			else if ( key.equals( "derivate" ) )
			{
				return eval("cos");
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
package functions;

import parser.ThermStringifier;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;

public class LogPlugin extends EnginePlugin {

	private Log log = new Log();

	@EngineExecute
	public Therm execute( Therm therm )
	{
		return new Chain( log, therm );
	}

	public static class Log extends Therm {

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				return "1/";
			}
			else if ( key.equals( "reduce" ) )
			{
				if ( params.length == 1 )
				{
					Therm therm = ReflectionUtils.as( params[0], Therm.class );
					if ( therm != null )
					{
						double value = therm.get( "value", Double.class );
						return String.valueOf( Math.log( value ) );
					}

					// return new Chain( this, therms[0] );
				}
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "log" );
		}
	}
}

package functions;

import functions.FunctionPlugin.Chain;
import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;
import tools.ReflectionUtils;

public class LogPlugin extends EnginePlugin {

	private Log log = new Log();

	@EngineExecute
	public Therm execute( Therm therm )
	{
		return new Chain( log, therm );
	}

	@Override
	public String getName()
	{
		return "function.log";
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );
		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.derivate.log";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				if ( key.contains( "derivate" ) )
				{
					return eval("x->1/x");
				}

				return null;
			}
		} );
	}

	public class Log extends Therm {

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "reduce" ) )
			{
				if ( params.length == 1 )
				{
					Therm therm = ReflectionUtils.as( params[0], Therm.class );
					if ( therm != null )
					{
						double value = therm.get( "value", Double.class );
						return eval( Math.log( value ) );
					}
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

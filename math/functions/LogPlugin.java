package functions;

import functions.FunctionPlugin.Chain;
import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;
import tools.ReflectionUtils;

public class LogPlugin extends EnginePlugin {

	private Log instance;

	@Override
	public String getName()
	{
		return "variable.log";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "log" ) )
		{
			return instance;
		}

		return null;
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );
		instance = new Log();
		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.variable.derivate.log";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				if ( key.contains( "derivate" ) )
				{
					return eval( "x->1/x" );
				}

				return null;
			}
		} );
	}

	public class Log extends Therm {

		@Override
		public EnginePlugin getPlugin()
		{
			return LogPlugin.this;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "call" ) && params.length == 1 )
			{
				Therm therm = ReflectionUtils.as( params[0], Therm.class );
				if ( therm != null )
				{
					double value = therm.get( "value", Double.class );
					return eval( Math.log( value ) );
				}
			}

			return super.get( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "log" );
		}
	}
}

package functions;

import java.util.Arrays;

import org.omg.IOP.ENCODING_CDR_ENCAPS;

import functions.FunctionPlugin.Chain;
import parser.EngineExecute;
import parser.EnginePlugin;
import parser.MathParser;
import parser.MathProgram;
import parser.PluginExtention;
import parser.ThermStringifier;
import therms.Therm;

public class SinPlugin extends EnginePlugin {

	private Sin instance = null;

	@Override
	public String getName()
	{
		return "variable.sin";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "sin" ) )
		{
			return instance;
		}

		return super.handle( key, params );
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );
		instance = new Sin();
		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.variable.derivate.sin";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				if ( key.equals( "derivate" ) && params[0] == instance )
				{
					return eval( "cos" );
				}

				return super.handle( key, params );
			}

		} );
	}

	public class Sin extends Therm {

		private Sin()
		{}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "call" ) && params.length == 1 )
			{
				Therm therm = (Therm) params[0];
				if ( therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );
					return eval( Math.sin( value ) );
				}

				return null;
			}

			return super.get( key, params );
		}
		
		@Override
		public EnginePlugin getPlugin()
		{
			return SinPlugin.this;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "sin" );
		}
	}
}
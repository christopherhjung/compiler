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

public class CosPlugin extends EnginePlugin {

	private Cos instance = null;

	@Override
	public String getName()
	{
		return "variable.cos";
	}

	@Override
	public Object handle( String key, Object... params )
	{
		if ( key.equals( "cos" ) )
		{
			return instance;
		}

		return super.handle( key, params );
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );
		instance = new Cos();
		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.variable.derivate.cos";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				if ( key.equals( "derivate" ) && params[0] == instance )
				{
					return eval( "x->-sin(x)" );
				}

				return super.handle( key, params );
			}

		} );
	}

	public class Cos extends Therm {

		private Cos()
		{}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "call" ) && params.length == 1 )
			{
				Therm therm = (Therm) params[0];
				if ( therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );
					return eval( Math.cos( value ) );
				}

				return null;
			}

			return super.execute( key, params );
		}
		
		@Override
		public EnginePlugin getPlugin()
		{
			return CosPlugin.this;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( "cos" );
		}
	}
}
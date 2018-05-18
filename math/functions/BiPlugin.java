package functions;

import functions.DividePlugin.Divide;
import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class BiPlugin extends EnginePlugin {

	private String name;
	private String linker;

	public BiPlugin( String name, String linker )
	{
		this.name = name;
		this.linker = linker;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		while ( !parser.eat( linker ) )
		{
			if ( !parser.eat( ' ' ) )
			{
				return null;
			}
		}

		return new BiTherm( this, left, parser.parse(), linker );
	}

	public static class BiTherm extends Therm {

		EnginePlugin plugin;
		private Therm left;
		private Therm right;
		private String linker;

		public BiTherm( EnginePlugin plugin, Therm left, Therm right, String linker )
		{
			this.plugin = plugin;
			this.left = left;
			this.right = right;
			this.linker = linker;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "left" ) )
			{
				return this.left;
			}
			else if ( key.equals( "right" ) )
			{
				return this.right;
			}

			return super.execute( key, params );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return plugin;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( left );
			builder.append( linker );
			builder.append( right );
		}
	}
}

package functions;

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

	private String getLinker()
	{
		return linker;
	}

	protected boolean hasRightType( int index, String type )
	{
		return true;
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null || !hasRightType( 0, left.getType() ) )
		{
			return null;
		}

		if ( !linker.startsWith( " " ) )
		{
			parser.eatAll( ' ' );
		}

		while ( !parser.eat( linker ) )
		{
			if ( !parser.eat( ' ' ) )
			{
				return null;
			}
		}

		Therm right = parser.parse();

		if ( right == null || !hasRightType( 1, right.getType() ) )
		{
			return null;
		}

		return create( left, right );
	}

	private Therm create( Therm left, Therm right )
	{
		return new BiTherm( this, left, right );
	}

	public static class BiTherm extends Therm {

		private BiPlugin plugin;
		private Therm left;
		private Therm right;

		public BiTherm( BiPlugin plugin, Therm left, Therm right )
		{
			this.plugin = plugin;
			this.left = left;
			this.right = right;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "left" ) )
			{
				return this.left;
			}
			else if ( key.equals( "right" ) )
			{
				return this.right;
			}
			else if ( key.equals( "size" ) )
			{
				return 2;
			}
			else if ( key.equals( "linker" ) )
			{
				return plugin.getLinker();
			}

			return super.get( key, params );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return plugin;
		}

		@Override
		public String getType()
		{
			return plugin.getName();
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( left );
			builder.append( plugin.getLinker() );
			builder.append( right );
		}
	}
}

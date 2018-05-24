package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class SinglePlugin extends EnginePlugin {

	private String name;
	private String pre;
	private String post;

	public SinglePlugin( String name, String pre, String post )
	{
		this.name = name;
		this.pre = pre;
		this.post = post;
	}

	@Override
	public String getName()
	{
		return name;
	}

	private String getPre()
	{
		return pre;
	}

	private String getPost()
	{
		return post;
	}
	
	@Override
	public final Therm handle( MathParser parser, Therm left )
	{
		if ( left != null || !parser.eat( pre ) )
		{
			return null;
		}

		Therm therm = handle( parser );

		if ( !parser.eat( post ) )
		{
			return null;
		}

		return therm;
	}

	protected Therm create( Object value )
	{
		return new SingleTherm( this, value );
	}

	public static class SingleTherm extends Therm {

		private SinglePlugin plugin;
		private Object value;

		public SingleTherm( SinglePlugin plugin, Object value )
		{
			this.plugin = plugin;
			this.value = value;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "value" ) )
			{
				return this.value;
			}
			else if ( key.equals( "size" ) )
			{
				return 1;
			}
			else if ( key.equals( "pre" ) )
			{
				return plugin.getPre();
			}
			else if ( key.equals( "post" ) )
			{
				return plugin.getPost();
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
			builder.append( plugin.getPre() );
			builder.append( value );
			builder.append( plugin.getPost() );
		}
	}
}

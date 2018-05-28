package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class BinaryPlugin extends EnginePlugin {

	public enum Index {
		LEFT, RIGHT
	}

	private String name;
	private String linker;

	public BinaryPlugin( String name, String linker )
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

	protected boolean checkStatement( Index index, Statement therm )
	{
		return true;
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left == null || !checkStatement( Index.LEFT, left ) )
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

		Statement right = parser.parse();
		if ( right == null || !checkStatement( Index.RIGHT, right ) )
		{
			return null;
		}

		return create( left, right );
	}

	private Statement create( Statement left, Statement right )
	{
		return new BinaryStatement( this, left, right );
	}

	public static class BinaryStatement extends Statement {

		private BinaryPlugin plugin;
		private Statement left;
		private Statement right;

		public BinaryStatement( BinaryPlugin plugin, Statement left, Statement right )
		{
			this.plugin = plugin;
			this.left = left;
			this.right = right;
		}

		@Override
		public Object getImpl( String key )
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

			return super.getImpl( key );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return plugin;
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( left );
			builder.append( " " );
			builder.append( plugin.getLinker() );
			builder.append( " " );
			builder.append( right );
		}
	}
}

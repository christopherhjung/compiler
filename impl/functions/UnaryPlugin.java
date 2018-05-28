package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class UnaryPlugin extends EnginePlugin {

	private String name;
	private String pre;
	private String post;

	public UnaryPlugin( String name, String pre, String post )
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

	protected boolean check( Statement statement )
	{
		return true;
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left != null )
		{
			return null;
		}

		parser.eatWhile( Character::isWhitespace );
		if ( !parser.eat( pre ) )
		{
			return null;
		}

		Statement statement = parser.parse();

		if ( !check( statement ) )
		{
			return null;
		}

		parser.eatWhile( Character::isWhitespace );
		if ( !parser.eat( post ) )
		{
			return null;
		}

		return create( statement );
	}

	protected Statement create( Statement value )
	{
		return new UnaryStatement( value );
	}

	public class UnaryStatement extends Statement {

		private Statement value;

		public UnaryStatement( Statement value )
		{
			this.value = value;
		}

		@Override
		public Object getImpl( String key )
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
				return UnaryPlugin.this.getPre();
			}
			else if ( key.equals( "post" ) )
			{
				return UnaryPlugin.this.getPost();
			}

			return super.getImpl( key );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return UnaryPlugin.this;
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( get( "pre" ) );
			builder.append( value );
			builder.append( get( "post" ) );
		}
	}
}

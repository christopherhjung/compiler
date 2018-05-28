package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class StringPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "string";
	}

	@Override
	public Statement handle( MathParser parser )
	{
		if ( !parser.eat( '\"' ) )
		{
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean explicit = false;
		while ( explicit || !parser.eat( "\"" ) )
		{
			explicit = false;
			char cha = parser.nextChar();
			if ( cha == '\\' )
			{
				explicit = true;
			}
			
			sb.append( cha );
		}

		return new StringStatement( sb.toString() );
	}

	public class StringStatement extends Statement {

		private String value;

		public StringStatement( String value )
		{
			this.value = value;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return StringPlugin.this;
		}
		
		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( "\"" );
			builder.append( value );
			builder.append( "\"" );
		}
	}
}

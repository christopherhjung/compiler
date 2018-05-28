package functions;

import java.util.ArrayList;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class VectorPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "vector";
	}

	@Override
	public Statement handle( MathParser parser )
	{
		Statement therm = null;

		parser.eatAll( ' ' );
		if ( parser.eat( '(' ) )
		{
			List<Statement> therms = new ArrayList<>();

			parser.eatAll( ' ' );
			for ( ; parser.isNot( ')' ) ; )
			{
				Statement param = parser.parseWithLevelReset();
				if ( param == null )
				{
					return null;
				}
				therms.add( param );
				parser.eatAll( ' ' );
				parser.eat( ',' );
			}

			parser.eat( ')' );

			Statement[] params = therms.toArray( new Statement[therms.size()] );

			therm = new Vector( params );
		}

		return therm;
	}

	public class Vector extends Statement {

		private final Statement[] params;

		public Vector( Statement... params )
		{
			this.params = params;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return VectorPlugin.this;
		}

		@Override
		public Object getImpl( String key)
		{
			if ( key.equals( "value" ) )
			{
				return this.params;
			}

			return super.getImpl( key );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( "(" );
			int temp = builder.resetLast();
			builder.append( params, ", " );
			builder.setLast( temp );
			builder.append( ")" );
		}
	}

}

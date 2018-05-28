package functions;

import java.util.HashMap;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class MethodPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "method";
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left == null || !left.is( "name" ) && !left.is( "vector" ) )
		{
			return null;
		}

		parser.eatAll( ' ' );
		if ( parser.eat( "->" ) )
		{
			Statement therm = parser.parseWithLevelReset();

			return new Method( left, therm );
		}

		return super.handle( parser );
	}

	public class Method extends Statement {

		private Statement vector;
		private Statement inner;

		public Method( Statement vector, Statement inner )
		{
			this.vector = vector;
			this.inner = inner;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return MethodPlugin.this;
		}

		@Override
		public Object getImpl( String key )
		{
			if ( key.equals( "type" ) )
			{
				return "method";
			}
			else if ( key.equals( "value" ) )
			{
				return inner;
			}
			else if ( key.equals( "params" ) )
			{
				return vector;
			}

			return super.getImpl( key );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			int temp = builder.resetLast();
			builder.append( vector );
			builder.append( " -> " );
			builder.append( inner );
			builder.setLast( temp );
		}
	}
}

package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class MethodSignaturePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "function";
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		Statement therm = null;

		if ( left == null )
		{
			return null;
		}

		Statement vector = parser.parse();

		if ( vector != null && vector.is( "vector" ) )
		{
			therm = new MethodSignature( left, vector );
		}

		return therm;
	}

	public class MethodSignature extends Statement {

		private final Statement vector;
		private final Statement method;

		public MethodSignature( Statement method, Statement vector )
		{
			this.method = method;
			this.vector = vector;
		}

		@Override
		public String getType()
		{
			return "chain";
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return MethodSignaturePlugin.this;
		}

		@Override
		public Object getImpl( String key )
		{
			if ( key.equals( "left" ) )
			{
				return method;
			}
			else if ( key.equals( "right" ) )
			{
				return this.vector;
			}

			return super.getImpl( key );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( method );
			builder.append( vector );
		}
	}

}

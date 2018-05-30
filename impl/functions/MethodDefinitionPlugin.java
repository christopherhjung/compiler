package functions;

import java.util.ArrayList;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class MethodDefinitionPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "method-definition";
	}

	public String[] required()
	{
		return new String[] {
				"assign", "declare", "name"
		};
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left == null || !left.is( "method-signature" ) )
		{
			return null;
		}

		Statement right = parser.parseWithLevelReset();

		if ( !right.is( "block" ) )
		{
			return null;
		}

		return new MethodDefinition( left, right );
	}
	
	public class MethodDefinition extends Statement{

		
		Statement left;
		Statement right;
		
		public MethodDefinition( Statement left, Statement right )
		{
			this.left = left;
			this.right = right;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return MethodDefinitionPlugin.this;
		}
		
		@Override
		public void toString( StatementStringifier builder )
		{
			left.toString( builder );
			right.toString( builder );
		}
		
	}

}

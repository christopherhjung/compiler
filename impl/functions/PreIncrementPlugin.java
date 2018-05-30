package functions;

import parser.MathParser;
import parser.Statement;

public class PreIncrementPlugin extends UnaryPlugin {

	public PreIncrementPlugin()
	{
		super( "preinc", "++", "" );
	}
	
}

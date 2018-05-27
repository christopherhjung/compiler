package functions;

import java.util.ArrayList;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class TemplatePlugin extends UnaryPlugin {

	public TemplatePlugin()
	{
		super( "template", "${", "}" );
	}
	
	@Override
	protected boolean check( Statement statement )
	{
		return statement.is( "name" );
	}
}

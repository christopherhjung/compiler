package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class TypePlugin extends BiPlugin {

	public TypePlugin()
	{
		super( "declare", " " );
	}

}

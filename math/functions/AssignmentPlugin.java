package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class AssignmentPlugin extends BiPlugin {

	public AssignmentPlugin()
	{
		super( "assign", "=" );
	}

}

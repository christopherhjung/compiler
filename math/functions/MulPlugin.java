package functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import tools.ListComparer;

public class MulPlugin extends BiPlugin {

	public MulPlugin()
	{
		super( "mul", "*" );
	}

}

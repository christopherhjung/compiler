package functions;

import parser.MathParser;
import parser.MathProgram;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.Run;

public class DerivatePlugin extends EnginePlugin {

	private MathProgram program;

	@Override
	public void onStart( MathProgram program )
	{
		this.program = program;
	}

	@EngineExecute
	public Therm execute( Therm therm, Variable var )
	{

		String str = (String) therm.execute( "derivate", var );
		if ( str == null )
		{
			return null;
		}

		MathParser parser = Run.safe( () -> program.start() );
		return parser.eval( str );
	}
}

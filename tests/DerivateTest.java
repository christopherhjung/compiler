import org.junit.Test;

import functions.IntegerPlugin;
import parser.ScriptParser;
import parser.MathProgram;
import parser.Statement;
import tools.Run;

public class DerivateTest {

	
	@Test
	public void derivateTest() throws Exception
	{
		MathProgram program = new MathProgram();
		program.installPlugin( 1, IntegerPlugin.class );
		ScriptParser engine = Run.safe( () -> program.start() );
		engine.eval( "f=x->x^2" );
		Statement derivate = engine.eval( "reduce(derivate(f))" );
		
		
	}
}

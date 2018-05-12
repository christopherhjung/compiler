import static org.junit.Assert.*;

import org.junit.Test;

import functions.ConstPlugin;
import parser.MathEngine;
import parser.MathProgram;
import therms.Therm;
import tools.Run;

public class DerivateTest {

	
	@Test
	public void derivateTest() throws Exception
	{
		MathProgram program = new MathProgram();
		program.installPlugin( 1, ConstPlugin.class );
		MathEngine engine = Run.safe( () -> program.start() );
		engine.eval( "f=x->x^2" );
		Therm derivate = engine.eval( "reduce(derivate(f))" );
		
		
	}
}

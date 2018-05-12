import static org.junit.Assert.*;

import org.junit.Test;

import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.ConstPlugin;
import functions.DividePlugin;
import functions.ExponentPlugin;
import functions.FunctionPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.ParenthesisPlugin;
import functions.ReducePlugin;
import functions.SignPlugin;
import functions.VariablePlugin;
import parser.MathEngine;
import parser.MathProgram;
import therms.Therm;
import tools.Run;

public class ConstTest {

	
	
	@Test
	public void engineStartTest()
	{
		assertNotNull( Util.startEngine() );
	}
	
	@Test
	public void constTest()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "1" );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 1.0 );
	}
	
	
}

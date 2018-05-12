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

public class EqualsTest {

	@Test
	public void equalsTestConst()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "1" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test
	public void equalsTestVariable()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "x" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test(timeout = 1000)
	public void equalsTestAdditional()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "1+x" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestExponential()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestCalc()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestNegate()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "-5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "x^4 + 3 * x - 5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig2()
	{
		MathEngine engine = Util.startEngine();
		Therm therm = engine.eval( "x^4 + 3 * x - 5" );
		Therm therm2 = engine.eval( "x^4 + 3 * x + 5" );
		assertFalse( ReducePlugin.equals( therm, therm2 ) );	
	}
}

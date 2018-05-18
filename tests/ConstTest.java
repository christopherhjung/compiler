import static org.junit.Assert.*;

import org.junit.Test;

import core.StackEngine;
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
	
	@Test
	public void methodTest()
	{
		MathEngine engine = StackEngine.startEngine();
		engine.eval( "update(f=x->x(2))" );
		engine.eval( "update(g=x->x)" );
		Therm therm = engine.eval( "update(f(g))" );
				
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 2.0 );
	}
	
	@Test
	public void sinusTest()
	{
		MathEngine engine = StackEngine.startEngine();
		Therm therm = engine.eval( "update(sin(0))" );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 0.0 );
	}
	

	@Test
	public void sinusTest2()
	{
		MathEngine engine = StackEngine.startEngine();
		engine.eval( "update(f=x->sin(x))" );
		Therm therm = engine.eval( "update(f(0))" );
		
		System.out.println( therm );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 0.0 );
	}
	
	@Test
	public void reduceTest()
	{
		MathEngine engine = StackEngine.startEngine();
		Therm therm = engine.eval( "reduce(x->(x^x * x * (1.0 * (x->(1.0/x))(x))))" );
		
		System.out.println( therm );
	}
}

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import functions.ReducePlugin;
import parser.MathEngine;
import parser.Statement;

public class EqualsTest {

	@Test
	public void equalsTestConst()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "1" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test
	public void equalsTestVariable()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "x" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test(timeout = 1000)
	public void equalsTestAdditional()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "1+x" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestExponential()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestCalc()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestNegate()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "-5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "x^4 + 3 * x - 5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig2()
	{
		MathEngine engine = Util.startEngine();
		Statement therm = engine.eval( "x^4 + 3 * x - 5" );
		Statement therm2 = engine.eval( "x^4 + 3 * x + 5" );
		assertFalse( ReducePlugin.equals( therm, therm2 ) );	
	}
}

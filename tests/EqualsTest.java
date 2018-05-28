import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import functions.ReducePlugin;
import parser.ScriptParser;
import parser.Statement;

public class EqualsTest {

	@Test
	public void equalsTestConst()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "1" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test
	public void equalsTestVariable()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "x" );
		
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}

	@Test(timeout = 1000)
	public void equalsTestAdditional()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "1+x" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestExponential()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestCalc()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "x^4" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestNegate()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "-5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "x^4 + 3 * x - 5" );
		assertTrue( ReducePlugin.equals( therm, therm ) );	
	}
	
	@Test(timeout = 1000)
	public void equalsTestBig2()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "x^4 + 3 * x - 5" );
		Statement therm2 = engine.eval( "x^4 + 3 * x + 5" );
		assertFalse( ReducePlugin.equals( therm, therm2 ) );	
	}
}

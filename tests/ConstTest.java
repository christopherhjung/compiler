import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import core.StackEngine;
import parser.ScriptParser;
import parser.Statement;

public class ConstTest {

	
	
	@Test
	public void engineStartTest()
	{
		assertNotNull( Util.startEngine() );
	}
	
	@Test
	public void constTest()
	{
		ScriptParser engine = Util.startEngine();
		Statement therm = engine.eval( "1" );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 1.0 );
	}
	
	@Test
	public void methodTest()
	{
		ScriptParser engine = StackEngine.startEngine();
		engine.eval( "update(f=x->x(2))" );
		engine.eval( "update(g=x->x)" );
		Statement therm = engine.eval( "update(f(g))" );
				
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 2.0 );
	}
	
	@Test
	public void sinusTest()
	{
		ScriptParser engine = StackEngine.startEngine();
		Statement therm = engine.eval( "update(sin(0))" );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 0.0 );
	}
	

	@Test
	public void sinusTest2()
	{
		ScriptParser engine = StackEngine.startEngine();
		engine.eval( "update(f=x->sin(x))" );
		Statement therm = engine.eval( "update(f(0))" );
		
		System.out.println( therm );
		
		assertTrue( therm.is( "const" ) );
		assertTrue( therm.get( "value", Double.class ) == 0.0 );
	}
	
	@Test
	public void reduceTest()
	{
		ScriptParser engine = StackEngine.startEngine();
		Statement therm = engine.eval( "reduce(x->(x^x * x * (1.0 * (x->(1.0/x))(x))))" );
		
		System.out.println( therm );
	}
}

package parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import parser.Space.Scope;
import therms.Therm;
import tools.Run;

public class MathEngine {
	private Map<Integer, Set<EnginePlugin>> plugins;

	public Scope globalScope = new Scope();
	public Scope currentScope = globalScope;

	public void enterScope( Scope scope )
	{
		scope.setParentScope( currentScope );
		currentScope = scope;
	}
	
	public void leaveScope()
	{
		currentScope = currentScope.getParentScope();
	}

	public MathEngine( Map<Integer, Set<EnginePlugin>> plugins )
	{
		this.plugins = plugins;
	}

	public <T extends MathParser> T getParser( Class<T> parserClass )
	{
		return Run.safe( () -> parserClass.getConstructor( Map.class ).newInstance( plugins ) );
	}

	public Therm eval( String str )
	{

		return new MathParser( plugins ).eval( str );
	}

	public Therm eval( Object... obj )
	{

		return new HybridMathParser( plugins ).eval( obj );
	}

	public Therm eval( List<Object> list )
	{

		return new HybridMathParser( plugins ).eval( list.toArray() );
	}
}

package parser;

import java.util.ArrayList;
import java.util.List;

import therms.Therm;

public abstract class PluginExtention {

	private MathEngine engine;
	private List<PluginExtention> extentions = new ArrayList<>();

	public abstract Object handle( String key, Object... params );

	public abstract String getParentName();

	public abstract String getName();
	
	void onCreate( MathProgram program )
	{
		extentions.forEach( extention -> extention.onCreate( program ) );
	}

	void onStart( MathEngine engine )
	{
		this.engine = engine;
		extentions.forEach( extention -> extention.onStart( engine ) );
	}

	void installExtention( PluginExtention extention )
	{
		extentions.add( extention );
	}

	public Therm eval( String str )
	{
		return engine.eval( str );
	}

	public Therm eval( Object... objs )
	{
		return engine.eval( objs );
	}

	public Therm eval( List<Object> list )
	{
		return engine.eval( list );
	}
}

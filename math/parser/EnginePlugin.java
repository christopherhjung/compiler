package parser;

import java.util.HashSet;
import java.util.List;

import therms.Therm;

public abstract class EnginePlugin {

	private MathEngine engine;
	private HashSet<EnginePlugin> extentions = new HashSet<>();

	public abstract String getName();

	public String getParentName()
	{
		String name = getName();
		int position = name.lastIndexOf( '.' );
		if ( position == -1 )
		{
			return null;
		}
		
		return name.substring( 0, position );
	}

	public void installExtention( EnginePlugin extention )
	{
		extentions.add( extention );
	}

	public MathEngine getEngine()
	{
		return engine;
	}

	public Therm eval( String str )
	{
		return getEngine().eval( str );
	}

	public Therm eval( Object... objs )
	{
		return getEngine().eval( objs );
	}

	public Therm eval( List<Object> list )
	{
		return getEngine().eval( list );
	}

	protected void onCreate( MathProgram program )
	{
		extentions.forEach( extention -> extention.onCreate( program ) );
	}

	protected void onStart( MathEngine engine )
	{
		this.engine = engine;
		extentions.forEach( extention -> extention.onStart( engine ) );
	}

	public void reset()
	{
		extentions.forEach( extention -> extention.reset() );
	}

	public Therm handle( MathParser parser )
	{
		return null;
	}

	public Therm handle( MathParser parser, Therm left )
	{
		if ( left != null )
		{
			return null;
		}

		return handle( parser );
	}

	public Object handle( String key, Object... params )
	{
		Object result = null;
		for ( EnginePlugin extention : extentions )
		{
			result = extention.handle( key, params );
			if ( result != null )
			{
				break;
			}
		}

		return result;
	}
}

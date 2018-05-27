package parser;

import java.util.HashSet;
import java.util.List;

import tools.ReflectionUtils;

public abstract class EnginePlugin {

	private MathEngine engine;
	protected HashSet<EnginePlugin> extentions = new HashSet<>();

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

	public Statement eval( String str )
	{
		return getEngine().eval( str );
	}

	public Statement eval( Object... objs )
	{
		return getEngine().eval( objs );
	}

	public Statement eval( List<Object> list )
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

	public Statement handle( MathParser parser )
	{
		return null;
	}

	public Statement handle( MathParser parser, Statement left )
	{
		Statement result = null;

		if ( left == null )
		{
			result = handle( parser );
		}

		if ( result == null )
		{
			for ( EnginePlugin extention : extentions )
			{
				result = ReflectionUtils.as( extention.handle( parser, left ), Statement.class );
				if ( result != null )
				{
					break;
				}
			}
		}

		return result;
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

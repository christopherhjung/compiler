package parser;

import java.util.Map;
import java.util.Set;

public class HybridMathParser extends MathParser {

	private Object[] objs;
	private Statement thermElement;
	private int objPosition = -1;
	private boolean isTherm = false;

	public HybridMathParser( Map<Integer, Set<EnginePlugin>> plugins )
	{
		super( plugins );
	}

	@Override
	public Statement parse()
	{
		Statement superTherm = super.parse();

		if ( isTherm && superTherm == null )
		{
			Statement temp = thermElement;
			next();
			return temp;
		}

		return superTherm;
	}

	@Override
	protected void reset()
	{
		super.reset();
		thermElement = null;
		objPosition = -1;
		isTherm = objs != null;
		next();
	}

	@Override
	public void next()
	{
		if ( isTherm || !super.hasNext() )
		{
			objPosition++;
			isTherm = false;
			if ( hasCurrent() )
			{
				Object current = objs[objPosition];
				if ( current instanceof Statement )
				{
					isTherm = true;
					thermElement = (Statement) current;
				}
				else if ( current instanceof char[] )
				{
					resetBuffer( (char[]) current );
				}
				else
				{
					resetBuffer( current.toString() );
				}
			}
		}
		else
		{
			super.next();
		}
	}

	@Override
	public boolean hasCurrent()
	{
		return objPosition < objs.length;
	}

	public Statement eval( Object[] objs )
	{
		this.objs = objs;
		reset();
		Statement result = parse();

		if ( hasNext() )
		{
			throw new ParseException( "Not all from input parsed :" + Character.toTitleCase( getChar()  ));
		}

		return result;
	}

	@Override
	public Statement eval( char[] chars )
	{
		return eval( new Object[] {
				chars
		} );
	}

	@Override
	public String toString()
	{
		return objs[objPosition].toString();
	}

	@Override
	protected RestoreAction getRestorePoint()
	{
		Statement thermElement = this.thermElement;
		int objPosition = this.objPosition;
		boolean isTherm = this.isTherm;

		RestoreAction action = super.getRestorePoint();

		return new RestoreAction() {

			@Override
			public void restore()
			{
				action.restore();
				HybridMathParser.this.thermElement = thermElement;
				HybridMathParser.this.objPosition = objPosition;
				HybridMathParser.this.isTherm = isTherm;
			}
		};
	}
	
	
}

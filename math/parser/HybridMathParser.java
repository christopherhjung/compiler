package parser;

import java.util.Map;
import java.util.Set;

import therms.Therm;

public class HybridMathParser extends MathParser {

	private Object[] objs;
	private Therm thermElement;
	private int objPosition = -1;
	private boolean isTherm = false;

	public HybridMathParser( Map<Integer, Set<EnginePlugin>> plugins )
	{
		super( plugins );
	}

	@Override
	public Therm parse()
	{
		Therm superTherm = super.parse();

		if ( isTherm && superTherm == null )
		{
			Therm temp = thermElement;
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
				if ( current instanceof Therm )
				{
					isTherm = true;
					thermElement = (Therm) current;
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

	public Therm eval( Object[] objs )
	{
		this.objs = objs;
		reset();
		return parse();
	}

	@Override
	public Therm eval( char[] chars )
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
		Therm thermElement = this.thermElement;
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

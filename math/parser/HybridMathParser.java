package parser;

import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
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
		isTherm = true;
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
				if ( objs[objPosition] instanceof Therm )
				{
					isTherm = true;
					thermElement = (Therm) objs[objPosition];
				}
				else
				{
					setChars( objs[objPosition].toString().toCharArray() );
					setPosition( 0 );
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
		return objs.length > objPosition;
	}

	public Therm eval( Object... objs )
	{
		this.objs = objs;
		reset();
		return parse();
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

		return new RestoreAction() {

			@Override
			public void restore()
			{
				HybridMathParser.this.thermElement = thermElement;
				HybridMathParser.this.objPosition = objPosition;
				HybridMathParser.this.isTherm = isTherm;
			}
		};
	}
}


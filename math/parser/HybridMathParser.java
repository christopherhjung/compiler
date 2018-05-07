package parser;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
import therms.Therm;

public class HybridMathParser extends MathParser {

	private Object[] objs;
	private Therm thermElement;
	private String stringElement;
	private int objPosition = -1;
	private int stringPosition = 0;
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
		stringElement = null;
		objPosition = -1;
		stringPosition = 0;
		isTherm = false;
		next();
	}

	@Override
	public void next()
	{
		stringPosition++;
		
		if ( stringElement == null && thermElement == null && hasNext() 
				|| isTherm || stringElement.length() <= stringPosition )
		{
			objPosition++;
			stringPosition = 0;
			isTherm = false;
			if ( hasNext() )
			{
				if ( objs[objPosition] instanceof Therm )
				{
					isTherm = true;
					thermElement = (Therm) objs[objPosition];
					stringElement = null;
				}
				else
				{
					stringElement = objs[objPosition].toString();
					thermElement = null;
				}
			}
		}
	}

	@Override
	public char getChar()
	{
		return stringElement == null ? 0 : stringElement.charAt( stringPosition );
	}

	@Override
	public boolean hasNext()
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
		String stringElement = this.stringElement;
		int objPosition = this.objPosition;
		int stringPosition = this.stringPosition;
		boolean isTherm = this.isTherm;	
		
		return new RestoreAction() {
			
			@Override
			public void restore()
			{
				HybridMathParser.this.thermElement = thermElement;
				HybridMathParser.this.stringElement = stringElement;
				HybridMathParser.this.objPosition = objPosition;
				HybridMathParser.this.stringPosition = stringPosition;
				HybridMathParser.this.isTherm = isTherm;	
			}
		};
	}
}

package parser;

import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
import therms.Therm;

public class HybridMathParser extends MathParser {

	private Object[] input;
	private Therm nextTherm;
	private String str;
	private int objPosition = 0;
	private int stringPos = 0;
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
			Therm temp = nextTherm;
			next();
			return temp;
		}

		return superTherm;
	}

	@Override
	public void next()
	{
		if ( isTherm || str.length() <= stringPos + 1 )
		{
			objPosition++;
			stringPos = 0;
			calc();
		}
		else
		{
			stringPos++;
		}
	}

	private void calc()
	{
		isTherm = false;
		if ( hasNext() )
		{
			if ( input[objPosition] instanceof Therm )
			{
				isTherm = true;
				nextTherm = (Therm) input[objPosition];
				str = null;
			}
			else
			{
				str = input[objPosition].toString();
				nextTherm = null;
			}
		}
	}

	@Override
	public char getChar()
	{
		return str == null ? 0 : str.charAt( stringPos );
	}

	@Override
	public boolean hasNext()
	{
		return input.length > objPosition;
	}

	public Therm eval( Object... objs )
	{
		input = objs;

		calc();

		resetLevel();
		return parse();
	}
	
	@Override
	public String toString()
	{
		return input[objPosition].toString();
	}
}

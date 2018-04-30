package parser;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import functions.EnginePlugin;
import therms.Therm;
import tools.Utils;

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
				isTherm = false;
				str = input[objPosition].toString();
				nextTherm = null;
			}
		}
	}

	@Override
	public char getChar()
	{
		return str.charAt( stringPos );
	}

	@Override
	public boolean hasNext()
	{
		return input.length > objPosition;
	}

	public Therm eval( Object... objs )
	{
		input = objs;
		System.out.println( Utils.concat( input, "" ) );

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

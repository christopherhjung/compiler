package parser;

public class ParseException extends RuntimeException {
	protected ParseException( MathParser engine )
	{
		this("Parse Error at :" + engine.getChar() );
	}

	protected ParseException( String comment )
	{
		super( comment );
	}
}
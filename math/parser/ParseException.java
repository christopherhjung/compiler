package parser;

public class ParseException extends RuntimeException {
	protected ParseException( MathEngine engine )
	{
		this("Unknow Signs left :" + engine.getChar() );
	}

	protected ParseException( String comment )
	{
		super( comment );
	}
}
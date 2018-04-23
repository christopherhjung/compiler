package parser;

public class ParseException extends RuntimeException {
	public <T> ParseException( StringParser<T> engine )
	{
		this("Parse Error at position " + engine.getPosition() + " \" " + engine.getChar() + " \"" );
		setStackTrace( Thread.currentThread().getStackTrace() );
		
		Thread.dumpStack();
	}

	protected ParseException( String comment )
	{
		super( comment );
	}
}
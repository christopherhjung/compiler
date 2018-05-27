package parser;

public class ParseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public <T> ParseException( StringParser<T> engine )
	{
		this("Parse Error at position -->" + engine.getChar() + "<--" );
		setStackTrace( Thread.currentThread().getStackTrace() );
		
		//Thread.dumpStack();
	}

	public ParseException( String comment )
	{
		super( comment );
	}
}
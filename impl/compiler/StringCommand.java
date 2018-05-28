package compiler;

import java.text.MessageFormat;

public class StringCommand extends AbstractCommand {
	private String inline;

	public StringCommand( String inline )
	{
		this.inline = inline;
	}

	@Override
	public String toString()
	{
		return inline + "\n";
	}

	public static StringCommand from( String format, Object... args )
	{
		return new StringCommand( MessageFormat.format( format, args ) );
	}
}

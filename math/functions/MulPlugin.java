package functions;

import builder.MultiplyBuilder;
import functions.DividePlugin.Divide;
import parser.MathParser;
import therms.Const;
import therms.Multiply;
import therms.Therm;

public class MulPlugin extends EnginePlugin{

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		MultiplyBuilder builder = new MultiplyBuilder();
		Therm first;

		if ( left == null )
		{
			first = parser.parse();
		}
		else
		{
			first = left;
		}

		builder.add( first );
		
		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case ' ':
					parser.next();
					break;

				case '*':
					parser.next();
				case '(':
					builder.add( parser.parse() );
					break;

				default:
					break loop;
			}
		}

		return builder.size() > 1 ? builder.build() : null;
	}
	
}

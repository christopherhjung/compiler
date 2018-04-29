package functions;

import builder.AdditionalBuilder;
import parser.MathParser;
import therms.Therm;

public class AddPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		AdditionalBuilder builder = new AdditionalBuilder();
		Therm therm;

		if ( left == null )
		{
			therm = parser.parse();
		}
		else
		{
			therm = left;
		}

		builder.add( therm );

		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case ' ':
					parser.next();
					break;

				case '+':
				case '-':
					builder.add( parser.parse() );
					break;

				default:
					break loop;
			}
		}

		return builder.size() > 1 ? builder.build() : null;
	}

}

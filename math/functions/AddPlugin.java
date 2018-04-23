package functions;

import builder.AdditionalBuilder;
import parser.MathParser;
import parser.ParseException;
import therms.Therm;

public class AddPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser engine )
	{
		if ( engine.getLevel() == 1 )
		{
			engine.setLevel( 2 );
			AdditionalBuilder builder = new AdditionalBuilder();
			builder.add( engine.parseTest() );

			loop: while ( engine.hasNext() )
			{
				switch ( engine.getChar() ) {
					case ' ':
						engine.next();
						break;

					case '}':
					case ')':
					case ',':
					case '=':
						break loop;

					case '+':
						engine.next();
					case '-':
						builder.add( engine.parseTest() );
						break;

					default:
						throw new ParseException( engine );
				}
			}

			return builder.build();
		}

		return null;
	}

}

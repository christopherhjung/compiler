package functions;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import parser.MathParser;
import parser.ParseException;
import therms.Const;
import therms.Therm;

public class MulPlugin extends EnginePlugin{

	@Override
	public Therm handle( MathParser engine )
	{
		if(engine.getLevel() == 2){
			engine.setLevel( 3 );
			MultiplyBuilder builder = new MultiplyBuilder();
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
					case '+':
					case '-':
					case '=':
						break loop;

					case '/':
						engine.next();
						builder.add( Const.ONE.div( engine.parseTest() ) );
						break;

					case '*':
						engine.next();
					case '(':
					default:
						builder.add( engine.parseTest() );
						break;
				}
			}

			return builder.build();
		}

		return null;
	}
	
}

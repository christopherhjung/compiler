package functions;

import builder.AdditionalBuilder;
import parser.MathParser;
import parser.ParseException;
import therms.Const;
import therms.Therm;

public class ConstPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser engine )
	{
		if ( engine.is( Character::isDigit ) )
		{
			StringBuilder builder = new StringBuilder();
			while ( engine.isDigit() )
			{
				builder.append( engine.nextChar() );
			}

			if ( engine.is( '.' ) )
			{
				builder.append( engine.nextChar() );

				while ( engine.isDigit() )
				{
					builder.append( engine.nextChar() );
				}
			}

			return new Const( Double.parseDouble( builder.toString() ) );
		}

		return null;
	}

}

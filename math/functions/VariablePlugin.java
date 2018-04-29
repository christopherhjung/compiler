package functions;

import parser.MathParser;
import therms.Therm;
import therms.Variable;

public class VariablePlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		if(parser.is( Character::isAlphabetic )){
			StringBuilder builder = new StringBuilder();
			while ( parser.is( Character::isAlphabetic ) )
			{
				builder.append( parser.nextChar() );
			}

			return new Variable( builder.toString() );			
		}
		
		return null;
	}

}

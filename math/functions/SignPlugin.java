package functions;

import javax.swing.text.AbstractDocument.LeafElement;

import parser.MathParser;
import therms.Const;
import therms.Therm;

public class SignPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser )
	{
		parser.eat( ' ' );
		boolean invert = false;
		if ( parser.eat( '-' ) )
		{
			invert = true;
		} 
		else
		{
			parser.eat( '+' );
		}

		Therm therm = parser.parse();

		if ( invert )
		{
			therm = Const.MINUS_ONE.mul( therm );
		}

		return therm;
	}

}

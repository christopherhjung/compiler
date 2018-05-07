package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class DividePlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		if ( parser.eat( '/' ) )
		{
			return new Divide( left, parser.parse() );
		}

		return null;
	}

	public static class Divide extends Therm {

		Therm numerators;
		Therm denominators;

		public Divide( Therm numerators, Therm denominators )
		{
			this.numerators = numerators;
			this.denominators = denominators;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "numerators" ) )
			{
				return numerators;
			}
			else if ( key.equals( "denominators" ) )
			{
				return denominators;
			}
			else if ( key.equals( "type" ) )
			{
				return "divide";
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( numerators );
			builder.append( "/" );
			builder.append( denominators );
		}

		@Override
		public int getLevel()
		{
			return EXPONENT_LEVEL;
		}
	}
}

package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Const;
import therms.Therm;
import therms.VarSet;

public class DividePlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm therm;

		if ( left != null )
		{
			therm = left;
		}
		else
		{
			therm = parser.parse();
		}

		if ( parser.eat( '/' ) )
		{
			therm = new Divide( therm, parser.parse() );
			return therm;
		}

		return null;
	}

	public static class Divide extends Therm {

		Therm zaehler;
		Therm nenner;

		public Divide( Therm zaehler, Therm nenner )
		{
			this.zaehler = zaehler;
			this.nenner = nenner;
		}

		@Override
		public Therm reduce( VarSet varSet, Therm... therms )
		{
			Therm reducedZaehler = zaehler.reduce( varSet, therms );
			Therm reducedNenner = nenner.reduce( varSet, therms );
			
			if( reducedZaehler instanceof Const && reducedNenner instanceof Const ){
				Const a = (Const) reducedZaehler;
				Const b = (Const) reducedNenner;
				
				Const r = new Const(a.getValue()/b.getValue());
				return r;
			}
			
			return new Divide(reducedZaehler,reducedNenner);
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( zaehler );
			builder.append( "/" );
			builder.append( nenner );
		}

		@Override
		public int getLevel()
		{
			return EXPONENT_LEVEL;
		}
	}
}

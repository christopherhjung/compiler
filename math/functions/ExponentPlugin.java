package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Const;
import therms.Exponenional;
import therms.Therm;
import therms.VarSet;
import tools.Utils;

public class ExponentPlugin extends EnginePlugin {

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

		if ( parser.eat( '^' ) )
		{
			therm = new Exponenional( therm, parser.parse() );
			return therm;
		}

		return null;
	}

	public class Exponenional extends Therm {

		private final Therm basis;
		private final Therm exponent;

		public Exponenional( Therm basis, Therm exponent )
		{
			this.basis = basis;
			this.exponent = exponent;
		}

		public Therm getBasis()
		{
			return basis;
		}

		public Therm getExponent()
		{
			return exponent;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				return eval( this, "*", "derivate(", exponent, "*", "log(", basis, "),", Utils.concat( params, "," ),
						")" );
			}
			else if ( key.equals( "reduce" ) )
			{
				Therm reducedBasis = (Therm) basis.execute( "reduce" );
				Therm reducedExponent = (Therm) exponent.execute( "reduce" );

				if ( params.length == 0 )
				{
					if ( reducedBasis.is( "const" ) && reducedExponent.is( "const" ) )
					{
						Double basisValue = reducedBasis.get( "value", Double.class );
						Double exponentValue =  reducedExponent.get( "value", Double.class );
						return eval(Math.pow( basisValue, exponentValue ));
					}

					return new Exponenional( reducedBasis, reducedExponent );
				}
			}

			return super.execute( key, params );
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( !(obj instanceof Exponenional) ) return false;
			Exponenional other = (Exponenional) obj;

			return basis.equals( other.basis ) && exponent.equals( other.exponent );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( basis );
			builder.append( " ^ " );
			builder.append( exponent );
		}

		@Override
		public int getLevel()
		{
			return EXPONENT_LEVEL;
		}
	}

}

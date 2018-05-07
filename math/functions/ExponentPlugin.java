package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

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
			if ( key.equals( "type" ) )
			{
				return "exponent";
			}
			else if ( key.equals( "basis" ) )
			{
				return basis;
			}
			else if ( key.equals( "exponent" ) )
			{
				return exponent;
			}
			else if ( key.equals( "do" ) )
			{
				return eval( basis.execute( "do" ), "^", exponent.execute( "do" ) );
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

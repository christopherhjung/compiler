package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Const;
import therms.Exponenional;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.Utils;

public class ExponentPlugin extends EnginePlugin {

	@Override
	public void onAttach( MathParser engine )
	{
		// TODO Auto-generated method stub
		super.onAttach( engine );
	}

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

	public static class Exponenional extends Therm {

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
			System.out.println( key );
			if( key.equals( "derivate" ) ){
				StringBuilder sb = new StringBuilder();
				sb.append( "derivate(" );
				sb.append( exponent );
				sb.append( '*' );
				sb.append( "log(" );
				sb.append( basis );
				sb.append( ')' );
				sb.append( ',' );
				Utils.concat( sb, params , ",");
				sb.append( ')' );	
				return sb.toString();
			}
			
			return super.execute( key, params );
		}
		
		@Override
		public Therm derivate( Variable name )
		{
			
			
			
			

			

			return exponent.mul( basis.derivate( name ) ).mul( basis.pow( exponent.add( Const.MINUS_ONE ) ) );
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
		public Therm reduce( VarSet varSet, Therm... therms )
		{
			Therm reducedBasis = basis.reduce( varSet );
			Therm reducedExponent = exponent.reduce( varSet );

			if ( therms.length == 0 )
			{
				if ( reducedBasis instanceof Const && reducedExponent instanceof Const )
				{
					Const basisValue = (Const) reducedBasis;
					Const exponentValue = (Const) reducedExponent;
					return new Const( Math.pow( basisValue.getValue(), exponentValue.getValue() ) );
				}

				return new Exponenional( reducedBasis, reducedExponent );
			}

			throw new IllegalArgumentException( "Wrong Arguments" );
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

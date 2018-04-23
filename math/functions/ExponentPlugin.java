package functions;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Const;
import therms.Exponenional;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class ExponentPlugin extends EnginePlugin{

	@Override
	public void enable( MathParser engine )
	{
		// TODO Auto-generated method stub
		super.enable( engine );
	}
	
	@Override
	public Therm handle( MathParser engine )
	{
		Therm basis = engine.parseTest(1);
		engine.eatAll( ' ' );
		if(!engine.eat( '^' )) return null;
		engine.eatAll( ' ' );
		Therm exponent = engine.parseTest(1);
		
		if(basis == null || exponent == null) return null;
		
		return new Exponenional( basis, exponent );
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
		public Therm derivate( Variable name )
		{
			/*
			 * if ( false && exponent.contains( name ) ) { //return mul(
			 * exponent.mul( new Chain( Functions.LOG.getTherm(), basis )
			 * ).derivate( name ) ); } else { }
			 */

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

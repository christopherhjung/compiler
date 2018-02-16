package functions;

import parser.ThermStringify;
import therms.Const;
import therms.Exponenional;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public enum Functions {
	SIN(new Function() {

		@Override
		public Therm derivate( Variable name )
		{
			return Functions.COS.getTherm();
		}

		@Override
		public double valueAt( VarSet varSet )
		{
			return Math.sin( varSet.getValue( Variable.X ) );
		}

		@Override
		public boolean contains( Therm var )
		{
			return var.equals( Variable.X );
		}

		@Override
		public void toString( ThermStringify builder )
		{
			builder.append( "sin( x )" );
		}
	}), COS(new Function() {

		Therm therm = Const.MINUS_ONE.mul( Functions.SIN.getTherm() );

		@Override
		public Therm derivate( Variable name )
		{
			return therm;
		}

		@Override
		public double valueAt( VarSet varSet )
		{
			return Math.cos( varSet.getValue( Variable.X ) );
		}

		@Override
		public boolean contains( Therm var )
		{
			return var.equals( Variable.X );
		}

		@Override
		public void toString( ThermStringify builder )
		{
			builder.append( "cos( x )" );
		}
	}),

	TAN(new Function() {
		Therm therm = Functions.COS.getTherm().pow( Const.MINUS_TWO );

		@Override
		public Therm derivate( Variable name )
		{
			return therm;
		}

		@Override
		public double valueAt( VarSet varSet )
		{
			return Math.tan( varSet.getValue( Variable.X ) );
		}

		@Override
		public boolean contains( Therm var )
		{
			return var.equals( Variable.X );
		}

		@Override
		public void toString( ThermStringify builder )
		{
			builder.append( "tan( x )" );
		}
	}), SQRT(new Exponenional( Variable.X, new Const( 0.5 ) )), EXP(new Function() {

		@Override
		public Therm derivate( Variable name )
		{
			return this;
		}

		@Override
		public double valueAt( VarSet varSet )
		{
			return Math.exp( varSet.getValue( Variable.X ) );
		}

		@Override
		public boolean contains( Therm var )
		{
			return var.equals( Variable.X );
		}

		@Override
		public void toString( ThermStringify builder )
		{
			builder.append( "exp( x )" );
		}
	}), LOG(new Function() {

		@Override
		public Therm derivate( Variable name )
		{
			return Const.ONE.div( Variable.X );
		}

		@Override
		public double valueAt( VarSet varSet )
		{
			return Math.log( varSet.getValue( Variable.X ) );
		}

		@Override
		public boolean contains( Therm var )
		{
			return var.equals( Variable.X );
		}

		@Override
		public void toString( ThermStringify builder )
		{
			builder.append( "log( x )" );
		}
	});

	private Therm therm;

	Functions( Therm therm )
	{
		this.therm = therm;
	}

	public Therm getTherm()
	{
		return therm;
	}
}

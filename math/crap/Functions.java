package crap;

import functions.ConstPlugin.Const;
import functions.ExponentPlugin.Exponenional;
import functions.VariablePlugin.Variable;
import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import therms.VarSet;

public enum Functions {
	SQRT(new Exponenional( Variable.X, new Const( 0.5 ) )), 
	
	EXP(new EnginePlugin() {

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
		public void toString( ThermStringifier builder )
		{
			builder.append( "exp( x )" );
		}
	}), LOG(new EnginePlugin() {

		@Override
		public Therm derivate( Variable name )
		{
			return new MathParser().eval("1/x");
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
		public void toString( ThermStringifier builder )
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

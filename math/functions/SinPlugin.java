package functions;

import parser.MathParser;
import parser.ThermStringify;
import therms.Chain;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;

public class SinPlugin extends EnginePlugin {

	private Therm derivate = null;
	
	@Override
	public void enable( MathParser engine )
	{
		derivate = engine.eval( "cos(x)" );
	}
	
	@EngineExecute
	public Therm execute( Therm a )
	{
		return new Sin(a);
	}
	
	public class Sin extends Therm {
		
		private Therm inner;
		
		private Sin( Therm inner )
		{
			this.inner = inner;
		}
		
		@Override
		public Therm derivate( Variable name )
		{
			return derivate;
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
	}
}
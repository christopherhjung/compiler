package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class ObjectPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "object";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null || !left.is( "variable" ) )
		{
			return null;
		}

		if ( parser.eat( '.' ) )
		{

			Therm right = parser.parse();

			if ( right != null )
			{
				return new Obj( left, right );
			}
		}

		return null;

	}

	private class Obj extends Therm {
		private Therm left;
		private Therm right;

		public Obj( Therm left, Therm right )
		{
			this.left = left;
			this.right = right;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( left );
			builder.append( "." );
			builder.append( right );
		}
	}
}

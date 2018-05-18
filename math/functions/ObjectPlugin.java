package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
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

		parser.eatAll( ' ' );
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

	private class Obj extends BiTherm {
		public Obj( Therm left, Therm right )
		{
			super( left, right, "." );
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return ObjectPlugin.this;
		}
	}
}

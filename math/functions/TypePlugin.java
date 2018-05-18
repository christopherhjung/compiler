package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.BiTherm;
import therms.Therm;

public class TypePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "declare";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null || !left.is( "variable" ) )
		{
			return null;
		}

		if ( parser.eatAll( ' ' ) )
		{
			Therm variable = parser.parse();

			if ( variable != null && variable.is( "variable" ) )
			{
				return new Declaration( left, variable );
			}
		}

		return null;
	}

	private class Declaration extends BiTherm {

		public Declaration( Therm type, Therm variable )
		{
			super( type, variable, " " );
		}
		
		@Override
		public String getType()
		{
			return "declare";
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return TypePlugin.this;
		}
	}
}

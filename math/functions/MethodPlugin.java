package functions;

import java.util.ArrayList;
import java.util.List;

import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class MethodPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			List<Therm> therms = new ArrayList<>();

			if ( parser.eat( '(' ) )
			{

				while ( parser.isNot( ')' ) )
				{
					Therm param = parser.parse();

					if ( !param.is( "variable" ) )
					{
						return null;
					}

					therms.add( param );
					parser.eat( ',' );
				}

				parser.eat( ')' );
			}

			left = new VariablePlugin().new Variable( therms.toString() );
		}

		if ( parser.eat( "->" ) )
		{
			Therm therm = parser.parseWithLevelReset();

			return new Method( left, therm );
		}

		return super.handle( parser );
	}
	
	public static class Reference extends Therm{

		public String alias;
		
		@Override
		public void toString( ThermStringifier builder )
		{
			
		}
	}

	public static class Method extends Therm {

		private Therm var;
		private Therm inner;

		public Method( Therm var, Therm inner )
		{
			this.var = var;
			this.inner = inner;
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( var );
			builder.append( "->" );
			builder.append( inner );
		}

		@Override
		public int getLevel()
		{
			return ZERO_LEVEL;
		}
	}
}

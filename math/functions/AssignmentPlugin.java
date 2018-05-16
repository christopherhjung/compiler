package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;

public class AssignmentPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "assign";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		if ( parser.eat( '=' ) )
		{
			Therm right = parser.parse();

			return new Assignment( left, right );
		}

		return null;
	}

	public class Assignment extends Therm {

		Therm left;
		Therm right;

		public Assignment( Therm left, Therm right )
		{
			this.left = left;
			this.right = right;
		}
		
		@Override
		public EnginePlugin getPlugin()
		{
			return AssignmentPlugin.this;
		}

		@Override
		public String getType()
		{
			return "assignment";
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "left" ) )
			{
				return left;
			}
			else if ( key.equals( "right" ) )
			{
				return right;
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( left );
			builder.append( "=" );
			builder.append( right );
		}
	}
}

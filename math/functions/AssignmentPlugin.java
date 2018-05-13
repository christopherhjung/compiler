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

	public static class Assignment extends Therm {

		Therm left;
		Therm right;

		public Assignment( Therm left, Therm right )
		{
			this.left = left;
			this.right = right;
		}
		
		@Override
		public Object execute( String key, Object... params )
		{
			if( key.equals( "insert" ) ){
				Therm newRight = (Therm)right.execute( "insert" );
				left.execute( "assign", newRight );
				return newRight;
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

		@Override
		public int getLevel()
		{
			return EQUATION_LEVEL;
		}
	}
}

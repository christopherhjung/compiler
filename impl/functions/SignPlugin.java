package functions;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class SignPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "sign";
	}

	@Override
	public Statement handle( MathParser parser )
	{
		parser.eatAll( ' ' );
		boolean invert = false;
		if ( parser.eat( '-' ) )
		{
			invert = true;
		}
		else
		{
			parser.eat( '+' );
		}

		Statement therm = parser.parse();

		if ( invert && therm != null )
		{
			Statement negate = (Statement) handle( "negate", therm );

			
			if ( negate == null )
			{
				therm = new Negate( therm );
			}
			else
			{
				therm = negate;
			}
		}

		return therm;
	}

	public class Negate extends Statement {

		private Statement therm;

		public Negate( Statement therm )
		{
			this.therm = therm;
		}

		@Override
		public Object getImpl( String key)
		{
			if ( key.equals( "type" ) )
			{
				return "negate";
			}
			else if ( key.equals( "value" ) )
			{
				return therm;
			}

			return super.getImpl( key );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( "-" );
			builder.append( therm );
		}
	}
}

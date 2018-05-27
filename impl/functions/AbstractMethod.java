package functions;

import parser.Statement;
import parser.StatementStringifier;

public abstract class AbstractMethod extends Statement {

	@Override
	public Object get( String key, Object... params )
	{
		if ( key.equals( "call" ) )
		{
			Statement[] therms = new Statement[params.length];
			for ( int i = 0 ; i < params.length ; i++ )
			{
				therms[i] = (Statement) params[i];
			}
			
			return call( therms );
		}

		return super.get( key, params );
	}

	public abstract Statement call( Statement[] params );

	@Override
	public String getType()
	{
		return "method";
	}

	@Override
	public void toString( StatementStringifier builder )
	{
		// TODO Auto-generated method stub

	}

}

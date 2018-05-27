package compiler;

import java.util.Arrays;

import parser.Statement;

public class Compiler {

	public String compile( Statement statement )
	{
		if(true)
			return null;
		if ( statement.is( "block" ) )
		{
			Statement methodHead = statement.get( "left", Statement.class );
			Statement[] right = statement.get( "right", Statement[].class );

			Statement[] modifier = methodHead.get( "left", Statement[].class );
			Statement methodSignature = methodHead.get( "right", Statement.class );
			
			Statement methodName = methodSignature.get( "left", Statement.class );
			Statement params = methodSignature.get( "right", Statement.class );
		
		}

		return null;
	}
}

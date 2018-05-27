package functions;

import java.util.ArrayList;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class DeclarePlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "declare";
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left == null )
		{
			return null;
		}

		if ( !left.is( "name" ) )
		{
			return null;
		}

		if ( !parser.eatAll( ' ' ) )
		{
			return null;
		}

		List<Statement> test = new ArrayList<>();
		test.add( left );

		while ( true )
		{
			left = parser.parse();
			if ( left == null )
			{
				break;
			}

			test.add( left );

			if ( !left.is( "name" ) || !parser.eatAll( ' ' ) )
			{
				break;
			}
		}

		if ( test.size() <= 1 )
		{
			return null;
		}

		Statement right = test.remove( test.size() - 1 );
		Statement[] modifier = test.toArray(new Statement[test.size()]);
		
		return new Declare( modifier, right );
	}

	public class Declare extends Statement {

		private Statement condition;
		private Statement[] modifier;

		public Declare( Statement[] modifier, Statement condition )
		{
			this.condition = condition;
			this.modifier = modifier;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "right" ) )
			{
				return condition;
			}
			else if ( key.equals( "left" ) )
			{
				return modifier;
			}
			else if ( key.equals( "size" ) )
			{
				return 2;
			}
			
			return super.get( key, params );
		}
		
		@Override
		public EnginePlugin getPlugin()
		{
			return DeclarePlugin.this;
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.append( modifier, " " );
			builder.append( " " );
			builder.append( condition );
		}

	}

}

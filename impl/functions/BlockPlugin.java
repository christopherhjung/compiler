package functions;

import java.util.ArrayList;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Statement;
import parser.StatementStringifier;

public class BlockPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "block";
	}

	public String[] required()
	{
		return new String[] {
				"assign", "declare", "name"
		};
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		parser.eatWhile( Character::isWhitespace );
		if ( parser.eat( '{' ) )
		{
			ArrayList<Statement> statements = new ArrayList<>();

			parser.eatWhile( Character::isWhitespace );
			while ( parser.isNot( '}' ) )
			{
				parser.eatWhile( Character::isWhitespace );
				Statement statement = parser.parseWithLevelReset();
				if ( statement == null )
				{
					return null;
				}
				statements.add( statement );

				parser.eatWhile( Character::isWhitespace );
				parser.eatAll( ';' );
				parser.eatWhile( Character::isWhitespace );
			}

			parser.eat( '}' );
			return new Block( left, statements.toArray( new Statement[statements.size()] ) );
		}

		return null;
	}

	public class Block extends Statement {

		private Statement condition;
		private Statement[] commands;

		public Block( Statement condition, Statement[] commands )
		{
			this.condition = condition;
			this.commands = commands;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return BlockPlugin.this;
		}
		
		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "left" ) )
			{
				return condition;
			}
			else if ( key.equals( "right" ) )
			{
				return commands;
			}
			else if ( key.equals( "size" ) )
			{
				return 2;
			}
			
			return super.get( key, params );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			builder.incLevel();
			int temp = builder.resetLast();

			if ( condition != null )
			{
				builder.append( condition );
			}

			builder.append( "{\n" );
			for ( Statement statement : commands )
			{
				builder.append( "\t", builder.getLevel() );
				builder.append( statement );
				builder.append( "\n" );
			}
			builder.decLevel();
			builder.append( "\t", builder.getLevel() );
			builder.append( "}" );

			builder.setLast( temp );
		}

	}
}

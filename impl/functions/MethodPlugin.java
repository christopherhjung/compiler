package functions;

import java.util.HashMap;

import parser.EnginePlugin;
import parser.MathParser;
import parser.Space.Scope;
import parser.Statement;
import parser.StatementStringifier;

public class MethodPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "method";
	}

	@Override
	public Statement handle( MathParser parser, Statement left )
	{
		if ( left == null || !left.is( "name" ) && !left.is( "vector" ) )
		{
			return null;
		}

		parser.eatAll( ' ' );
		if ( parser.eat( "->" ) )
		{
			Statement therm = parser.parseWithLevelReset();

			return new Method( left, therm );
		}

		return super.handle( parser );
	}

	public class Method extends AbstractMethod {

		private Statement vector;
		private Statement inner;

		public Method( Statement vector, Statement inner )
		{
			this.vector = vector;
			this.inner = inner;
		}

		@Override
		public EnginePlugin getPlugin()
		{
			return MethodPlugin.this;
		}

		@Override
		public Statement call( Statement[] params )
		{
			/*if ( vars.length == params.length )
			{
				getPlugin().getEngine().enterScope( new Scope() {
					@Override
					public Statement get( Object key )
					{
						if ( varSet.containsKey( key ) )
						{
							return (Statement) params[varSet.get( getKey( key ) )];
						}

						return super.get( key );
					}
				} );

				Statement result = eval( "update(", inner, ")" );

				getPlugin().getEngine().leaveScope();

				return result;
			}

			return null;*/
			return null;
		}

		@Override
		public Object get( String key, Object... params )
		{
			if ( key.equals( "type" ) )
			{
				return "method";
			}
			else if ( key.equals( "value" ) )
			{
				return inner;
			}
			else if ( key.equals( "params" ) )
			{
				return vector;
			}

			return super.get( key, params );
		}

		@Override
		public void toString( StatementStringifier builder )
		{
			int temp = builder.resetLast();
			builder.append( vector );
			builder.append( " -> " );
			builder.append( inner );
			builder.setLast( temp );
		}
	}
}

package functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.MathEngine;
import parser.MathParser;
import parser.Space.Scope;
import parser.ThermStringifier;
import therms.Therm;

public class MethodPlugin extends EnginePlugin {

	private Scope currentScope = null;

	@Override
	public void reset()
	{
		currentScope = getEngine().globalScope;
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		Therm[] vars;

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
				vars = therms.toArray( new Therm[therms.size()] );
			}
			else
			{
				return null;
			}
		}
		else
		{
			if ( !left.is( "variable" ) )
			{
				return null;
			}

			vars = new Therm[] {
					left
			};
		}

		if ( parser.eat( "->" ) )
		{
			Therm therm = parser.parseWithLevelReset();

			return new Method( vars, therm );
		}

		return super.handle( parser );
	}

	public class Method extends AbstractMethod {

		private Therm[] vars;
		private Therm inner;
		private HashMap<Therm, Integer> varSet;

		public Method( Therm[] vars, Therm inner )
		{
			this.vars = vars;
			this.inner = inner;
			this.varSet = new HashMap<>();

			for ( int i = 0 ; i < vars.length ; i++ )
			{
				varSet.put( vars[i], i );
			}
		}

		@Override
		public MathEngine getEngine()
		{
			return MethodPlugin.this.getEngine();
		}

		@Override
		public Therm call( Therm[] params )
		{
			if ( vars.length == params.length )
			{
				Scope old = getEngine().globalScope;
				getEngine().globalScope = new Scope( old ) {
					@Override
					public Therm get( Object key )
					{
						if ( varSet.containsKey( key ) )
						{
							return (Therm) params[varSet.get( key )];
						}

						return super.get( key );
					}
				};

				Therm result = (Therm) inner.execute( "do" );

				getEngine().globalScope = old;

				return result;
			}
			
			return null;
		}
		
		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "do" ) )
			{
				Scope old = getEngine().globalScope;
				getEngine().globalScope = new Scope( old ) {
					@Override
					public Therm get( Object key )
					{
						if ( varSet.containsKey( key ) )
						{
							return (Therm) key;
						}

						return super.get( key );
					}
				};

				Therm result = (Therm) inner.execute( "do" );

				getEngine().globalScope = old;
				return new Method( vars, result );
			}
			else if ( key.equals( "type" ) )
			{
				return "method";
			}
			else if ( key.equals( "value" ) )
			{
				return inner;
			}
			else if ( key.equals( "param" ) )
			{
				return vars[0];
			}

			return super.execute( key, params );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			if ( vars.length != 1 )
			{
				builder.append( "(" );
			}

			for ( int i = 0 ; i < vars.length ; i++ )
			{
				if ( i > 0 )
				{
					builder.append( "," );
				}

				builder.append( vars[i] );
			}

			if ( vars.length != 1 )
			{
				builder.append( ")" );
			}

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

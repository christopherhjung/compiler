package functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.EnginePlugin;
import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import parser.PluginExtention;
import parser.Space.Scope;
import parser.ThermStringifier;
import therms.Therm;
import tools.Run;
import tools.Utils;

public class MethodPlugin extends EnginePlugin {

	@Override
	public String getName()
	{
		return "method";
	}

	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );

		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "function.variable.method";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				Therm therm = getEngine().currentScope.get( key );
				if ( therm != null )
				{
					therm = (Therm) therm.execute( "call", params );
				}
				return therm;
			}
		} );
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
		private Scope scope;
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
		public EnginePlugin getPlugin()
		{
			return MethodPlugin.this;
		}

		@Override
		public Therm call( Therm[] params )
		{
			if ( vars.length == params.length )
			{
				getPlugin().getEngine().enterScope( new Scope() {
					@Override
					public Therm get( Object key )
					{
						if ( varSet.containsKey( key ) )
						{
							return (Therm) params[varSet.get( key )];
						}

						return super.get( key );
					}
				} );

				Therm result = eval("update(",inner,")");

				getPlugin().getEngine().leaveScope();

				return result;
			}

			return null;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			/*if ( key.equals( "insert" ) )
			{
				getEngine().enterScope( new Scope( getEngine().currentScope ) {
					@Override
					public Therm get( Object key )
					{
						if ( varSet.containsKey( key ) )
						{
							return (Therm) key;
						}

						return super.get( key );
					}
				} );
				
				Therm result = (Therm) inner.execute( "insert" );

				getEngine().leaveScope();
				return new Method( vars, result );
			}
			else */if ( key.equals( "type" ) )
			{
				return "method";
			}
			else if ( key.equals( "value" ) )
			{
				return inner;
			}
			else if ( key.equals( "params" ) )
			{
				return vars;
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
			
			Utils.alternating( vars, ",", o -> builder.append( o ) );

			if ( vars.length != 1 )
			{
				builder.append( ")" );
			}

			builder.append( "->" );
			builder.append( inner );
		}
	}
}

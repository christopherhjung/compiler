package functions;

import parser.EnginePlugin;
import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;

public class ConstPlugin extends SinglePlugin {


	public ConstPlugin( )
	{
		super( "const", "", "" );
	}

	@Override
	protected void onCreate( MathProgram program )
	{
		super.onCreate( program );

		program.installPlugin( () -> new EnginePlugin() {

			@Override
			public String getName()
			{
				return "sign.const";
			}

			@Override
			public Object handle( String key, Object... params )
			{
				Therm therm = (Therm) params[0];
				if ( key.equals( "negate" ) && therm.is( "const" ) )
				{
					Double value = therm.get( "value", Double.class );

					return create( -value );
				}

				return super.handle( key, params );
			}
		} );
	}

	@Override
	public Therm handle( MathParser parser )
	{
		StringBuilder builder = new StringBuilder();

		parser.eatAll( ' ' );
		if ( parser.is( Character::isDigit ) )
		{
			while ( parser.is( Character::isDigit ) )
			{
				builder.append( parser.nextChar() );
			}

			if ( parser.is( '.' ) )
			{
				builder.append( parser.nextChar() );

				while ( parser.is( Character::isDigit ) )
				{
					builder.append( parser.nextChar() );
				}
			}

			return create(Double.parseDouble( builder.toString() ));
		}

		return null;
	}

}

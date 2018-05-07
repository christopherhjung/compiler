package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import builder.AdditionalBuilder;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Additional;
import therms.Therm;
import tools.ListComparer;

public class AddPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		List<Therm> builder = new ArrayList<Therm>();

		builder.add( left );

		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case ' ':
					parser.next();
					break;

				case '+':
				case '-':
					builder.add( parser.parse() );
					break;

				default:
					break loop;
			}
		}

		return builder.size() > 1 ? new Additional( builder ) : null;
	}

	public class Additional extends Therm implements Iterable<Therm> {

		private final List<Therm> therms;

		public Additional()
		{
			therms = new ArrayList<>();
		}

		public <T extends Therm> Additional( T... therms )
		{
			this.therms = Arrays.asList( therms );
		}

		public Additional( List<? extends Therm> therms )
		{
			this.therms = new ArrayList<>( therms );
		}

		public List<Therm> getTherms()
		{
			return therms;
		}

		@Override
		public Object execute( String key, Object... params )
		{
			if ( key.equals( "derivate" ) )
			{
				List<Object> builder = new ArrayList<>();
				for ( Therm therm : this )
				{
					if ( builder.size() > 0 )
					{
						builder.add( '+' );
					}

					builder.add( therm.execute( key, params ) );
				}

				return eval( builder );
			}
			else if ( key.equals( "type" ) )
			{
				return "add";
			}
			else if ( key.equals( "value" ) )
			{
				return therms;
			}
			else if ( key.equals( "do" ) )
			{
				List<Object> list = new ArrayList<>();

				for ( int i = 0 ; i < therms.size() ; i++ )
				{
					if ( i > 0 )
					{
						list.add( "+" );
					}

					list.add( therms.get( i ).execute( "do" ) );
				}

				return eval(list);
			}

			return super.execute( key, params );
		}

		private int size()
		{
			return therms.size();
		}

		@Override
		public Iterator<Therm> iterator()
		{
			return therms.iterator();
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( super.equals( obj ) ) return true;
			if ( !(obj instanceof Additional) ) return false;

			Additional other = (Additional) obj;
			return ListComparer.containsSame( therms, other.therms );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( therms, " + " );
		}

		@Override
		public int getLevel()
		{
			return ADDITION_LEVEL;
		}
	}

}

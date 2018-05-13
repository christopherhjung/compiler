package functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import parser.EnginePlugin;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Therm;
import tools.ListComparer;

public class MulPlugin extends EnginePlugin {
	
	@Override
	public String getName()
	{
		return "mul";
	}

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}

		ArrayList<Therm> builder = new ArrayList<Therm>();

		builder.add( left );

		loop: while ( parser.hasCurrent() )
		{
			switch ( parser.getChar() ) {
				case '*':
					parser.next();
					builder.add( parser.parse() );
					break;

				default:
					break loop;
			}
		}

		return builder.size() > 1 ? new Multiply( builder ) : null;
	}

	public class Multiply extends Therm implements Iterable<Therm> {

		private List<Therm> therms;

		public Multiply()
		{
			this.therms = new ArrayList<>();
		}

		public Multiply( Collection<? extends Therm> therms )
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
			if ( key.equals( "type" ) )
			{
				return "mul";
			}
			else if ( key.equals( "value" ) )
			{
				return therms;
			}
			else if ( key.equals( "insert" ) )
			{
				List<Object> list = new ArrayList<>();

				for ( int i = 0 ; i < therms.size() ; i++ )
				{
					if ( i > 0 )
					{
						list.add( "*" );
					}

					list.add( therms.get( i ).execute( "insert" ) );
				}

				return eval( list );
			}

			return super.execute( key, params );
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
			if ( !(obj instanceof Multiply) ) return false;
			Multiply other = (Multiply) obj;

			return ListComparer.containsSame( therms, other.therms );
		}

		@Override
		public void toString( ThermStringifier builder )
		{
			builder.append( therms, " * " );
		}

		@Override
		public int getLevel()
		{
			return MULTIPLY_LEVEL;
		}
	}

}

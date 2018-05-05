package functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import builder.MultiplyBuilder;
import parser.MathParser;
import parser.ThermStringifier;
import therms.Multiply;
import therms.Therm;
import therms.VarSet;
import tools.ListComparer;

public class MulPlugin extends EnginePlugin {

	@Override
	public Therm handle( MathParser parser, Therm left )
	{
		if ( left == null )
		{
			return null;
		}
		
		ArrayList<Therm> builder = new ArrayList<Therm>();
		
		builder.add( left );

		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case ' ':
					parser.next();
					break;

				case '*':
					parser.next();
				case '(':
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
			if ( key.equals( "derivate" ) )
			{
				ArrayList<Object> list = new ArrayList<>();
				for ( int i = 0 ; i < therms.size() ; i++ )
				{
					if ( i > 0 )
					{
						list.add( '+' );
					}

					for ( int j = 0 ; j < therms.size() ; j++ )
					{
						if ( j > 0 )
						{
							list.add( '*' );
						}

						if ( i == j )
						{
							list.add( therms.get( j ).execute( key, params ) );
						}
						else
						{
							list.add( therms.get( j ) );
						}
					}
				}

				return eval( list );
			}
			else if ( key.equals( "reduce" ) )
			{
				Therm therm = (Therm) therms.get( 0 ).execute( "reduce" );

				for ( int i = 1 ; i < therms.size() ; i++ )
				{
					therm = (Therm) therm.execute( "mulreduce", therms.get( i ).execute( "reduce" ) );
				}

				return therm;
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

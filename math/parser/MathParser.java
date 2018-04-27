package parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import functions.BasicPlugin;
import functions.EngineExecute;
import functions.EnginePlugin;

import matrix.Matrix;
import matrix.Vector;
import therms.Chain;
import therms.Const;
import therms.Equation;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;
import tools.Run;

public class MathParser extends StringParser<Therm> {

	public static final char PARENTHESIS_LEFT = '(';
	public static final char PARENTHESIS_RIGHT = ')';
	public static final char BRACE_LEFT = '{';
	public static final char BRACE_RIGHT = '}';
	public static final char DOT = '.';
	public static final char COMMA = ',';
	public static final char PLUS = '+';
	public static final char MINUS = '-';
	public static final char EQUALS = '=';
	public static final char SPACE = ' ';
	public static final char POW = '^';
	public static final char DIV = '/';
	public static final char MUL = '*';

	private Map<String, EnginePlugin> plugins = new HashMap<>();

	public MathParser( Map<String, EnginePlugin> plugins )
	{
		this.plugins = plugins;
	}

	@Override
	protected void reset( char[] chars )
	{
		super.reset( chars );
		level = 1;
	}

	private int level = 1;

	public int getLevel()
	{
		return level;
	}

	public void setLevel( int level )
	{
		this.level = level;
	}

	@Override
	public Therm parseTest()
	{
		for ( EnginePlugin plugin : plugins.values() )
		{
			int savePosition = getPosition();
			int saveLevel = getLevel();
			
			Therm result = plugin.handle( this );
			
			setLevel( saveLevel );
			
			if ( result != null )
			{
				return result;
			}
			else
			{
				setPosition( savePosition );
			}
		}

		throw new ParseException( this );
	}

	public Therm parseTest(int level)
	{
		int saveLevel = getLevel();
		setLevel( level );
		Therm result = parseTest();	
		setLevel( saveLevel );
		
		return result;
	}
	
	@Override
	public Therm parse()
	{
		if(true){
			return new BasicPlugin().handle( this );
		}
		
		Therm result = parseEquation();

		if ( hasNext() ) throw new ParseException( "Unknow Signs left :" + getChar() );

		return result;
	}

	protected Therm parseEquation()
	{
		Therm result = parseAdditional();

		if ( eat( EQUALS ) ) result = new Equation( result, parseAdditional() );

		return result;
	}

	protected Therm parseAdditional()
	{
		AdditionalBuilder builder = new AdditionalBuilder();
		builder.add( parseMultiply() );

		loop: while ( hasNext() )
		{
			switch ( getChar() ) {
				case SPACE:
					next();
					break;

				case PLUS:
				case MINUS:
					builder.add( parseMultiply() );
					break;

				default:
					break loop;
			}
		}

		return builder.build();
	}

	protected Therm parseMultiply()
	{
		MultiplyBuilder builder = new MultiplyBuilder();
		builder.add( parseTherm() );

		loop: while ( hasNext() )
		{
			switch ( getChar() ) {
				case SPACE:
					next();
					break;

				case DIV:
					next();
					builder.add( Const.ONE.div( parseTherm() ) );
					break;

				case MUL:
					next();
				case PARENTHESIS_LEFT:
					builder.add( parseTherm() );
					break;
					
				default:
					break loop;
			}
		}

		return builder.build();
	}

	protected Therm parseTherm()
	{
		boolean invert = parseSign();

		Therm therm;
		if ( is( PARENTHESIS_LEFT ) )
		{
			next();
			therm = parseAdditional();
			next();
		}
		else if ( is( BRACE_LEFT ) )
		{
			therm = parseVectorOrMatrix();
		}
		else if ( isAlpha() )
		{
			String str = parseString();

			if ( str.equalsIgnoreCase( "infinity" ) )
			{
				if ( invert )
				{
					invert = false;
					therm = new Const( Double.NEGATIVE_INFINITY );
				}
				else
				{
					therm = new Const( Double.POSITIVE_INFINITY );
				}
			}
			else
			{
				therm = parseMethod( str );
				// therm = new Variable( str );
			}
		}
		else if ( isDigit() || is( DOT ) )
		{
			// Attach minus to number
			therm = parseDouble( invert );
			invert = false;
		}
		else throw new ParseException( getChar() + " Wrong position" );

		eatAll( SPACE );

		if ( eat( '^' ) ) therm = therm.pow( parseTherm() );

		if ( invert ) return Const.MINUS_ONE.mul( therm );

		return therm;
	}

	private Therm parseMethod( String methodName )
	{
		List<Therm> therms = new ArrayList<>();

		if ( eat( PARENTHESIS_LEFT ) )
		{
			for ( ; isNot( PARENTHESIS_RIGHT ) ; )
			{
				Therm param = parseEquation();
				therms.add( param );
				eat( COMMA );
			}

			eat( PARENTHESIS_RIGHT );
		}

		Class<?>[] classes = new Class<?>[therms.size()];
		Object[] thermsArr = new Therm[therms.size()];

		for ( int i = 0 ; i < therms.size() ; i++ )
		{
			thermsArr[i] = therms.get( i );
			classes[i] = thermsArr[i].getClass();
		}

		Therm therm = null;

		EnginePlugin function = plugins.get( methodName.toLowerCase() );

		if ( function != null )
		{
			List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith( function.getClass(), EngineExecute.class );
			Method method = ReflectionUtils.findBestMethod( methods, classes );
			therm = ReflectionUtils.safeInvoke( null, Therm.class, function, method, thermsArr );
		}

		if ( therm == null )
		{
			therm = new Variable( methodName );

			if ( thermsArr.length == 1 )
			{
				therm = therm.mul( (Therm) thermsArr[0] );
			}
			else if ( thermsArr.length > 1 )
			{
				throw new ParseException( this );
			}
		}

		return therm;
	}

	protected Therm parseVectorOrMatrix()
	{
		List<Therm> therms = parseElements();
		Therm first = therms.get( 0 );
		if ( first instanceof Vector || first instanceof Matrix )
		{
			return parseMatrix( therms );
		}
		else
		{
			return parseVector( therms );
		}
	}

	protected List<Therm> parseElements()
	{
		List<Therm> therms = new ArrayList<>();
		while ( isNot( BRACE_RIGHT ) )
			therms.add( parseAdditional() );

		if ( therms.isEmpty() ) throw new ParseException( "Empty Vector" );

		return therms;
	}

	protected Vector parseVector()
	{
		return parseVector( parseElements() );
	}

	protected Vector parseVector( List<Therm> therms )
	{
		return new Vector( therms.toArray( new Therm[therms.size()] ) );
	}

	protected Matrix parseMatrix()
	{
		return parseMatrix( parseElements() );
	}

	protected Matrix parseMatrix( List<Therm> therms )
	{
		int columns = ((Vector) therms.get( 0 )).size();
		for ( Therm element : therms )
		{
			if ( !(element instanceof Vector) )
			{
				throw new ParseException( "No Vector" );
			}

			if ( columns != ((Vector) element).size() )
			{
				throw new ParseException( "Different Column Size in Matrix" );
			}
		}

		Therm[][] matrix = new Therm[therms.size()][columns];

		for ( int row = 0 ; row < therms.size() ; row++ )
		{
			Therm[] vector = ((Vector) therms.get( row )).getValues();
			for ( int column = 0 ; column < columns ; column++ )
			{
				matrix[row][column] = vector[column];
			}
		}

		return new Matrix( matrix );
	}
	/*
	 * protected Therm parse( Type type ) { switch ( type ) { case THERM: return
	 * parseAdditional(); case VARIABLE: return parseVariable(); case CONST:
	 * return parseDouble( parseSign() ); case MATRIX: return parseMatrix();
	 * case VECTOR: return parseVector(); }
	 * 
	 * return null; }
	 */

	public boolean parseSign()
	{
		boolean sign = false;
		while ( is( MINUS ) || is( PLUS ) || is( SPACE ) )
		{
			sign ^= is( MINUS );
			next();
		}

		return sign;
	}

	protected String parseString()
	{
		StringBuilder builder = new StringBuilder();
		while ( isAlpha() )
			builder.append( nextChar() );

		return builder.toString();
	}

	protected Variable parseVariable()
	{
		return new Variable( parseString() );
	}

	protected Const parseDouble( boolean invert )
	{
		StringBuilder builder = new StringBuilder();

		while ( isDigit() )
		{
			builder.append( nextChar() );
		}

		if ( is( DOT ) )
		{
			builder.append( nextChar() );

			while ( isDigit() )
			{
				builder.append( nextChar() );
			}
		}

		return new Const( Double.parseDouble( builder.toString() ) * (invert ? -1 : 1) );
	}

	protected Const parseInt( boolean invert )
	{
		StringBuilder builder = new StringBuilder();

		while ( isDigit() )
		{
			builder.append( nextChar() );
		}

		return new Const( Integer.parseInt( builder.toString() ) * (invert ? -1 : 1) );
	}
}

package parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import functions.EngineExecute;
import functions.EnginePlugin;
import functions.Functions;
import matrix.Matrix;
import matrix.Vector;
import therms.Chain;
import therms.Const;
import therms.Equation;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;

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
	protected Therm parse()
	{
		Therm result = parseEquation();

		if ( hasNext() ) throw new ParseException( "Unknow Signs left :" + getChar() );

		return result;
	}

	protected Therm parseEquation()
	{
		Therm result = parseAdditional();

		if ( eatNext( EQUALS ) ) result = new Equation( result, parseAdditional() );

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

				case BRACE_RIGHT:
				case PARENTHESIS_RIGHT:
				case COMMA:
				case EQUALS:
					break loop;

				case PLUS:
				case MINUS:
					builder.add( parseMultiply() );
					break;

				default:
					throw new ParseException( getChar() + " Wrong position" );
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

				case BRACE_RIGHT:
				case PARENTHESIS_RIGHT:
				case COMMA:
				case PLUS:
				case MINUS:
				case EQUALS:
					break loop;

				case DIV:
					next();
					builder.add( Const.ONE.div( parseTherm() ) );
					break;

				case MUL:
					next();
				case PARENTHESIS_LEFT:
				default:
					builder.add( parseTherm() );
					break;
			}
		}

		return builder.build();
	}

	protected Therm parseTherm()
	{
		boolean invert = parseSign();

		Therm therm;
		if ( hasNext( PARENTHESIS_LEFT ) )
		{
			next();
			therm = parseAdditional();
			next();
		}
		else if ( hasNext( BRACE_LEFT ) )
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
			/*else if ( str.equals( "e" ) )
			{
				therm = Functions.EXP.getTherm();

				if ( eatNext( POW ) )
				{
					therm = new Chain( therm, parseTherm() );
				}
				else
				{
					therm = new Chain( therm, Const.ONE );
				}
			}*/
			else if ( hasNext( PARENTHESIS_LEFT ) )
			{
				therm = parseMethod( str );
			}
			else
			{
				therm = new Variable( str );
			}
		}
		else if ( isDigit() || hasNext( DOT ) )
		{
			// Attach minus to number
			therm = parseDouble( invert );
			invert = false;
		}
		else throw new ParseException( getChar() + " Wrong position" );

		eatAll( SPACE );

		if ( eatNext( '^' ) ) therm = therm.pow( parseTherm() );

		if ( invert ) return Const.MINUS_ONE.mul( therm );

		return therm;
	}

	private Therm parseMethod( String methodName )
	{
		Therm therm = null;

		try
		{
			List<Therm> therms = new ArrayList<>();

			for ( ; hasNext() && nextChar() != PARENTHESIS_RIGHT ; )
			{
				therms.add( parseEquation() );
			}

			Class<?>[] classes = new Class<?>[therms.size()];
			Object[] thermsArr = new Therm[therms.size()];

			for ( int i = 0 ; i < therms.size() ; i++ )
			{
				thermsArr[i] = therms.get( i );
				classes[i] = thermsArr[i].getClass();
			}

			try
			{
				Therm function = plugins.get( methodName.toLowerCase() );

				int[] test = new int[]{2};
				
				Arrays.stream( test ).asLongStream();
				
				try
				{
					List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith( function.getClass(), EngineExecute.class );
					Method method = ReflectionUtils.findBestMethod( methods, classes );

					try
					{
						Object result = method.invoke( function, thermsArr );

						if ( result instanceof Therm )
						{
							return (Therm) result;
						}
					}
					catch ( Throwable excp )
					{
						throw new ParseException( "Failure while running method " + methodName );
					}
				}
				catch ( Throwable excp )
				{
					throw new ParseException(
							"No Matching Method found for " + methodName + " with " + Arrays.toString( classes ) );
				}
			}
			catch ( Throwable excp )
			{
				if ( thermsArr.length != 1 )
				{
					throw new ParseException(
							"No Matching Method found for " + methodName + " with " + Arrays.toString( classes ) );
				}

				therm = new Variable( methodName ).mul( (Therm) thermsArr[0] );
			}

		}
		catch ( IllegalArgumentException e2 )
		{
			therm = new Variable( methodName );
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
		while ( hasNext() && nextChar() != BRACE_RIGHT )
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

	protected boolean parseSign()
	{
		boolean sign = false;
		while ( hasNext( MINUS ) || hasNext( PLUS ) || hasNext( SPACE ) )
		{
			sign ^= hasNext( MINUS );
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

		if ( hasNext( DOT ) )
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

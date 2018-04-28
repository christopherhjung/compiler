package functions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import builder.AdditionalBuilder;
import builder.MultiplyBuilder;
import parser.MathParser;
import parser.ParseException;
import therms.Const;
import therms.Equation;
import therms.Therm;
import therms.Variable;
import tools.ReflectionUtils;

public class BasicPlugin extends EnginePlugin {

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

	@Override
	public Therm handle( MathParser parser )
	{
		return parseEquation( parser );
	}

	protected Therm parseEquation( MathParser parser )
	{
		Therm result = parseAdditional( parser );

		if ( parser.eat( EQUALS ) )
		{
			result = new Equation( result, parseAdditional( parser ) );
		}

		return result;
	}

	protected Therm parseAdditional( MathParser parser )
	{
		AdditionalBuilder builder = new AdditionalBuilder();
		builder.add( parseMultiply( parser ) );

		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case SPACE:
					parser.next();
					break;

				case PLUS:
				case MINUS:
					builder.add( parseMultiply( parser ) );
					break;

				default:
					break loop;
			}
		}

		return builder.build();
	}

	protected Therm parseMultiply( MathParser parser )
	{
		MultiplyBuilder builder = new MultiplyBuilder();
		builder.add( parseSign( parser ) );

		loop: while ( parser.hasNext() )
		{
			switch ( parser.getChar() ) {
				case SPACE:
					parser.next();
					break;

				case DIV:
					parser.next();
					builder.add( Const.ONE.div( parseSign( parser ) ) );
					break;

				case MUL:
					parser.next();
				case PARENTHESIS_LEFT:
					builder.add( parseSign( parser ) );
					break;

				default:
					break loop;
			}
		}

		return builder.build();
	}

	protected Therm parseSign( MathParser parser )
	{
		boolean sign = false;
		while ( parser.is( MINUS ) || parser.is( PLUS ) || parser.is( SPACE ) )
		{
			sign ^= parser.is( MINUS );
			parser.next();
		}

		Therm therm = parseExponent( parser );

		if ( sign )
		{
			therm = Const.MINUS_ONE.mul( therm );
		}

		return therm;
	}

	protected Therm parseExponent( MathParser parser )
	{
		Therm therm = parseParenthesis( parser );
		if ( parser.eat( '^' ) )
		{
			therm = therm.pow( parseParenthesis( parser ) );
		}
		return therm;
	}

	/*
	 * protected Therm parseVar( MathParser parser ) { Therm therm =
	 * parseParenthesis( parser );
	 * 
	 * return therm; }
	 */

	protected Therm parseParenthesis( MathParser parser )
	{
		Therm therm = null;
		if ( parser.is( PARENTHESIS_LEFT ) )
		{
			parser.next();
			therm = parseEquation( parser );
			parser.next();
		}
		else
		{
			therm = parser.parseWithIgnore();
		}

		return therm;
	}

	protected String parseString( MathParser parser )
	{
		StringBuilder builder = new StringBuilder();
		while ( parser.is( Character::isAlphabetic ) )
		{
			builder.append( parser.nextChar() );
		}

		return builder.toString();
	}

	protected Variable parseVariable( MathParser parser )
	{
		return new Variable( parseString( parser ) );
	}

	protected Const parseDouble( MathParser parser )
	{
		StringBuilder builder = new StringBuilder();
		int startPos = parser.getPosition();
		
		while ( parser.is( Character::isDigit ) )
		{
			builder.append( parser.nextChar() );
		}

		if ( parser.is( DOT ) )
		{
			builder.append( parser.nextChar() );

			while ( parser.is( Character::isDigit ) )
			{
				builder.append( parser.nextChar() );
			}
		}

		return new Const( Double.parseDouble( builder.toString() ) );
	}

	/*
	 * private Therm parseMethod( String methodName ) { List<Therm> therms = new
	 * ArrayList<>();
	 * 
	 * if ( parser.eat( PARENTHESIS_LEFT ) ) { for ( ; isNot( PARENTHESIS_RIGHT
	 * ) ; ) { Therm param = parseEquation(); therms.add( param ); eat( COMMA );
	 * }
	 * 
	 * eat( PARENTHESIS_RIGHT ); }
	 * 
	 * Class<?>[] classes = new Class<?>[therms.size()]; Object[] thermsArr =
	 * new Therm[therms.size()];
	 * 
	 * for ( int i = 0 ; i < therms.size() ; i++ ) { thermsArr[i] = therms.get(
	 * i ); classes[i] = thermsArr[i].getClass(); }
	 * 
	 * Therm therm = null;
	 * 
	 * EnginePlugin function = plugins.get( methodName.toLowerCase() );
	 * 
	 * if ( function != null ) { List<Method> methods =
	 * ReflectionUtils.getMethodsAnnotatedWith( function.getClass(),
	 * EngineExecute.class ); Method method = ReflectionUtils.findBestMethod(
	 * methods, classes ); therm = ReflectionUtils.safeInvoke( null,
	 * Therm.class, function, method, thermsArr ); }
	 * 
	 * if ( therm == null ) { therm = new Variable( methodName );
	 * 
	 * if ( thermsArr.length == 1 ) { therm = therm.mul( (Therm) thermsArr[0] );
	 * } else if ( thermsArr.length > 1 ) { throw new ParseException( this ); }
	 * }
	 * 
	 * return therm; }
	 */

}

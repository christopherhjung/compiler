package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import therms.Equation;
import therms.Therm;

public class Deducer {

	private static final String FILE = "simplifier.txt";
	private static final String COMMENT_START = "//";
	
	private MathParser engine = new MathParser();

	public void parse()
	{
		InputStream fis = getClass().getResourceAsStream( FILE );
		BufferedReader reader = new BufferedReader( new InputStreamReader( fis ) );

		try
		{
			String line;
			while ( (line = reader.readLine()) != null )
			{
				compute( line );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private void compute( String line )
	{
		if ( !line.startsWith( COMMENT_START ) && !line.trim().isEmpty() )
		{
			Therm therm = engine.eval( line );
			if ( therm instanceof Equation )
			{
				compute( (Equation) therm );
			}
		}
	}

	private void compute( Equation equation )
	{
		Therm from = equation.getLeft();
		DeducerContext context = new DeducerContext();

		System.out.println( from );
		
	}
}

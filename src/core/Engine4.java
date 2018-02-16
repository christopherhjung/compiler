package core;

import java.util.Scanner;

import parser.MathEngine;

public class Engine4 {

	public static void main( String[] args )
	{
		MathEngine engine = new MathEngine();
		
		Scanner scanner = new Scanner( System.in );
		while ( true )
		{
			System.out.print( ">" );
			String therm = scanner.nextLine();

			if ( therm.equals( "quit" ) )
			{
				scanner.close();
				break;
			}

			try
			{
				System.out.println( engine.eval( therm ).simplify() );
			}
			catch ( Throwable t )
			{
				System.out.println( t.getMessage() );
			}
		}
	}

}

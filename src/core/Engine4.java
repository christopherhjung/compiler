package core;

import java.util.Scanner;

import functions.CosPlugin;
import functions.ReducePlugin;
import functions.SinPlugin;
import modifier.DerivatePlugin;
import parser.MathParser;
import parser.MathProgram;

public class Engine4 {

	public static void main( String[] args ) throws Exception
	{
		MathProgram program = new MathProgram();
		
		program.installPlugin( "sin", SinPlugin.class);
		program.installPlugin( "cos", CosPlugin.class);
		program.installPlugin( "reduce", ReducePlugin.class);
		//program.installPlugin( "tan", Tan.class);
		program.installPlugin( "derivate" , DerivatePlugin.class );
		
		MathParser engine = program.start();
		
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
				System.out.println( engine.eval( therm ) );
			}
			catch ( Throwable t )
			{
				System.out.println( t.getMessage() );
			}
		}
		
	}
}

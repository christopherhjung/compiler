package core;

import java.util.ArrayList;
import java.util.Scanner;

import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.ConstPlugin;
import functions.DividePlugin;
import functions.ExponentPlugin;
import functions.FunctionPlugin;
import functions.IncrementPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.ParenthesisPlugin;
import functions.SignPlugin;
import functions.VariablePlugin;
import parser.MathEngine;
import parser.MathProgram;
import therms.Therm;

public class StackEngine {

	public static void main( String[] args ) throws Exception
	{
		MathProgram program = new MathProgram();

		program.installPlugin( 1, AssignmentPlugin.class );
		program.installPlugin( 11, AddPlugin.class );
		program.installPlugin( 12, MulPlugin.class );
		program.installPlugin( 12, DividePlugin.class );
		program.installPlugin( 14, SignPlugin.class );
		program.installPlugin( 14, IncrementPlugin.class );
		program.installPlugin( 15, ExponentPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );
		program.installPlugin( 17, VariablePlugin.class );
		program.installPlugin( 17, FunctionPlugin.class );
		program.installPlugin( 18, ParenthesisPlugin.class );
		program.installPlugin( ConstPlugin.class );

		// program.installPlugin( BasicPlugin.class );
		// program.installPlugin( ConstPlugin.class );
		// program.installPlugin( ReducePlugin.class );

		// program.installPlugin( "+", AddPlugin.class );
		// program.installPlugin( "*", MulPlugin.class );
		// program.installPlugin( "sss", ThermPlugin.class );
		// program.installPlugin( "ss", FunctionPlugin.class );
		// program.installPlugin( "exp", ExponentPlugin.class );

		// program.installPlugin( "sin", SinPlugin.class);
		// program.installPlugin( "cos", CosPlugin.class);
		// program.installPlugin( "reduce", ReducePlugin.class);
		// program.installPlugin( "tan", Tan.class);
		// program.installPlugin( "derivate" , DerivatePlugin.class );

		MathEngine engine = program.start();

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
				long begin = System.currentTimeMillis();
				Therm result = engine.eval( therm );
				System.out.println( result );
				long end = System.currentTimeMillis();

				System.out.println( "Time need:" + (end - begin) );
			}
			catch ( Throwable t )
			{
				System.out.println( t.getMessage() );
			}
		}

	}
}

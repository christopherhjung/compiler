package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.ConstPlugin;
import functions.CosPlugin;
import functions.DerivatePlugin;
import functions.DividePlugin;
import functions.ExponentPlugin;
import functions.FunctionPlugin;
import functions.InsertPlugin;
import functions.LogPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.ObjectPlugin;
import functions.ParenthesisPlugin;
import functions.ReducePlugin;
import functions.SignPlugin;
import functions.SinPlugin;
import functions.TypePlugin;
import functions.UpdatePlugin;
import functions.VariablePlugin;
import parser.HybridMathParser;
import parser.MathEngine;
import parser.MathParser;
import parser.MathProgram;
import parser.PluginExtention;
import therms.Therm;
import tools.Run;

public class StackEngine {

	public static MathEngine startEngine()
	{
		MathProgram program = new MathProgram();

		program.installPlugin( 1, AssignmentPlugin.class );
		program.installPlugin( 2, TypePlugin.class );
		program.installPlugin( 11, AddPlugin.class );
		program.installPlugin( 12, MulPlugin.class );
		program.installPlugin( 12, DividePlugin.class );
		program.installPlugin( 14, SignPlugin.class );
		program.installPlugin( 15, ExponentPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );
		program.installPlugin( 17, FunctionPlugin.class );
		program.installPlugin( 18, ParenthesisPlugin.class );
		program.installPlugin( 18, ObjectPlugin.class );

		program.installPlugin( ConstPlugin.class );
		program.installPlugin( VariablePlugin.class );

		program.installPlugin( ReducePlugin.class );
		program.installPlugin( DerivatePlugin.class );
		program.installPlugin( UpdatePlugin.class );

		program.installPlugin( SinPlugin.class );
		program.installPlugin( CosPlugin.class );
		program.installPlugin( LogPlugin.class );

		return Run.safe( program::start );
	}

	public static void main( String[] args ) throws Exception
	{
		MathEngine engine = startEngine();
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

				System.out.println( "Result:" + result );
				result = engine.eval( "reduce(update(", therm , "))" );
				System.out.println( "Result2:" + result );
				long end = System.currentTimeMillis();

				System.out.println( "Time need:" + (end - begin) );
			}
			catch ( Throwable t )
			{
				System.out.println( t.getMessage() );
				t.printStackTrace();
			}
		}

	}
}

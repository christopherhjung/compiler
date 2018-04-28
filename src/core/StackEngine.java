package core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

import functions.BasicPlugin;
import functions.ConstPlugin;
import functions.CosPlugin;
import functions.EngineExecute;
import functions.EnginePlugin;
import functions.ExponentPlugin;
import functions.FunctionPlugin;
import functions.ReducePlugin;
import functions.SinPlugin;
import functions.Tan;
import functions.ThermPlugin;
import modifier.DerivatePlugin;
import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.ReflectionUtils;

public class StackEngine {

	public static void main( String[] args ) throws Exception
	{
		MathProgram program = new MathProgram();

		program.installPlugin( BasicPlugin.class );
		program.installPlugin( ConstPlugin.class );
		//program.installPlugin( ReducePlugin.class );
		program.installPlugin( FunctionPlugin.class );

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
				long begin = System.currentTimeMillis();
				System.out.println( engine.eval( therm ) );
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

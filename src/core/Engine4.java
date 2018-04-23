package core;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

import functions.CosPlugin;
import functions.EngineExecute;
import functions.EnginePlugin;
import functions.Functions;
import functions.ReducePlugin;
import functions.SinPlugin;
import functions.Tan;
import modifier.DerivatePlugin;
import parser.MathParser;
import parser.MathProgram;
import parser.ThermStringifier;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import tools.ReflectionUtils;

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

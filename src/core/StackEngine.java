package core;

import java.util.Scanner;

import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.BlockPlugin;
import functions.DeclarePlugin;
import functions.DividePlugin;
import functions.MethodSignaturePlugin;
import functions.IntegerPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.NamePlugin;
import functions.ObjectPlugin;
import functions.ParenthesisPlugin;
import functions.SignPlugin;
import functions.SubPlugin;
import functions.VectorPlugin;
import parser.ScriptParser;
import parser.MathProgram;
import parser.Statement;

public class StackEngine {

	public static ScriptParser startEngine()
	{
		MathProgram program = new MathProgram();

		program.installPlugin( 0, BlockPlugin.class );
		program.installPlugin( 1, AssignmentPlugin.class );
		program.installPlugin( 2, DeclarePlugin.class );
		program.installPlugin( 11, AddPlugin.class );
		program.installPlugin( 11, SubPlugin.class );
		program.installPlugin( 12, MulPlugin.class );
		program.installPlugin( 12, DividePlugin.class );
		program.installPlugin( 14, SignPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );
		program.installPlugin( 17, MethodSignaturePlugin.class );
		program.installPlugin( 18, ParenthesisPlugin.class );
		program.installPlugin( 18, ObjectPlugin.class );

		program.installPlugin( VectorPlugin.class );
		program.installPlugin( IntegerPlugin.class );
		program.installPlugin( NamePlugin.class );

		//program.installPlugin( ReducePlugin.class );
		//program.installPlugin( UpdatePlugin.class );

		try{
			return program.start();
		}catch(Exception e){
			e.printStackTrace( );
			return null;
		}
	}
	
	public static void main( String[] args ) throws Exception
	{
		
		ScriptParser engine = startEngine();
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
				Statement result = engine.eval( therm );

				System.out.println( "Result:" + result );
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

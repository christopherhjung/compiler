package core;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import compiler.Compiler;
import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.BlockPlugin;
import functions.DeclarePlugin;
import functions.DividePlugin;
import functions.FunctionPlugin;
import functions.IntegerPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.NamePlugin;
import functions.ObjectPlugin;
import functions.ParenthesisPlugin;
import functions.PreIncrementPlugin;
import functions.SignPlugin;
import functions.StringPlugin;
import functions.SubPlugin;
import functions.TemplatePlugin;
import functions.VectorPlugin;
import parser.MathEngine;
import parser.MathProgram;
import parser.Statement;
import tools.Run;

public class Files {

	public static MathEngine startEngine()
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
		program.installPlugin( 14, PreIncrementPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );
		program.installPlugin( 17, FunctionPlugin.class );
		program.installPlugin( 18, ParenthesisPlugin.class );

		//program.installPlugin( 19,TemplatePlugin.class );
		program.installPlugin( 20, ObjectPlugin.class );

		program.installPlugin( 21, StringPlugin.class );
		
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
	
	static String getString()
	{
		InputStream is = StackEngine.class.getResourceAsStream( "test.txt" );
		Scanner s = new Scanner( is ).useDelimiter( "\\A" );
		String res = s.hasNext() ? s.next() : "";
		s.close();
		return res;
	}

	public static void main( String[] args ) throws Exception
	{
		String str = getString();
		MathEngine engine = startEngine();
		Statement result = engine.eval( str );
		
		Compiler compiler = new Compiler();
		
		String code = compiler.compile(result);
		
		System.out.println( code );
	}
	
	public void runAvr(){
		 ProcessBuilder pb = new ProcessBuilder("avrdude");
		 //Map<String, String> env = pb.environment();
		 //env.put("VAR1", "myValue");
		 Process p = Run.safe( pb::start );
	}

}

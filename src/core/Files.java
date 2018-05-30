package core;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

import compiler.Compiler;
import compiler.CompilerBackwards;
import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.BlockPlugin;
import functions.DeclarePlugin;
import functions.DividePlugin;
import functions.MethodSignaturePlugin;
import functions.IntegerPlugin;
import functions.MethodDefinitionPlugin;
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
import parser.ScriptParser;
import parser.MathProgram;
import parser.Statement;
import tools.Run;

public class Files {

	public static ScriptParser startEngine()
	{
		MathProgram program = new MathProgram();

		program.installPlugin( 1, AssignmentPlugin.class );
		program.installPlugin( 11, AddPlugin.class );
		program.installPlugin( 11, SubPlugin.class );
		program.installPlugin( 12, MulPlugin.class );
		program.installPlugin( 12, DividePlugin.class );
		program.installPlugin( 14, SignPlugin.class );
		program.installPlugin( 14, PreIncrementPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );

		program.installPlugin( 17, ObjectPlugin.class );

		program.installPlugin( 18, MethodDefinitionPlugin.class );

		program.installPlugin( 50, BlockPlugin.class );
		program.installPlugin( 55, DeclarePlugin.class );
		program.installPlugin( 60, MethodSignaturePlugin.class );

		program.installPlugin( 70, ParenthesisPlugin.class );

		//program.installPlugin( 19,TemplatePlugin.class );

		program.installPlugin( 80, StringPlugin.class );
		
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
		ScriptParser engine = startEngine();
		Statement result = engine.eval( str );
		
		//CompilerBackwards compiler = new CompilerBackwards();
		
		//String code = compiler.compile(result);
		
		System.out.println(  result );
	}
	
	public void runAvr(){
		 ProcessBuilder pb = new ProcessBuilder("avrdude");
		 //Map<String, String> env = pb.environment();
		 //env.put("VAR1", "myValue");
		 Process p = Run.safe( pb::start );
	}

}

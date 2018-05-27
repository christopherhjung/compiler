import functions.AddPlugin;
import functions.AssignmentPlugin;
import functions.DividePlugin;
import functions.ExponentPlugin;
import functions.FunctionPlugin;
import functions.IntegerPlugin;
import functions.MethodPlugin;
import functions.MulPlugin;
import functions.NamePlugin;
import functions.ParenthesisPlugin;
import functions.SignPlugin;
import parser.MathEngine;
import parser.MathProgram;
import tools.Run;

public class Util {
	public static MathEngine startEngine(){
		MathProgram program = new MathProgram();

		program.installPlugin( 1, AssignmentPlugin.class );
		program.installPlugin( 11, AddPlugin.class );
		program.installPlugin( 12, MulPlugin.class );
		program.installPlugin( 12, DividePlugin.class );
		program.installPlugin( 14, SignPlugin.class );
		program.installPlugin( 15, ExponentPlugin.class );
		program.installPlugin( 16, MethodPlugin.class );
		program.installPlugin( 17, FunctionPlugin.class );
		program.installPlugin( 18, ParenthesisPlugin.class );
		program.installPlugin( NamePlugin.class );
		program.installPlugin( IntegerPlugin.class );

		return Run.safe( () -> program.start() );
	}
}

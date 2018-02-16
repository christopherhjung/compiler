package core;

import java.util.Arrays;
import java.util.Scanner;

import functions.Function;
import neuron.NeuralNetwork;
import parser.MathEngine;
import parser.ThermStringify;
import therms.Const;
import therms.Therm;
import therms.VarSet;
import therms.Variable;
import trainer.ErrorMeasurement;
import trainer.NetworkTrainer;
import trainer.TrainingLesson;

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

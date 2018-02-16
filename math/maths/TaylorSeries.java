package maths;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import builder.AdditionalBuilder;
import builder.ThermBuilder;
import therms.Additional;
import therms.ArrayPolynom;
import therms.Const;
import therms.ListPolynom;
import therms.Polynom;
import therms.Therm;
import therms.Variable;

public class TaylorSeries {

	public static Therm envolve( Therm therm, double position, int order )
	{
		if ( order < 0 ) throw new ArithmeticException( "A taylor polynom with a order less than 0 is not possible" );

		AdditionalBuilder[] builders = new AdditionalBuilder[order + 1];
		double factorial = 1;
		for ( int i = 0 ;; )
		{
			Therm thermPart = therm.replace( Variable.X, new Const( position ) ).div( new Const( factorial ) );

			builders[i] = new AdditionalBuilder();

			double positionPow = 1;
			int[] coefficents = Coefficents.get( i );
			for ( int j = 0 ; j < coefficents.length && positionPow != 0 ; j++ )
			{
				builders[i - j].add( thermPart.mul( new Const( coefficents[j] * positionPow ) ) );
				positionPow *= -position;
			}

			if ( i++ == order ) break;

			therm = therm.derivate( Variable.X ).simplify();
			if ( therm.equals( Const.ZERO ) ) break;
			factorial *= i;
		}

		ThermBuilder builder = new AdditionalBuilder();

		for ( int i = 0 ; i < builders.length ; i++ )
		{
			builder.add( builders[i].build().mul( Variable.X.pow( new Const( i ) ) ) );
		}

		return builder.build();
	}

	public static Therm envolve2( Therm therm, double position, int order )
	{
		if ( order < 0 ) throw new ArithmeticException( "A taylor polynom with a order less than 0 is not possible" );

		Therm[] newTherms = new Therm[order + 1];
		double factorial = 1;
		for ( int i = 0 ;; )
		{
			newTherms[i] = therm.replace( Variable.X, new Const( position ) ).div( new Const( factorial ) );

			if ( position != 0 && i != 0 )
			{
				double positionPow = 1;
				int[] coefficents = Coefficents.get( i );
				for ( int j = 1 ; j < coefficents.length ; j++ )
				{
					newTherms[i - j] = newTherms[i - j].add( newTherms[i].mul( new Const( coefficents[j] * positionPow ) ) );
					positionPow *= -position;
				}
			}

			if ( i++ == order ) break;

			therm = therm.derivate( Variable.X ).simplify();
			if ( therm.equals( Const.ZERO ) ) break;
			factorial *= i;
		}

		ThermBuilder builder = new AdditionalBuilder();

		for ( int i = 0 ; i < newTherms.length ; i++ )
		{
			builder.add( newTherms[i].mul( Variable.X.pow( new Const( i ) ) ) );
		}

		return builder.build();
	}
	
	public static Polynom envolveUntil( Therm therm, int position, Predicate<Polynom> predicate )
	{
		List<Double> values = new ArrayList<>();
		Polynom polynom = new ListPolynom( values );
		double factorial = 1;
		for ( int i = 0 ;; )
		{
			double factor = therm.valueAt( position ) / factorial;

			values.add( factor );
			if ( position != 0 && i != 0 )
			{
				double positionTemp = 1;
				int[] coefficents = Coefficents.get( i );
				for ( int j = 1 ; j < coefficents.length ; j++ )
				{
					values.set( i - j, values.get( i - j ) + coefficents[j] * factor * positionTemp );
					positionTemp *= -position;
				}
			}

			if ( predicate.test( polynom ) ) break;

			therm = therm.derivate( Variable.X ).simplify();
			factorial *= i;
		}

		return polynom;
	}

}

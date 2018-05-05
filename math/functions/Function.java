package functions;

import parser.ThermStringifier;
import therms.Therm;

public class Function extends Therm {

	private Therm therm;

	public Function( Therm therm )
	{
		this.therm = therm;
	}

	@Override
	public void toString( ThermStringifier builder )
	{
		
	}
	
	public static class Reference extends Therm{

		@Override
		public void toString( ThermStringifier builder )
		{
			// TODO Auto-generated method stub
			
		}
	}
}

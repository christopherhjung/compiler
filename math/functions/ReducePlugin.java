package functions;

import therms.Therm;

public class ReducePlugin extends EnginePlugin {
	
	@EngineExecute
	public Therm execute( Therm therm )
	{
		Therm result = (Therm) therm.execute( "reduce" );

		if ( result == null )
		{
			return therm;
		}

		return result;
	}
	
	public Therm get( String event, Therm[] therms ){
		if( event.equals( "reduce" ) ){
			if(therms.length == 1){
				
			}else{
				//fail
			}
		}else if( event.equals( "mulreduce" ) ){
			if(therms.length == 2){
				
			}else{
				//fail
			}
		}else if( event.equals( "addreduce" ) ){
			if(therms.length == 2){
				
			}else{
				//fail
			}
		}
		
		return null;
	}
	/*
	private Therm reduce( Therm therm ){
		addMiniPlugin("reduce","const",)
	}
	
	private Therm mulReduce( Therm left, Therm right ){
		
	}
	
	private Therm addReduce( Therm left, Therm right ){
		
	}
	*/
	
	
}
package com.navlog.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.navlog.calculator.R;
import com.navlog.models.AviationWeatherModel;
import com.navlog.models.AviationWeatherModel.OnWeatherObtainedListener;
import com.navlog.models.CalculationsModel;
import com.navlog.models.FlightWaypointsModel;
import com.navlog.models.Frequencies;
import com.navlog.models.Frequencies.freqStruct;
import com.navlog.models.LegData;
import com.navlog.models.LegDataEntry;
import com.navlog.models.WeatherStation;


public class CalculationsActivity extends Activity implements DetailsFragment.OnDetailsSetListener 
, TitlesFragment.ViewHandler,OnWeatherObtainedListener{
	
	private CalculationsModel flightData;
	private LegDataEntry legs;
	public static final String legKey = "legs";
	public static final String indexKey = "index";
	public static final String departureConditions = "deptCond";
	public static final String destinationConditions = "destCond";
	int lastIndex = -1;
	private String DepartureWeather;
	private String DestinationWeather;
	private String deptartureFreq;
	private String destinationFreq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
	
		loadPreviousActivityFlightData();
		restoreLegsState(savedInstanceState);
		restoreWeatherState(savedInstanceState);
		getAirportFreq();
	    replaceListFragment();
        
	}
	
	public void getAirportFreq()
	{
		Frequencies freq = new Frequencies();
		ArrayList<freqStruct> deptFreq = freq.getData(flightData.getDepartureICAO(), this);
		
		deptartureFreq = "Airport Frequencies: \n";
		for(int i=0; i<deptFreq.size(); i++)
		{
			deptartureFreq+=deptFreq.get(i).toString();
		}
		
		ArrayList<freqStruct> destFreq = freq.getData(flightData.getDestinationICAO(), this);
		destinationFreq = "Airport Frequencies: \n";
		for(int i=0; i<destFreq.size(); i++)
		{
			destinationFreq+=destFreq.get(i).toString();
		}
		

		
	}
	
	
	public void replaceListFragment()
	{
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment newFragment = TitlesFragment.newInstance(flightData); 
        ft.replace(R.id.titles, newFragment);
        ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculations, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.perform_calculations:
	        	performCalculations();
	        	
	        	break;
	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	private void loadPreviousActivityFlightData()
	{
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		
		flightData = new CalculationsModel();
		flightData = (CalculationsModel) b.getSerializable(MapActivity.flightModelDetails);	
	}

	@Override
	public void onDetailsSet(LegData aLeg) 
	{
		addOrUpdateLeg(aLeg);
	}
	
	public void addOrUpdateLeg(LegData aLeg)
	{
		Boolean added;
		 added = legs.addDataEntry(aLeg);
		 if(added == false)
		 {
			 legs.updateLeg(aLeg);
		 }
	}
	
	public LegData getLegData(int index)
	{
		LegData leg;
		try
		{
			leg = legs.getLegDataList().get(index);
			return leg;
		}
		catch(Exception e)
		{
			leg = new LegData();
			//Log.e("Calculations Activity", "Specified leg is null", e);
			return leg;
		}
		
	}
	
	@Override
    public void displayDetailsFragment(int index)
    {
		int labelCount = this.flightData.getAllLabels().length;
		
		if(index == 0 || index == labelCount-1 )
		{
			lastIndex = index;
			String weather = getWeatherString(index);
			String freq = getFreqString(index);
			AirportFragment details = AirportFragment.newInstance(weather, freq );
			//Execute a transaction, replaceing any existing fragment
			//with the new one inside the frame
			
			 FragmentTransaction ft = getFragmentManager().beginTransaction();
	         ft.replace(R.id.details, details);
	         ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	         ft.commit();	
		}
		else
		{
			//Only replace the fragment when a different item is selected
			if(index != this.lastIndex)
			{
				lastIndex = index;
				LegData leg = getLegData(index-1);
				DetailsFragment details = DetailsFragment.newInstance(index-1, leg);
				//Execute a transaction, replaceing any existing fragment
				//with the new one inside the frame
				
				 FragmentTransaction ft = getFragmentManager().beginTransaction();
		         ft.replace(R.id.details, details);
		         ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		         ft.commit();
			}
		}
			
		
    
    }
	

	 private void getWeatherFromAWC(FlightWaypointsModel waypoints)
	    {
	    	
	    	try
	    	{
		    	AviationWeatherModel weather = new AviationWeatherModel(this);
		    	weather.execute(waypoints);

	    	}
	    	catch(Exception e)
	    	{
	    		System.out.println("Error getting weather data = " + e);
	    	}
	    	
	    }
	    
	    private String getWeatherString(WeatherStation w)
	    {
	    	String elev = "Elevation : " + Double.toString(w.getElevation())+" Meters";
	    	String observationTime = "Observation Time: " + w.getObservationTime();
	    	String temp ="Temperature: " + Double.toString(w.getTemperature())+" Celsius";
	    	String visibility = "Visibility: " + Double.toString(w.getVisibility())+" Statute Miles";
	    	String dir ="Direction: " + Double.toString(w.getWindDirection())+" Degrees";
	    	String speed = "Speed: " + Double.toString(w.getWindSpeed())+" Knots";
	    	
	    	String output = elev + "\n" + observationTime + "\n"
	    			+ temp + "\n" + visibility + "\n" + dir + "\n"
	    			+ speed + "\n";
	    	return output;
	    }
    
	
	@Override
	protected void onSaveInstanceState(Bundle out)
	{
		super.onSaveInstanceState(out);
		out.putSerializable(legKey, legs);
		out.putInt(indexKey, lastIndex);
		out.putString(departureConditions, this.DepartureWeather);
		out.putString(destinationConditions, DestinationWeather);
	}
	
	private void restoreWeatherState(Bundle in)
	{
		if(in != null && in.containsKey(departureConditions) &&  in .containsKey(destinationConditions))
		{
			this.DepartureWeather = in.getString(departureConditions);
			this.DestinationWeather = in.getString(destinationConditions);
		}
		else
		{
			setRequestedOrientation(this.getResources().getConfiguration().orientation);
			FlightWaypointsModel wp = (FlightWaypointsModel)flightData;
			getWeatherFromAWC(wp);
			
		}
		
	}
	
	
	private void restoreLegsState(Bundle in)
	{
		Boolean flightDataContainsLegs = !(flightData.getLegsData() == null);
		Boolean bundleContainsLegs;
		if(in == null)
		{
			bundleContainsLegs = false;
		}
		else
		{
			bundleContainsLegs = in.containsKey(legKey);
		}
		
		if(flightDataContainsLegs == false && bundleContainsLegs == false )
		{
			legs = new LegDataEntry();	
		}
		else if(flightDataContainsLegs == false && bundleContainsLegs == true )
		{
			legs = (LegDataEntry) in.getSerializable(legKey);
			
		}
		else if(flightDataContainsLegs == true && bundleContainsLegs == false )
		{
			ArrayList<LegData> d = flightData.getLegsData(); 
			legs.setLegDataList(d);
		}
		else if(flightDataContainsLegs == true && bundleContainsLegs == true )
		{
			legs = (LegDataEntry) in.getSerializable(legKey);
			
		}
		/*in the future when data has been calculated it is likely that on screen rotation 
		 * calculations will be lost until performed again. A Boolean value in calculations to
		 * identify if calc has been performed may be necesary in the future.
		 */
	}


	@Override
	public void WeatherObtained(List<WeatherStation> w) {
		DepartureWeather = this.getWeatherString(w.get(0));
		DestinationWeather = this.getWeatherString(w.get(1));
		Toast.makeText(getApplicationContext(), "Weather data obtained, refresh tab if needed", Toast.LENGTH_SHORT).show();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		
	}
	
	public String getWeatherString(int index)
	{
		 String weather = "Getting weather data...";
		 if(DepartureWeather != null && DestinationWeather != null)
		 {
			 if(index == 0)
			 {
				 return DepartureWeather;
			 }
			 else
			 {
				 return DestinationWeather;
				 
			 }
		 } 
		 return weather;	 
	}
	
	public String getFreqString(int index)
	{
		 String weather = "Getting Frequency data...";
		 if(DepartureWeather != null && DestinationWeather != null)
		 {
			 if(index == 0)
			 {
				 return this.deptartureFreq;
			 }
			 else
			 {
				 return this.destinationFreq;
				 
			 }
		 } 
		 return weather;	 
	}
	
	
	private void performCalculations()
	{
		ArrayList<LegData> calculations;
		if(legs != null)
		{
			ArrayList<LegData> l = legs.getLegDataList();
			calculations = flightData.getCalculatedData(l);
			String  temp = calculations.get(0).toString();
			Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "No Waypoint data has been entered.", Toast.LENGTH_LONG).show();
			
		}
		
	}
	

}

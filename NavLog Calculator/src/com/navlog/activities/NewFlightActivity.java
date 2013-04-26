package com.navlog.activities;


import java.util.Locale;

import com.example.navlog.calculator.R;
import com.navlog.models.AirportModel;
import com.navlog.models.MapModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewFlightActivity extends Activity 
{
	public final static String departureLatitude ="Departure_Latitude";
	public final static String departureLongitude ="Departure_Longitude";
	public final static String departureAltitude = "Departure_Altitude";
	public final static String departureICAO = "Departure_ICAO";
	
	public final static String destinationLatitude ="Destination_Latitude";
	public final static String destinationLongitude ="Destination_Longitude";
	public final static String destinationAltitude = "Destination_Altitude";
	public final static String destinationICAO = "Destination_ICAO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight);
        populateSpinner();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_flight, menu);
        return true;
    }
    
    public void submitPressed(View view)
    {
    	
    	
    	 MapModel mapModel = new MapModel();
    	 try
    	 {
    	 mapModel.parseXML(this.getApplicationContext(),"Maps");
    	 }
	     catch (Exception e) 
	     {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
    	
    	EditText deptAirportCode = (EditText) findViewById(R.id.departureAirportCode);
    	String deptCode = deptAirportCode.getText().toString();
    	
    	EditText destAirportCode = (EditText) findViewById(R.id.DestinationAirportCode);
    	String destCode = destAirportCode.getText().toString();
    	
    	
    	try 
    	{
    		Locale loc = Locale.getDefault();
			AirportModel ap = new AirportModel();
			boolean departureFound = ap.parseAirportXML(this.getApplicationContext(), deptCode.toUpperCase(loc));
			double departureLat = ap.getLatitude();
			double departureLong = ap.getLongitude();
			double departureAlt = ap.getELEV();
			String deptICAO = ap.getICAO();
			
			ap = new AirportModel();
			boolean destinationFound = ap.parseAirportXML(this.getApplicationContext(), destCode.toUpperCase(loc));
			double destinationLat = ap.getLatitude();
			double destinationLong = ap.getLongitude();
			double destinationAlt = ap.getELEV();
			String destICAO = ap.getICAO();
			
			
	    	if(departureFound == true && destinationFound == true )
	    	{    	
	    		Intent intent = new Intent(this, MapActivity.class);
	    		intent.putExtra(departureLatitude, departureLat);
	    		intent.putExtra(departureLongitude, departureLong);
	    		intent.putExtra(departureAltitude, departureAlt);
	    		intent.putExtra(departureICAO, deptICAO);
	    		
	    		
	    		intent.putExtra(destinationLatitude, destinationLat);
	    		intent.putExtra(destinationLongitude, destinationLong);
	    		intent.putExtra(destinationAltitude, destinationAlt);
	    		intent.putExtra(destinationICAO, destICAO);
	    		
	    		startActivity(intent);
	    	}
	    	else if(departureFound == false && destinationFound == false)
	    	{
	    		Toast.makeText(getApplicationContext(),"Departure and destination airports not found."  ,Toast.LENGTH_SHORT).show();
	    	}
	    	else if(departureFound == false)
	    	{
	    		Toast.makeText(getApplicationContext(),"Departure airport not found."  ,Toast.LENGTH_SHORT).show();
	    	}
	    	else if(destinationFound == false)
	    	{
	    		Toast.makeText(getApplicationContext(),"Destination airport not found."  ,Toast.LENGTH_SHORT).show();
	    	}
			
		} 
    	catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
    	

    }
    
    public void populateSpinner()
    {
    	String[] maps = {"Flight #1", "Flight #2","Flight #3"}; //This should be replaced by the contents of the MAPS XML FILE
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
        	(this, android.R.layout.simple_spinner_item, maps);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

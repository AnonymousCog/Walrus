package com.navlog.activities;


import java.util.Locale;

import com.example.navlog.calculator.R;
import com.navlog.models.AirportModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewFlightActivity extends Activity 
{
	public final static String departureLatitude ="Departure_Latitude";
	public final static String departureLongitude ="Departure_Longitude";
	public final static String departureICAO = "Departure_ICAO";
	
	public final static String destinationLatitude ="Destination_Latitude";
	public final static String destinationLongitude ="Destination_Longitude";
	public final static String destinationICAO = "Destination_ICAO";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flight); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_flight, menu);
        return true;
    }
    
    public Boolean planeSelected()
    {
    	
    	SharedPreferences settings = getSharedPreferences(AirplaneListActivity.selected_plane_pref, 0);
        String plane = settings.getString(AirplaneListActivity.selected, "None");
        Boolean selected = !plane.equals("None");
        return selected;
    }
    
    public void submitPressed(View view)
    {
    	if(planeSelected())
    	{
    		findAirports();
    	}
    	else
    	{
    		Toast.makeText(getApplicationContext(), "You must first select a plane", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void setEditableTrue()
    {
    	SharedPreferences settings = getSharedPreferences("waypoints_editable", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("editable", true);
	    editor.commit();
    }
    
    
    public void findAirports()
    {
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
			String deptICAO = ap.getICAO();
			
			ap = new AirportModel();
			boolean destinationFound = ap.parseAirportXML(this.getApplicationContext(), destCode.toUpperCase(loc));
			double destinationLat = ap.getLatitude();
			double destinationLong = ap.getLongitude();
			String destICAO = ap.getICAO();
			
			
	    	if(departureFound == true && destinationFound == true )
	    	{    	
	    		Intent intent = new Intent(this, MapActivity.class);
	    		intent.putExtra(departureLatitude, departureLat);
	    		intent.putExtra(departureLongitude, departureLong);
	    		intent.putExtra(departureICAO, deptICAO);
	    		
	    		
	    		intent.putExtra(destinationLatitude, destinationLat);
	    		intent.putExtra(destinationLongitude, destinationLong);
	    		intent.putExtra(destinationICAO, destICAO);
	    		
	    		setEditableTrue();
	    		
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
}

package com.navlog.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.navlog.calculator.R;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.models.CalculationsModel;
import com.navlog.models.FlightWaypointsModel;
import com.navlog.models.LegDataEntry;


public class CalculationsActivity extends Activity implements DetailsFragment.OnDetailsSetListener
, TitlesFragment.ViewHandler{
	
	private CalculationsModel flightData;
	private LegDataEntry legs;
	public static final String legKey = "legs";
	public static final String indexKey = "index";
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		loadPreviousActivityFlightData();
		legs = new LegDataEntry();
       if (savedInstanceState == null) {
    	   replaceFragment();
        }
	}
	
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (0) : { 
	      if (resultCode == Activity.RESULT_OK) 
	      { 
	    	Bundle b = data.getExtras();
			legs = new LegDataEntry();
			legs = (LegDataEntry) b.getSerializable(CalculationsActivity.legKey);
	  		
	      } 
	      break; 
	    } 
	  } 
	}
	
	public void replaceFragment()
	{
        // First-time init; create fragment to embed in activity.
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment newFragment = TitlesFragment.newInstance(flightData, legs, index); 
        ft.add(R.id.titles, newFragment);
        //ft.addToBackStack(null);
        ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculations, menu);
		return true;
	}
	
	private void loadPreviousActivityFlightData()
	{
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		
		flightData = new CalculationsModel();
		flightData = (CalculationsModel) b.getSerializable(MapActivity.flightModelDetails);
		
		String deptICAO = flightData.getDepartureICAO();
		String destICAO = flightData.getDestinationICAO();
		//Toast.makeText(getApplicationContext(), "deptICAO : "+deptICAO+ " Dest ICAO : "+destICAO, Toast.LENGTH_SHORT).show();
		
		
	}

	@Override
	public void onDetailsSet(int legIndex, double altitude, double TAS, double rpms,
			double windSpeed, double windDir, double windTemp) 
	{
		Boolean added;
		 added = legs.addDataEntry(legIndex, altitude, TAS, rpms, windSpeed, windDir, windTemp);
		 if(added == false)
		 {
			 legs.updateLeg(legIndex, altitude, TAS, rpms, windSpeed, windDir, windTemp);
		 }
		TitlesFragment details = (TitlesFragment) getFragmentManager().findFragmentById(R.id.titles);
		details.setLegData(legs);
	
		
	}
	
	@Override
    public void displayDetailsFragment(DetailsFragment details, int index)
    {
    	details = DetailsFragment.newInstance(index, legs);
		
		//Execute a transaction, replaceing any existing fragment
		//with the new one inside the frame
		
		 FragmentTransaction ft = getFragmentManager().beginTransaction();
         ft.replace(R.id.details, details);
         ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
         ft.commit();
    
    }
    
	@Override
    public void displayDetailsActivity(int index)
    {   	
    	Intent intent = new Intent();
		intent.setClass(this, DetailsActivity.class);	
		Bundle b = new Bundle();
		b.putInt("index", index);
    	b.putSerializable(CalculationsActivity.legKey, legs);
    	intent.putExtras(b);
		//startActivity(intent);
    	startActivityForResult(intent, 0);
    	
    }
	
	
	@Override
	protected void onSaveInstanceState(Bundle out)
	{
		super.onSaveInstanceState(out);
		out.putSerializable(legKey, legs);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle in)
	{
		super.onRestoreInstanceState(in);	
		this.legs = (LegDataEntry) in.getSerializable(legKey);
	}
	



}

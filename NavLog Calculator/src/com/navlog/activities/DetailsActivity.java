package com.navlog.activities;

import com.navlog.models.LegDataEntry;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailsActivity extends Activity implements DetailsFragment.OnDetailsSetListener  {
	private DetailsFragment details;
	private LegDataEntry legs;
	private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        /*if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) 
        {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
        	returnDataToPreviousActivity();
        }*/

        if (savedInstanceState == null) 
        {
            // During initial setup, plug in the details fragment.
        	loadPreviousActivityLegdData();
        	details = DetailsFragment.newInstance(index, legs);
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
    
    private void loadPreviousActivityLegdData()
    {
    	Bundle b = new Bundle();
    	b = getIntent().getExtras();
    	index = b.getInt("index");
    	legs =(LegDataEntry) b.getSerializable(CalculationsActivity.legKey);
    	if(legs == null)
    	{
    		legs = new LegDataEntry();
    	}
    }
    
    @Override
	public void onBackPressed()
	{
    	returnDataToPreviousActivity();
    	
    	
	}

	@Override
	public void onDetailsSet(int legIndex, double altitude, double TAS,
			double rpms, double windSpeed, double windDir, double windTemp) {
		Boolean added;
		 added = legs.addDataEntry(legIndex, altitude, TAS, rpms, windSpeed, windDir, windTemp);
		 if(added == false)
		 {
			 legs.updateLeg(legIndex, altitude, TAS, rpms, windSpeed, windDir, windTemp);
		 }
		
	}
	
	private void returnDataToPreviousActivity()
	{
		details.saveLegData();
		Intent resultIntent = new Intent();
		Bundle b = new Bundle();
    	b.putSerializable(CalculationsActivity.legKey, legs);
    	resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
		super.onBackPressed();

	}
	
	
    
    
}
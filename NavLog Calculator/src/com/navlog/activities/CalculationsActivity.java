package com.navlog.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.navlog.calculator.R;
import com.navlog.models.CalculationsModel;
import com.navlog.models.LegData;
import com.navlog.models.LegDataEntry;


public class CalculationsActivity extends Activity implements DetailsFragment.OnDetailsSetListener
, TitlesFragment.ViewHandler{
	
	private CalculationsModel flightData;
	private LegDataEntry legs;
	public static final String legKey = "legs";
	public static final String indexKey = "index";
	int lastIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		loadPreviousActivityFlightData();
		
		restoreLegsState(savedInstanceState);
	    replaceListFragment();
        
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
		if(index != this.lastIndex)
		{
			lastIndex = index;
			LegData leg = getLegData(index);
			DetailsFragment details = DetailsFragment.newInstance(index, leg);
			//Execute a transaction, replaceing any existing fragment
			//with the new one inside the frame
			
			 FragmentTransaction ft = getFragmentManager().beginTransaction();
	         ft.replace(R.id.details, details);
	         ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	         ft.commit();
		}
			
		
    
    }
    
	
	@Override
	protected void onSaveInstanceState(Bundle out)
	{
		super.onSaveInstanceState(out);
		out.putSerializable(legKey, legs);
		out.putInt(indexKey, lastIndex);
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
	

}

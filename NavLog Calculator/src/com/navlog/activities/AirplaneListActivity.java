package com.navlog.activities;

import java.util.LinkedList;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.example.navlog.calculator.R;
import com.navlog.models.AirplaneCollectionModel;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.support.MyListAdaptor;


public class AirplaneListActivity extends ListActivity {
	
	public static final String fileName = "AirplaneList.ser";
	public static final String airplaneProfileKey = "airplane";
	private String profileToEdit;
	AirplaneCollectionModel list = new AirplaneCollectionModel();
	ListView lv;

	private String[] Airplanes;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AirplaneCollectionModel loaded = AirplaneCollectionModel.loadAirplaneList(this.getApplicationContext());
		if(loaded != null)
		{
			list = loaded;
			Airplanes = loaded.getAllLabels();
		}

		LinkedList<String> mLinked = new LinkedList<String>();
		for (int i = 0; i < Airplanes.length; i++) {
			mLinked.add(Airplanes[i]);
		}

		setListAdapter(new MyListAdaptor(this, mLinked));

		ListView lv = getListView();
		lv.setFastScrollEnabled(true);
		AirplaneListClickListener clickListener = new AirplaneListClickListener();
		lv.setOnItemClickListener(clickListener);
	}
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		list.saveAirplaneList(this.getApplicationContext());
	}
	
	private void setListcontent()
	{
		LinkedList<String> mLinked = new LinkedList<String>();
		for (int i = 0; i < Airplanes.length; i++) {
			mLinked.add(Airplanes[i]);
		}

		setListAdapter(new MyListAdaptor(this, mLinked));
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
	    	AirplaneProfileModel profile = new AirplaneProfileModel();
	  		profile = (AirplaneProfileModel) b.getSerializable(airplaneProfileKey);
	  		String label = profile.getLabel();
	  		this.list.removeAirplaneProfile(this.profileToEdit);
	  		this.list.addAirplaneProfile(label, profile);
	  		list.saveAirplaneList(this.getApplicationContext());
	  		
	  		
	  		String[] labels = this.list.getAllLabels();;
	  		this.Airplanes = labels;
	  		
	  		
	  		this.setListcontent();
	      
	      } 
	      break; 
	    } 
	  } 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.add_airplane:
	        	launchAirplaneProfileActivity();
	        	
	        	
	        	break;
	        
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.airplane_list, menu);
		return true;
	}
	
	
	private void launchLoadedProfileActivity(String label)
    {
		this.profileToEdit = label;
    	Intent intent = new Intent(this, AirplaneProfileActivity.class);
    	Bundle b = new Bundle();
    	AirplaneProfileModel profile = this.list.getAirplaneProfile(label);
    	b.putSerializable(airplaneProfileKey, profile);
    	intent.putExtras(b);
    	startActivityForResult(intent, 0);
    	//startActivity(intent);
    }
	
	private void launchAirplaneProfileActivity()
	{
		Intent intent = new Intent(this, AirplaneProfileActivity.class);
		startActivityForResult(intent, 0);
		//startActivity(intent);
	}
	

			public class AirplaneListClickListener implements OnItemClickListener
			{

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text
					String label = (String)((TextView) view).getText();
					launchLoadedProfileActivity(label);
					//Toast.makeText(getApplicationContext(), label, Toast.LENGTH_SHORT).show();
				}
				
			}
	

}

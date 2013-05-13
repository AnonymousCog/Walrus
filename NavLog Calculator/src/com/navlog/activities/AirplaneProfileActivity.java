package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;
import com.navlog.activities.MapActivity.MapSpinnerListener;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.models.CruisePerformanceModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class AirplaneProfileActivity extends Activity {
	private AirplaneProfileModel profile = new AirplaneProfileModel();
	public static final String cruisePerformanceKey = "cruisePerformance";
	private String performanceToEdit;
	private String[] performanceProfileLabels;
	private ProfileSpinnerListener mySpinnerListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airplane_profile);
		loadPreviousActivityFlightData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.airplane_profile, menu);
		return true;
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
	    	CruisePerformanceModel performance = new CruisePerformanceModel();
	  		performance = (CruisePerformanceModel) b.getSerializable(cruisePerformanceKey);
	  		String label = performance.getLabel();
	  		//this.profile.removeCruisePerformanceParam(performanceToEdit);
	  		this.profile.addCruisePerformanceParam(label, performance);
	  		
	  		
	  
	  		this.performanceProfileLabels = this.profile.getAllLabels();
	  		
	  		
	  		this.populateSpinner();
	      
	      } 
	      break; 
	    } 
	  } 
	}
	
	@Override
	public void onBackPressed()
	{
		final String items[] = {"Save and Exit", "Don't Save"};

		AlertDialog.Builder ab=new AlertDialog.Builder(this);
		ab.setTitle("Keep Changes?");
		ab.setItems(items, new NLExitClickListener());
		ab.show();
	
	}
	
	private void goBack()
	{
		super.onBackPressed();
	}
	
	
	
	
	public void launchLoadedCruisePerformanceActivity(String label)
    {
		this.performanceToEdit = label;
    	Intent intent = new Intent(this, CruisePerformanceActivity.class);
    	Bundle b = new Bundle();
    	CruisePerformanceModel performance = this.profile.getCruisePerformanceParam(label);
    	b.putSerializable(cruisePerformanceKey, performance);
    	intent.putExtras(b);
    	startActivityForResult(intent, 0);
    }
	
	public void launchCruisePerformanceActivity(View view)
	{
		Intent intent = new Intent(this, CruisePerformanceActivity.class);
		startActivityForResult(intent, 0);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.save_profile:
	        	returnDataToPreviousActivity();
	        	
	        	
	        	break;
	        

	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	public void save(View view)
	{
		returnDataToPreviousActivity();
	}
	
	public void returnDataToPreviousActivity()
	{
		Intent resultIntent = new Intent();
		Bundle b = new Bundle();
    	
		profile.setAirplaneBrand(this.getAirplaneBrandField());
    	profile.setAirplaneModel(this.getAirplaneModelField());
    	profile.setAirplaneName(this.getAirplaneNameField());
    	profile.setAirplaneTailNumber(this.getAirplaneTailNumberField());
    	
    	b.putSerializable(AirplaneListActivity.airplaneProfileKey, profile);
    	resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();

		
	}
	
	
	private void loadPreviousActivityFlightData()
	{
		if(getIntent().hasExtra(AirplaneListActivity.airplaneProfileKey))
		{
			Bundle b = new Bundle();
			b = getIntent().getExtras();
			profile = (AirplaneProfileModel) b.getSerializable(AirplaneListActivity.airplaneProfileKey);
			this.setAirplaneBrandField(profile.getAirplaneBrand());
			this.setAirplaneModelField(profile.getAirplaneModel());
			this.setAirplaneNameField(profile.getAirplaneName());
			this.setAirplaneTailNumberField(profile.getAirplaneTailNumber());
			populateSpinner();
		}
			
		
	}
	
    public void populateSpinner()
    {
    	String[] performanceLabels = profile.getAllLabels();
    	if(performanceLabels.length > 0)
    	{
	        Spinner spinner = (Spinner) findViewById(R.id.performanceSpinner);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>
	        	(this, android.R.layout.simple_spinner_item, performanceLabels);
	        
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner.setAdapter(adapter);
	        
	        mySpinnerListener = new ProfileSpinnerListener();
	        spinner.setOnItemSelectedListener(mySpinnerListener);
    	}
    }
	
	
	
	public String getAirplaneNameField()
	{
		EditText airplaneName = (EditText) findViewById(R.id.airplaneName);
    	String name = airplaneName.getText().toString();
    	return name;
    	
	}
	
	public void setAirplaneNameField(String name)
	{
		EditText airplaneName = (EditText) findViewById(R.id.airplaneName);
    	airplaneName.setText(name);
	}
    	
	
	public String getAirplaneBrandField()
	{
		EditText airplaneBrand = (EditText) findViewById(R.id.airplaneBrand);
    	String brand = airplaneBrand.getText().toString();
    	return brand;
	}
	
	public void setAirplaneBrandField(String brand)
	{
		EditText airplaneBrand = (EditText) findViewById(R.id.airplaneBrand);
    	airplaneBrand.setText(brand);
	}
	
	public String getAirplaneModelField()
	{
		EditText airplaneModel = (EditText) findViewById(R.id.airplaneModel);
    	String model = airplaneModel.getText().toString();
    	return model;
	}
	
	public void setAirplaneModelField(String model)
	{
		EditText airplaneModel = (EditText) findViewById(R.id.airplaneModel);
    	airplaneModel.setText(model);
	}
	
	public String getAirplaneTailNumberField()
	{
		EditText airplaneTail = (EditText) findViewById(R.id.airplaneTailNum);
    	String tail = airplaneTail.getText().toString();
    	return tail;
	}
	
	public void setAirplaneTailNumberField(String tailNum)
	{
		EditText airplaneTail = (EditText) findViewById(R.id.airplaneTailNum);
    	airplaneTail.setText(tailNum);
	}
	
	public class NLExitClickListener implements DialogInterface.OnClickListener
	{
		public void onClick(DialogInterface d, int choice) 
		{
			if(choice == 0)
			{
				returnDataToPreviousActivity();
			}
			else if(choice == 1)
			{
				goBack();
			}
		}
			
	}
	
	public class ProfileSpinnerListener implements OnItemSelectedListener
    {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) 
		{
			TextView view = (TextView) arg1;
			Toast.makeText(getApplicationContext(),"Selected : " + view.getText().toString(), Toast.LENGTH_SHORT).show();
			launchLoadedCruisePerformanceActivity(view.getText().toString());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }


}

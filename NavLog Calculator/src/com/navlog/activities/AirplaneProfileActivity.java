package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.models.FlightModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AirplaneProfileActivity extends Activity {
	private AirplaneProfileModel profile = new AirplaneProfileModel();

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
	
	public void createPerformanceParameter(View view)
	{
    	Intent intent = new Intent(this, CruisePerformanceActivity.class);
    	startActivity(intent);
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


}

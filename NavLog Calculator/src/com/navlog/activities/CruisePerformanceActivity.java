package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.models.CruisePerformanceModel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CruisePerformanceActivity extends Activity {
	CruisePerformanceModel performance = new CruisePerformanceModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cruise_performance);
		loadPreviousActivityPerformanceParameters();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cruise_performance, menu);
		return true;
	}
	
	
	private void loadPreviousActivityPerformanceParameters()
	{
		if(getIntent().hasExtra(AirplaneProfileActivity.cruisePerformanceKey))
		{
			Bundle b = new Bundle();
			b = getIntent().getExtras();
			performance = (CruisePerformanceModel) b.getSerializable(AirplaneProfileActivity.cruisePerformanceKey);
			this.setAltitude(performance.getAltitude());
			this.setRPM(performance.getRpm());
			
			this.setKTASBelow20std(performance.getBelow20Ktas());
			this.setGPHBelow20std(performance.getBelow20Gph());
			
			this.setKTASstd(performance.getStd20Ktas());
			this.setGPHstd(performance.getStd20Gph());
			
			this.setKTASAbove20std(performance.getAbove20Ktas());
			this.setGPHAbove20std(performance.getAbove20Gph());
		
		}
			
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.save_performance:
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
		
		performance.setAltitude(this.getAltitude());
		performance.setRpm(this.getRPM());
		performance.setBelow20Ktas(this.getKTASBelow20std());
		performance.setBelow20Gph(this.getGPHBelow20std());
		performance.setStd20Ktas(this.getKTASstd());
		performance.setStd20Gph(this.getGPHstd());
		performance.setAbove20Ktas(this.getKTASAbove20std());
		performance.setAbove20Gph(this.getGPHAbove20std());
    	
		b.putSerializable(AirplaneProfileActivity.cruisePerformanceKey, performance);
    	resultIntent.putExtras(b);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();

		
	}
	
	
	
	
	public int getAltitude()
	{
		EditText alt = (EditText) findViewById(R.id.performanceAltitude);
    	int altitude = Integer.parseInt(alt.getText().toString());
    	return altitude;
    	
	}
	
	public void setAltitude(int a)
	{
		EditText altitude = (EditText) findViewById(R.id.performanceAltitude);
    	altitude.setText(Integer.toString(a));
	}
	
	public int getRPM()
	{
		EditText rpm = (EditText) findViewById(R.id.performanceRPM);
    	int r = Integer.parseInt(rpm.getText().toString());
    	return r;
    	
	}
	
	public void setRPM(int r)
	{
		EditText rpm = (EditText) findViewById(R.id.performanceRPM);
    	rpm.setText(Integer.toString(r));
	}
	
	public double getKTASBelow20std()
	{
		EditText ktas = (EditText) findViewById(R.id.below20cKTAS);
    	double k = Double.parseDouble(ktas.getText().toString());
    	return k;
    	
	}
	
	public void setKTASBelow20std(Double k)
	{
		EditText ktas = (EditText) findViewById(R.id.below20cKTAS);
		ktas.setText(Double.toString(k));
	}
	
	
	public double getKTASstd()
	{
		EditText ktas = (EditText) findViewById(R.id.stdKTAS);
    	double k = Double.parseDouble(ktas.getText().toString());
    	return k;
    	
	}
	
	public void setKTASstd(Double k)
	{
		EditText ktas = (EditText) findViewById(R.id.stdKTAS);
		ktas.setText(Double.toString(k));
	}
	
	
	public double getKTASAbove20std()
	{
		EditText ktas = (EditText) findViewById(R.id.above20cKTAS);
		double k = Double.parseDouble(ktas.getText().toString());
    	return k;
    	
	}
	
	public void setKTASAbove20std(Double k)
	{
		EditText gph = (EditText) findViewById(R.id.above20cKTAS);
		gph.setText(Double.toString(k));
	}
	
	
	public double getGPHBelow20std()
	{
		EditText gph = (EditText) findViewById(R.id.below20cGPH);
		double g = Double.parseDouble(gph.getText().toString());
    	return g;
    	
	}
	
	public void setGPHBelow20std(Double g)
	{
		EditText gph = (EditText) findViewById(R.id.below20cGPH);
		gph.setText(Double.toString(g));
	}
	
	
	public double getGPHstd()
	{
		EditText ktas = (EditText) findViewById(R.id.stdGPH);
		double g = Double.parseDouble(ktas.getText().toString());
    	return g;
    	
	}
	
	public void setGPHstd(Double g)
	{
		EditText ktas = (EditText) findViewById(R.id.stdGPH);
		ktas.setText(Double.toString(g));
	}
	
	public double getGPHAbove20std()
	{
		EditText ktas = (EditText) findViewById(R.id.above20cGPH);
		double g = Double.parseDouble(ktas.getText().toString());
    	return g;
    	
	}
	
	public void setGPHAbove20std(Double g)
	{
		EditText ktas = (EditText) findViewById(R.id.above20cGPH);
		ktas.setText(Double.toString(g));
	}
	
	
	
	

}

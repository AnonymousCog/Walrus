package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.navlog.models.FlightModel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

public class CalculationsActivity extends Activity {
	
	FlightModel flightData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculations);
		loadPreviousActivityFlightData();
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
		flightData = new FlightModel();
		flightData = (FlightModel) b.getSerializable(MapActivity.flightModelDetails);
		
		String deptICAO = flightData.getDepartureICAO();
		String destICAO = flightData.getDestinationICAO();
		Toast.makeText(getApplicationContext(), "deptICAO : "+deptICAO+ " Dest ICAO : "+destICAO, Toast.LENGTH_SHORT).show();		
		
	}

}

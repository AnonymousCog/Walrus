package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AirplaneProfileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airplane_profile);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.airplane_profile, menu);
		return true;
	}

}

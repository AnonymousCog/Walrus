package com.navlog.activities;

import com.example.navlog.calculator.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CalculationsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculations);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculations, menu);
		return true;
	}

}

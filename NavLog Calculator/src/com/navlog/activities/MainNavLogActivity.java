package com.navlog.activities;

import com.example.navlog.calculator.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainNavLogActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_log);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_nav_log, menu);
        return true;
    }
    
    public void GoToNewFlight(View view)
    {
    	Intent intent = new Intent(this, NewFlightActivity.class);
    	startActivity(intent);
    }
    
    public void goToAirplaneList(View view)
    {
    	Intent intent = new Intent(this, AirplaneListActivity.class);
    	startActivity(intent);
    }
    public void goToMapDownload(View view)
    {
    	Intent intent = new Intent(this, MapDownloadActivity.class);
    	startActivity(intent);
    }
}

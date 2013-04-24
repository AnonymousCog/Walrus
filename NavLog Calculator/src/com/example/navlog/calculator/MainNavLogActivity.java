package com.example.navlog.calculator;

import com.example.navlog.calculator.R;

import android.app.Activity;
import android.content.Intent;
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
    	//Toast.makeText(getApplicationContext(), "Goin to new Flight", Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent(this, NewFlightActivity.class);
    	startActivity(intent);
    }
    
    public void logOutPressed(View view)
    {
    	Intent intent = new Intent(this, LogInOrRegisterActivity.class);
    	startActivity(intent);
    }
    public void goToSetup(View view)
    {
    	//Intent intent = new Intent(this, MapDownloadActivity.class);
    	Intent intent = new Intent(this, AirplaneListActivity.class);
    	startActivity(intent);
    }
}

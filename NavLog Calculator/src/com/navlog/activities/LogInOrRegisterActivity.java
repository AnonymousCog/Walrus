package com.navlog.activities;

import com.example.navlog.calculator.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class LogInOrRegisterActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_or_regiister);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_in_or_regiister, menu);
        return true;
    }
    
    public void logInPressed(View view)
    {
    	Toast.makeText(getApplicationContext(), "Logging In...", Toast.LENGTH_SHORT).show();
    	

    }
}

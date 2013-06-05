package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;
import com.navlog.activities.AirplaneListActivity.AirplaneListClickListener;
import com.navlog.activities.AirplaneListActivity.AirplaneListContextualtMenu;
import com.navlog.models.AirplaneCollectionModel;
import com.navlog.models.CalculationsCollectionModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CalculationsHistoryActivity extends ListActivity {
	private CalculationsCollectionModel list = new CalculationsCollectionModel();
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CalculationsCollectionModel loaded = CalculationsCollectionModel.loadCalculationsList(this.getApplicationContext());
		if(loaded != null)
		{
			list = loaded;
			String[] calculations = loaded.getAllLabels();
			
			setListAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_activated_1, calculations));
			lv = getListView();
			lv.setFastScrollEnabled(true);
			//AirplaneListClickListener clickListener = new AirplaneListClickListener();
			//lv.setOnItemClickListener(clickListener);
			//lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			//lv.setMultiChoiceModeListener(new AirplaneListContextualtMenu());	   
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculations_history, menu);
		return true;
	}
	
	
	
	
	
	
	

}

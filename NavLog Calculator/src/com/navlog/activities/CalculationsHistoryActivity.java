package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.layout;
import com.example.navlog.calculator.R.menu;
import com.navlog.activities.AirplaneListActivity.AirplaneListClickListener;
import com.navlog.activities.AirplaneListActivity.AirplaneListContextualtMenu;
import com.navlog.models.AirplaneCollectionModel;
import com.navlog.models.AirplaneProfileModel;
import com.navlog.models.CalculationsCollectionModel;
import com.navlog.models.CalculationsModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CalculationsHistoryActivity extends ListActivity {
	public final static String flightKey = "flight";
	private CalculationsCollectionModel list = new CalculationsCollectionModel();
	ListView lv;
	private String selectedProfile;

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
			lv.getId();
			CalculationsListClickListener clickListener = new CalculationsListClickListener();
			lv.setOnItemClickListener(clickListener);
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
	
	public void launchLoadedMapActivity(String label)
	{
		Intent intent = new Intent(this, MapActivity.class);
    	Bundle b = new Bundle();
    	CalculationsCollectionModel storage =  CalculationsCollectionModel.loadCalculationsList(this);
    	CalculationsModel calc = storage.getCalculationsModel(label);
    	b.putSerializable(flightKey, calc);
    	intent.putExtras(b);
    	setEditableFalse();
    	startActivity(intent);
	}
	
	public void setEditableFalse()
	{
		SharedPreferences settings = getSharedPreferences("waypoints_editable", 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("editable", false);
	    editor.commit();
	}
	
	public void deleteFlight(String label)
	{
		CalculationsCollectionModel storage = CalculationsCollectionModel.loadCalculationsList(getApplicationContext());
		storage.removeCalculationsModel(label);
		storage.saveCalculationsList(getApplicationContext());
		String [] calculations = storage.getAllLabels();
		setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, calculations));
	}
	
	public class CalculationsListClickListener implements OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) 
		{	
			selectedProfile = (String)((TextView) view).getText();
			final CharSequence[] items = {"Open", "Delete"};

			AlertDialog.Builder builder = new AlertDialog.Builder(CalculationsHistoryActivity.this);
			builder.setTitle("Select an option");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();

			        if(items[item].equals("Open"))
			        {
			        	launchLoadedMapActivity(selectedProfile);
			        }
			        else if (items[item].equals("Delete"))
			        {
			        	deleteFlight(selectedProfile);
			        }
			    }
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
		
	}
		

}

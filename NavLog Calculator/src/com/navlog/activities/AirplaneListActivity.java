package com.navlog.activities;

import java.util.LinkedList;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navlog.calculator.R;
import com.navlog.models.AirplaneCollectionModel;
import com.navlog.models.AirplaneProfileModel;



public class AirplaneListActivity extends ListActivity {
	
	public static final String fileName = "AirplaneList.ser";
	public static final String airplaneProfileKey = "airplane";
	private String profileToEdit;
	AirplaneCollectionModel list = new AirplaneCollectionModel();
	ListView lv;

	private String[] airplanes;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AirplaneCollectionModel loaded = AirplaneCollectionModel.loadAirplaneList(this.getApplicationContext());
		if(loaded != null)
		{
			list = loaded;
			airplanes = loaded.getAllLabels();
			
			setListAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_activated_1, airplanes));
			ListView lv = getListView();
			lv.setFastScrollEnabled(true);
			AirplaneListClickListener clickListener = new AirplaneListClickListener();
			lv.setOnItemClickListener(clickListener);
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			lv.setMultiChoiceModeListener(new ModeCallback());
			
		}
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActionBar().setSubtitle("Current: ");
    }
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		list.saveAirplaneList(this.getApplicationContext());
	}
	
	private void setListcontent()
	{
		setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_activated_1, airplanes));
	}
	
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
	    case (0) : { 
	      if (resultCode == Activity.RESULT_OK) 
	      { 
	    	
	    	Bundle b = data.getExtras();
	    	AirplaneProfileModel profile = new AirplaneProfileModel();
	  		profile = (AirplaneProfileModel) b.getSerializable(airplaneProfileKey);
	  		String label = profile.getLabel();
	  		this.list.removeAirplaneProfile(this.profileToEdit);
	  		this.list.addAirplaneProfile(label, profile);
	  		list.saveAirplaneList(this.getApplicationContext());
	  		
	  		
	  		String[] labels = this.list.getAllLabels();;
	  		this.airplanes = labels;
	  		
	  		
	  		this.setListcontent();
	      
	      } 
	      break; 
	    } 
	  } 
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.add_airplane:
	        	launchAirplaneProfileActivity();
	        	
	        	
	        	break;
	        
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.airplane_list, menu);
		return true;
	}
	
	
	private void launchLoadedProfileActivity(String label)
    {
		this.profileToEdit = label;
    	Intent intent = new Intent(this, AirplaneProfileActivity.class);
    	Bundle b = new Bundle();
    	AirplaneProfileModel profile = this.list.getAirplaneProfile(label);
    	b.putSerializable(airplaneProfileKey, profile);
    	intent.putExtras(b);
    	startActivityForResult(intent, 0);
    	//startActivity(intent);
    }
	
	private void launchAirplaneProfileActivity()
	{
		Intent intent = new Intent(this, AirplaneProfileActivity.class);
		startActivityForResult(intent, 0);
		//startActivity(intent);
	}
	

			public class AirplaneListClickListener implements OnItemClickListener
			{

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text
					String label = (String)((TextView) view).getText();
					launchLoadedProfileActivity(label);
					//Toast.makeText(getApplicationContext(), label, Toast.LENGTH_SHORT).show();
				}
				
			}
			
		    private class ModeCallback implements ListView.MultiChoiceModeListener {

		        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		            MenuInflater inflater = getMenuInflater();
		            inflater.inflate(R.menu.airplane_list_contextual, menu);
		            mode.setTitle("Select Items");
		            return true;
		        }

		        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		            return true;
		        }

		        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		            switch (item.getItemId()) {
		            case R.id.delete_airplane:
		                Toast.makeText(AirplaneListActivity.this, "Shared " + getListView().getCheckedItemCount() +
		                        " items", Toast.LENGTH_SHORT).show();
		                mode.finish();
		                break;
		            case R.id.set_airplane:
		            	int count = getListView().getCheckedItemCount();
		            	if(count < 2 && count > 0 )
		            	{
		            		String label ="stipid"; //(String)((TextView) getListView().getSelectedItem()).getText();
		            		 getActionBar().setSubtitle("Current: "+ label);
		            		
		            	}
		            	else
		            	{
		            		Toast.makeText(AirplaneListActivity.this, "Only one Plane must be selected" + item.getTitle(),
			                        Toast.LENGTH_SHORT).show();
		            	}
		            	mode.finish();
		                break;
		                
		            default:
		                Toast.makeText(AirplaneListActivity.this, "Clicked " + item.getTitle(),
		                        Toast.LENGTH_SHORT).show();
		                mode.finish();
		                break;
		            }
		            return true;
		        }

		        public void onDestroyActionMode(ActionMode mode) {
		        }

		        public void onItemCheckedStateChanged(ActionMode mode,
		                int position, long id, boolean checked) {
		            final int checkedCount = getListView().getCheckedItemCount();
		            switch (checkedCount) {
		                case 0:
		                    mode.setSubtitle(null);
		                    break;
		                case 1:
		                    mode.setSubtitle("One item selected");
		                    break;
		                default:
		                    mode.setSubtitle("" + checkedCount + " items selected");
		                    break;
		            }
		        }
		        
		    }

	

}

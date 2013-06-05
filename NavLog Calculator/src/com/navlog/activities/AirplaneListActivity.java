package com.navlog.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
	
	public static final String selected_plane_pref = "selectedPlane";
	public static final String selected = "selected";
	public static final String fileName = "AirplaneList.ser";
	public static final String airplaneProfileKey = "airplane";
	private String profileToEdit;
	AirplaneCollectionModel list = new AirplaneCollectionModel();
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		AirplaneCollectionModel loaded = AirplaneCollectionModel.loadAirplaneList(this.getApplicationContext());
		if(loaded != null)
		{
			list = loaded;
			String[] airplanes = loaded.getAllLabels();
			
			setListAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_activated_1, airplanes));
			lv = getListView();
			lv.setFastScrollEnabled(true);
			AirplaneListClickListener clickListener = new AirplaneListClickListener();
			lv.setOnItemClickListener(clickListener);
			lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			lv.setMultiChoiceModeListener(new AirplaneListContextualtMenu());	
		       
		}
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getActionBar().setSubtitle("Selected: "+ getSelectedPlanePref());
    }                               
    
    private String getSelectedPlanePref()
    {
    	SharedPreferences settings = getSharedPreferences(selected_plane_pref, 0);
        String plane = settings.getString(selected, "None");
    	return plane;
    }
    
    private void setSelectedPlanePref(String plane)
    {
    	// We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(selected_plane_pref, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(selected, plane);

        // Commit the edits!
        editor.commit();
        getActionBar().setSubtitle("Selected: "+ plane);
    	
    }
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		list.saveAirplaneList(this.getApplicationContext());
	}
	
	private void setListcontent()
	{
		String[] airplanes = list.getAllLabels();
		if(airplanes != null)
		{
			setListAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_activated_1, airplanes));
		}
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
    }
	
	private void launchAirplaneProfileActivity()
	{
		Intent intent = new Intent(this, AirplaneProfileActivity.class);
		startActivityForResult(intent, 0);
	}
    private void setSelectedAirplane()
    {
    	String[] airplanes = list.getAllLabels();
    	int count = getListView().getCheckedItemCount();
    	if(count < 2 && count > 0 )
    	{
    		SparseBooleanArray checked = getListView().getCheckedItemPositions();
    		int listSize = getListView().getCount();
            for(int i = 0; i < listSize; i++){
                if(checked.get(i)){
                    setSelectedPlanePref(airplanes[i]);
                }
            }
    			
    	}
    	else
    	{
    		Toast.makeText(AirplaneListActivity.this, "Only one Plane must be selected",
                    Toast.LENGTH_SHORT).show();
    	}
    	
    }
    
    private void deleteAirplanes()
    {
    	String[] airplanes = list.getAllLabels();
    	int count = getListView().getCheckedItemCount();
    	if(count > 0 )
    	{
    		SparseBooleanArray checked = getListView().getCheckedItemPositions();
    		int listSize = getListView().getCount();
            for(int i = 0; i < listSize; i++){
                if(checked.get(i))
                	//Must implement persistent storage of selected
                	this.list.removeAirplaneProfile(airplanes[i]);
                	if(getSelectedPlanePref().equals(airplanes[i]))
                	{
                		this.setSelectedPlanePref("None");
                	}
            }
	  		list.saveAirplaneList(this.getApplicationContext());	    	  		
	  		this.setListcontent();		
    	}
    }

			public class AirplaneListClickListener implements OnItemClickListener
			{
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String label = (String)((TextView) view).getText();
					launchLoadedProfileActivity(label);
				}
				
			}
			
		    public class AirplaneListContextualtMenu implements ListView.MultiChoiceModeListener {

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
		            	deleteAirplanes();
		                mode.finish();
		                break;
		                
		            case R.id.set_airplane:
		            	setSelectedAirplane();
		            	mode.finish();
		                break;
		                
		            default:
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

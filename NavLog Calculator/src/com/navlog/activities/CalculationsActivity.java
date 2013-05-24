package com.navlog.activities;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.navlog.calculator.R;
import com.example.navlog.calculator.R.id;
import com.navlog.models.FlightModel;

public class CalculationsActivity extends Activity {
	
	private FlightModel flightData;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_layout);
		loadPreviousActivityFlightData();
		
		 
       if (savedInstanceState == null) {
            // First-time init; create fragment to embed in activity.
        	FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment newFragment = TitlesFragment.newInstance(flightData); 
            ft.add(R.id.titles, newFragment);
            //ft.addToBackStack(null);
            ft.commit();
        }
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
	

		
	public static class DetailsActivity extends Activity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);

	        if (getResources().getConfiguration().orientation
	                == Configuration.ORIENTATION_LANDSCAPE) 
	        {
	            // If the screen is now in landscape mode, we can show the
	            // dialog in-line with the list so we don't need this activity.
	            finish();
	            return;
	        }

	        if (savedInstanceState == null) 
	        {
	            // During initial setup, plug in the details fragment.
	            DetailsFragment details = new DetailsFragment();
	            details.setArguments(getIntent().getExtras());
	            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
	        }
	    }
	    
	    
	}
	
	public static class TitlesFragment extends ListFragment
	{
		boolean mDualPane;
		int mCurCheckPosition;
		FlightModel data;
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			Bundle args = getArguments();
			if(args != null)
			{
				data = (FlightModel) getArguments().getSerializable(MapActivity.flightModelDetails);
			}
			
			
		}
		
		
		@Override
		public void onActivityCreated(Bundle savedInstance)
		{
			super.onActivityCreated(savedInstance);
			String[] locations = {" "};
			if(data != null)
			{
				//FlightModel d = this.getData();
				locations = data.getAllLabels();
			
				//populate list with strings
				//String[] locations = {"TJSJ", "TJMZ"}; 
				setListAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_list_item_activated_1, locations));
				
			}
			setListShown(true);
			//check to see if we have a frame in ehich to embed the details
			//fragment directly containing UI
			View detailsFrame = getActivity().findViewById(R.id.details);
			mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
			
			if (savedInstance != null)
			{
				//restore last state for checked posistion
				mCurCheckPosition = savedInstance.getInt("curChoice", 0); 
			}
			
			if(mDualPane)
			{
				// in dual-pane mode, the list view highlights the selected item
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				//make sure the UI is in correct state
				showDetails(mCurCheckPosition);
			}
			
		}
		
		
		 @Override
		    public void onSaveInstanceState(Bundle outState) 
		 	{
		        super.onSaveInstanceState(outState);
		        outState.putInt("curChoice", mCurCheckPosition);
		    }

		    @Override
		    public void onListItemClick(ListView l, View v, int position, long id) 
		    {
		        showDetails(position);
		    }
		    
		    public static TitlesFragment newInstance(FlightModel data) {
		    	TitlesFragment f = new TitlesFragment();
		    	Bundle args = new Bundle();
		        args.putSerializable(MapActivity.flightModelDetails, data);
		        f.setArguments(args);
		        return f;
		    }

		     
		    /**
		     * Helper function to show the details of a selected item, either by
		     * displaying a fragment in-place in the current UI, or starting a
		     * whole new activity in which it is displayed.
		     */
		    void showDetails(int index)
		    {
		    	mCurCheckPosition = index;
		    	if(mDualPane)
		    	{
		    		//display everytihng in place with fragments, so update
		    		// the liast to highlight the selected item and show the data
		    		
		    		getListView().setItemChecked(index, true);
		    		
		    		//check what fragment is currently shown, replace if needed
		    		DetailsFragment details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
		    		if(details == null || details.getShownIndex() != index)
		    		{
		    			details = DetailsFragment.newInstance(index);
		    			
		    			//Execute a transaction, replaceing any existing fragment
		    			//with the new one inside the frame
		    			
		    			 FragmentTransaction ft = getFragmentManager().beginTransaction();
		                 ft.replace(R.id.details, details);
		                 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		                 ft.commit();
		    			
		    		}
		    	}
		    		else
		    		{
		    			Intent intent = new Intent();
		    			intent.setClass(getActivity(), DetailsActivity.class);
		    			intent.putExtra("index", index);
		    			startActivity(intent);
		    		}
		    		
		    		
		    	
		    }

	}
	
	public static class DetailsFragment extends Fragment {
		/**
	     * Create a new instance of DetailsFragment, initialized to
	     * show the text at 'index'.
	     */
	    public static DetailsFragment newInstance(int index) {
	        DetailsFragment f = new DetailsFragment();

	        // Supply index input as an argument.
	        Bundle args = new Bundle();
	        args.putInt("index", index);
	        f.setArguments(args);

	        return f;
	    }

	    public int getShownIndex() {
	        return getArguments().getInt("index", 0);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        if (container == null) {
	            // We have different layouts, and in one of them this
	            // fragment's containing frame doesn't exist.  The fragment
	            // may still be created from its saved state, but there is
	            // no reason to try to create its view hierarchy because it
	            // won't be displayed.  Note this is not needed -- we could
	            // just run the code below, where we would create and return
	            // the view hierarchy; it would just never be used.
	            return null;
	        }

	        ScrollView scroller = new ScrollView(getActivity());
	        TextView text = new TextView(getActivity());
	        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                4, getActivity().getResources().getDisplayMetrics());
	        text.setPadding(padding, padding, padding, padding);
	        scroller.addView(text);
	        
	        
	        
	        //text.setText();
	        return scroller;
	    }
	}


}

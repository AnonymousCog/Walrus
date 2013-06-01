package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.navlog.activities.DetailsFragment.OnDetailsSetListener;
import com.navlog.models.CalculationsModel;
import com.navlog.models.FlightWaypointsModel;
import com.navlog.models.LegDataEntry;


import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment
{
	boolean mDualPane;
	int mCurCheckPosition;
	private FlightWaypointsModel data;
	private LegDataEntry legs;
	private ViewHandler callBack;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args != null)
		{
			data = (FlightWaypointsModel) getArguments().getSerializable(MapActivity.flightModelDetails);
			mCurCheckPosition = (int) getArguments().getInt("curChoice");
			legs = (LegDataEntry) getArguments().getSerializable(CalculationsActivity.legKey);
		}
		
		
	}
	
	@Override
	public void onAttach(Activity a)
	{
		super.onAttach(a);
		try
		{
			callBack = (ViewHandler) a;
		}
		catch (ClassCastException e) 
		{
            throw new ClassCastException(a.toString()
                    + " must implement ViewHandler");
		}

	}
	
	public void setLegData(LegDataEntry l)
	{
		legs = l;
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
	    
	    public static TitlesFragment newInstance(CalculationsModel data, LegDataEntry legs, int index) {
	    	TitlesFragment f = new TitlesFragment();
	    	Bundle args = new Bundle();
	        args.putSerializable(MapActivity.flightModelDetails, data);
	        args.putSerializable(CalculationsActivity.legKey, legs);
	        args.putInt("curChoice", index);
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
	    			callBack.displayDetailsFragment(details ,index);
	    			
	    		}
	    	}
	    		else
	    		{
	    			callBack.displayDetailsActivity(index);
	    		}
	    		
	    }
	    
	    
	    public interface ViewHandler{
			public void displayDetailsActivity(int index);
			public void displayDetailsFragment(DetailsFragment details, int index);
		}

}


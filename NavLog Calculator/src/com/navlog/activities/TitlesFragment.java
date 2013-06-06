package com.navlog.activities;

import com.example.navlog.calculator.R;
import com.navlog.models.CalculationsModel;
import com.navlog.models.FlightWaypointsModel;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesFragment extends ListFragment
{
	  public interface ViewHandler{
			public void displayDetailsFragment(int index);
		}
	
	
	int mCurCheckPosition;
	private FlightWaypointsModel data;
	private ViewHandler callBack;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args != null)
		{
			data = (FlightWaypointsModel) getArguments().getSerializable(MapActivity.flightModelDetails);
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
		
	@Override
	public void onActivityCreated(Bundle savedInstance)
	{
		super.onActivityCreated(savedInstance);
		String[] locations = {" "};
		if(data != null)
		{
			locations = data.getAllLabels(); 
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_activated_1, locations));
			
		}
		setListShown(true);
			
		if (savedInstance != null)
		{
			//restore last state for checked posistion
			mCurCheckPosition = savedInstance.getInt("curChoice", 0); 
		}
		
		// in dual-pane mode, the list view highlights the selected item
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//make sure the UI is in correct state
		showDetails(mCurCheckPosition);	
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
	    
	    public static TitlesFragment newInstance(CalculationsModel data) {
	    	TitlesFragment f = new TitlesFragment();
	    	Bundle args = new Bundle();
	        args.putSerializable(MapActivity.flightModelDetails, data);
	        f.setArguments(args);
	        return f;
	    }

	     
	    /**
	     * Helper function to show the details of a selected item by
	     * displaying a fragment in-place in the current UI
	     */
	    void showDetails(int index)
	    {
	    	mCurCheckPosition = index;
    		getListView().setItemChecked(index, true);
    		callBack.displayDetailsFragment(index);
	    }
}


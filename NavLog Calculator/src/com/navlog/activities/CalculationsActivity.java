package com.navlog.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.navlog.calculator.R;
import com.navlog.models.CalculationsModel;
import com.navlog.models.FlightModel;
import com.navlog.models.LegDataEntry;

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
		private FlightModel data;
		
		
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
		private EditText windsAloftDir; // Degrees
		private TextView windsAloftDirLabel;
		private EditText windsAloftVel; //MPH
		private TextView windsAloftVelLabel;
		private EditText windsAloftTemp; //F
		private TextView windsAloftTempLabel;
		private EditText altitude;
		private TextView altitudeLabel;
		private EditText tas;
		private TextView tasLabel;
		private EditText tc; //true course
		private TextView tcLabel; 
		private EditText wca; //wind correction angle
		private TextView wcaLabel;
		private EditText th; //true heading
		private TextView thLabel;
		private EditText mh;//magnetic heading
		private TextView mhLabel;
		private EditText remainingLegDistance;
		private TextView remainingLegDistanceLabel;
		private EditText totalLegDistance;
		private TextView totalLegDistanceLabel;
		private EditText gsEst; //ground speed estimated
		private TextView gsEstLabel;
		private EditText gsAct; //ground speed actual
		private TextView gsActLabel;
		private TimePicker timeOff; //take off time
		private TextView timeOffLabel;
		private EditText ete; //estimated time en route
		private TextView eteLabel;
		private EditText eta; //estimated time of arrival
		private TextView etaLabel;
		private EditText ate; //actual time enroute
		private TextView ateLabel;
		private TimePicker ata; //actual time of arrival
		private TextView ataLabel;
		private EditText gph; //gallons per hour
		private TextView gphLabel;
		private EditText fuel; 
		private TextView fuelLabel;
		private EditText fuelRemaining; 
		private TextView fuelRemainingLabel;
		private EditText rpm; //gallons per hour
		private TextView rpmLabel;
		private EditText var; //gallons per hour
		private TextView varLabel;
		
		
		
		
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
	        
	        // Grabbing the Application context
	        Context context = getActivity();
	         
	        // Creating a new LinearLayout
	        LinearLayout linearLayout = new LinearLayout(context);
	         
	        // Setting the orientation to vertical
	        linearLayout.setOrientation(LinearLayout.VERTICAL);
	         
	        
	        this.rpmLabel = initTextView("Engine Revolutions Per Minute:");
	        this.windsAloftDirLabel = initTextView("Winds Aloft Direction:");
	        this.windsAloftVelLabel = initTextView("Winds Aloft Velocity:");
	        this.windsAloftTempLabel =initTextView("Winds Aloft Temperature:");
	        this.altitudeLabel = initTextView("Altitude:");
	        this.tasLabel = initTextView("True Air Speed");
	        this.gphLabel = initTextView("Gallons Per Hour:");
	        this.tcLabel = initTextView("True Course:");
	        this.wcaLabel = initTextView("Wind Correction Angle:");
	        this.thLabel = initTextView("True Heading:");
	        this.mhLabel = initTextView("Magnetic Heading:");
	        this.remainingLegDistanceLabel = initTextView("Remaining Leg Distance");
	        this.totalLegDistanceLabel = initTextView("Total Leg Distance:");
	        this.gsEstLabel = initTextView("Estimated Ground Speed:");
	        this.gsActLabel = initTextView("Actual Ground Speed:");
	        this.timeOffLabel = initTextView("Take off time:");
	        this.eteLabel = initTextView("Estimated Time En Route:");
	        this.etaLabel = initTextView("Estimated Time of Arrival:");
	        this.ateLabel = initTextView("Actual Time En Route:");
	        this.ataLabel = initTextView("Actual Time of Arrival:");
	        this.fuelLabel = initTextView("Fuel");
	        this.fuelRemainingLabel = initTextView("Remainig Fuel");
	        this.varLabel = initTextView("Varitation");
	        
	        this.rpm = initEditText("RPM");
	        this.windsAloftDir = initEditText("Degrees");
	        this.windsAloftVel = initEditText("Knots");
	        this.windsAloftTemp = initEditText("Farenheit");
	        this.altitude = initEditText("Feet");
	        this.tas = initEditText("Knots");
	        this.tc = initEditText("Degree");
	        this.wca = initEditText("Degrees");
	        this.th = initEditText("Degrees");
	        this.mh = initEditText("Degrees");
	        this.gsEst = initEditText("Knots");
	        this.gsAct = initEditText("Knots");
	        this.eta = initEditTextDisabled("HHMM (Calculated)", true);
	        this.ete = initEditTextDisabled("HHMM (Calculated)", true);
	        this.ate = initEditText("HHMM (hours minutes)");
	        this.gph = initEditText("GPH");
	        this.fuel= initEditText("Gallons");
	        this.fuelRemaining= initEditText("Gallons");
	        this.remainingLegDistance = initEditTextDisabled("Milles (Calculated)", true);
	        this.totalLegDistance = initEditTextDisabled("Milles (Calculated)", true);
	        this.var = initEditText("Degrees");
	        
	        this.timeOff = new TimePicker(context);
	        this.ata = new TimePicker(context);
	       
	        linearLayout.addView(this.altitudeLabel);
	        linearLayout.addView(this.altitude); 
	        linearLayout.addView(this.rpmLabel);
	        linearLayout.addView(this.rpm);
	        linearLayout.addView(this.tasLabel);
	        linearLayout.addView(this.tas);
	        linearLayout.addView(this.varLabel);
	        linearLayout.addView(this.var);
	        linearLayout.addView(this.windsAloftDirLabel);
	        linearLayout.addView(this.windsAloftDir);
	        linearLayout.addView(this.windsAloftVelLabel);
	        linearLayout.addView(this.windsAloftVel);
	        linearLayout.addView(this.windsAloftTempLabel);
	        linearLayout.addView(this.windsAloftTemp);
	        linearLayout.addView(this.gphLabel);
	        linearLayout.addView(this.gph);
	        linearLayout.addView(this.fuelLabel);
	        linearLayout.addView(this.fuel);
	        linearLayout.addView(this.fuelRemainingLabel);
	        linearLayout.addView(this.fuelRemaining);
	        linearLayout.addView(this.tcLabel);
	        linearLayout.addView(this.tc);
	        linearLayout.addView(this.wcaLabel);
	        linearLayout.addView(this.wca);
	        linearLayout.addView(this.thLabel);
	        linearLayout.addView(this.th);
	        linearLayout.addView(this.mhLabel);
	        linearLayout.addView(this.mh);
	        linearLayout.addView(this.remainingLegDistanceLabel);
	        linearLayout.addView(this.remainingLegDistance);
	        linearLayout.addView(this.totalLegDistanceLabel);
	        linearLayout.addView(this.totalLegDistance);
	        linearLayout.addView(this.gsEstLabel);
	        linearLayout.addView(this.gsEst);
	        linearLayout.addView(this.gsActLabel);
	        linearLayout.addView(this.gsAct);
	        linearLayout.addView(this.timeOffLabel);
	        linearLayout.addView(this.timeOff);
	        linearLayout.addView(this.eteLabel);
	        linearLayout.addView(this.ete);
	        linearLayout.addView(this.etaLabel);
	        linearLayout.addView(this.eta);
	        linearLayout.addView(this.ateLabel);
	        linearLayout.addView(this.ate);
	        linearLayout.addView(this.ataLabel);
	        linearLayout.addView(this.ata);
	        
	        scroller.addView(linearLayout);	         
	        return scroller;
	    }
	    
	    private EditText initEditText(String hint)
	    {
	    	Context context = getActivity();
	    	int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		                4, getActivity().getResources().getDisplayMetrics());
	    	EditText edit = new EditText(context);
	        //int windsId = 1;
	        //windsAloftDir.setId(windsId);
	    	edit.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                    1f));
	    	edit.setWidth(100);
	        //winds.setImeOptions(EditorInfo.IME_ACTION_NEXT);
	    	edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	    	edit.setHint(hint);
	    	edit.setPadding(padding, padding, padding, padding);
	    	return edit;
	    	
	    }
	    
	    private EditText initEditTextDisabled(String hint, boolean  disabled)
	    {
	    	EditText edit = initEditText(hint);
	    	if(disabled == true)
	    	{
	    		edit.setKeyListener(null);
	    	}
	    	return edit;
	    
	    }
	    
	    private TextView initTextView(String text)
	    {
	    	Context context = getActivity();
	    	TextView view = new TextView(context);
	        //view.setBackgroundColor(0xFFFF00FF);
	        //tv.setTextColor(0xFF000000);
	        view.setTypeface(null, Typeface.BOLD);
	        view.setText(text);
	        //view.setGravity(Gravity.CENTER_HORIZONTAL);
	        return view;
	    	
	    }
	    
	    private void saveWaypointData()
	    {
	    	double windsAloftDirDouble = editTextToDouble(windsAloftDir); // rad
			double windsAloftVelDouble = editTextToDouble(windsAloftVel); //MPH
			double windsAloftTempDouble = editTextToDouble(windsAloftTemp); //MPH
			double altitudeDouble = editTextToDouble(altitude);
			double tasDouble = editTextToDouble(tas);
			double tcDouble = editTextToDouble(tc);
			double wcaDouble = editTextToDouble(wca);
			double thDouble = editTextToDouble(th);
			double mhDouble = editTextToDouble(mh);
			double remLegDistDouble = editTextToDouble(remainingLegDistance);
			double totalDistDouble = editTextToDouble(totalLegDistance);
			double gsEstDouble = editTextToDouble(gsEst);
			double gsActDouble = editTextToDouble(gsAct);
			double rpmDouble = editTextToDouble(rpm);
			double gphDouble = editTextToDouble(gph); //gallons per hour
			double eteDouble =editTextToDouble(ete); //estimated time en route
			double etaDouble =editTextToDouble(eta);
			double ateDouble =editTextToDouble(ate);
			//TimePicker timeOff; //take off time
			//TimePicker ata; //actual time of arrival
			
			LegDataEntry legs = new LegDataEntry();
			legs.addDataEntry(this.getShownIndex(), altitudeDouble, tasDouble, rpmDouble, windsAloftVelDouble, windsAloftDirDouble, windsAloftTempDouble);
			//CalculationsModel calc = new CalculationsModel(flightModel, legs.getLegDataList());
			//legs = calc.getData();
			
		
 	
	    	
	    }
	    private double editTextToDouble(EditText edit)
	    {
	    	double d = Double.parseDouble(this.windsAloftDir.getText().toString());
	    	return d;
	    }
	    
	}


}

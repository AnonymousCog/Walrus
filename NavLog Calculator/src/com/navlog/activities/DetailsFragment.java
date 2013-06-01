package com.navlog.activities;


import com.navlog.models.LegData;
import com.navlog.models.LegDataEntry;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

public class DetailsFragment extends Fragment {
	
	public interface OnDetailsSetListener{
		public void onDetailsSet(int legIndex, double altitude, double TAS,
				double rpms, double windSpeed, double windDir, double windTemp);
	}
	
	private OnDetailsSetListener callBack;
	
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
	
	
	@Override
	public void onAttach(Activity a)
	{
		super.onAttach(a);
		try
		{
			callBack = (OnDetailsSetListener) a;
		}
		catch (ClassCastException e) 
		{
            throw new ClassCastException(a.toString()
                    + " must implement OnDetailsSetListener");
		}

	}
	
	
	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static DetailsFragment newInstance(int index, LegDataEntry legs) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putSerializable(CalculationsActivity.legKey, legs);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    public LegData getLegData()
    {
    	LegData leg;
    	try
    	{
    		LegDataEntry legs =(LegDataEntry) getArguments().getSerializable(CalculationsActivity.legKey);
	    	leg = legs.getLegDataList().get(getShownIndex());
	    	return leg;
    	}
    	catch(Exception e)
    	{
    		return null;
    	}
    	
    	
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
        
        this.tc = initEditText("Degree", false);
        this.wca = initEditText("Degrees", false);
        this.th = initEditText("Degrees", false);
        this.mh = initEditText("Degrees", false);
        this.gsEst = initEditText("Knots", false);
        this.gsAct = initEditText("Knots", false);
        this.eta = initEditText("HHMM (Calculated)", false);
        this.ete = initEditText("HHMM (Calculated)", false);
        this.ate = initEditText("HHMM (hours minutes)", false);
        this.gph = initEditText("GPH", false);
        this.fuel= initEditText("Gallons", false);
        this.fuelRemaining= initEditText("Gallons");
        this.remainingLegDistance = initEditText("Milles (Calculated)", false);
        this.totalLegDistance = initEditText("Milles (Calculated)", false);
        this.var = initEditText("Degrees");
        
        this.timeOff = new TimePicker(context);
        this.ata = new TimePicker(context);
       
        linearLayout.addView(this.altitudeLabel);
        linearLayout.addView(this.altitude);// 
        linearLayout.addView(this.rpmLabel);
        linearLayout.addView(this.rpm);//
        linearLayout.addView(this.tasLabel);
        linearLayout.addView(this.tas);//
        linearLayout.addView(this.varLabel);
        linearLayout.addView(this.var);
        linearLayout.addView(this.windsAloftDirLabel);
        linearLayout.addView(this.windsAloftDir);//
        linearLayout.addView(this.windsAloftVelLabel);
        linearLayout.addView(this.windsAloftVel);//
        linearLayout.addView(this.windsAloftTempLabel);
        linearLayout.addView(this.windsAloftTemp);//
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
            
        populateFields();
        scroller.addView(linearLayout);	         
        return scroller;
    }
    
    private void populateFields()
    {
    	LegData leg = getLegData();
    	if(leg != null)
    	{
    		this.rpm.setText(Double.toString(leg.getRPM()));
	        this.windsAloftDir.setText(Double.toString(leg.getDIR()));
	        this.windsAloftVel.setText(Double.toString(leg.getSPD()));
	        this.windsAloftTemp.setText(Double.toString(leg.getTMP()));
	        this.altitude.setText(Double.toString(leg.getALT()));
	        this.tas.setText(Double.toString(leg.getTAS()));
    		
    	}
    	

    	
    }
    
    private EditText initEditText(String hint)
    {
    	Context context = getActivity();
    	int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                4, getActivity().getResources().getDisplayMetrics());
    	EditText edit = new EditText(context);
    	edit.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                1f));
    	edit.setWidth(100);
    	edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    	edit.setHint(hint);
    	edit.setPadding(padding, padding, padding, padding);
    	//edit.setText(text)
    	return edit;
    	
    }
    
    private EditText initEditText(String hint, boolean  enabled)
    {
    	EditText edit = initEditText(hint);
    	if(enabled == false)
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
    
	@Override
	public void onStop()
	{
		super.onStop();
		this.saveLegData();
	}
    
    public void saveLegData()
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
		
		int i = this.getShownIndex();
		
		callBack.onDetailsSet(i, altitudeDouble, 
				tasDouble, rpmDouble, windsAloftVelDouble, 
				windsAloftDirDouble, windsAloftTempDouble);
	
	
    	
    }
    private double editTextToDouble(EditText edit)
    {
    	try
    	{
    		String test = edit.getText().toString();
    		double d = Double.parseDouble(test);
        	return d;
    	}
    	catch(Exception e)
    	{
    		return 0;
    	}
    	
    }
}
    

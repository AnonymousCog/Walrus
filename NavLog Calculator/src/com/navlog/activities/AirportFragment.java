package com.navlog.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;



public class AirportFragment extends Fragment {
   
    public static AirportFragment newInstance(String weather, String freq, String runway ) {
    	AirportFragment f = new AirportFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("weather", weather);
        args.putString("freq", freq);
        args.putString("runway", runway);
        f.setArguments(args);

        return f;
    }

    public String getWeather() {
        return getArguments().getString("weather");
    }
    
    public String getFreq() {
        return getArguments().getString("freq");
    }
    public String getRunway()
    {
    	return getArguments().getString("runway");
    }
    
  

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	String weather = getWeather();
    	String freq = getFreq();
    	String run = getRunway();
        ScrollView scroller = new ScrollView(getActivity());
        TextView text = initTextView(weather, freq, run);
        scroller.addView(text);
        return scroller;
    }
    
    public TextView initTextView(String upper, String middle , String lower)
    {
    	TextView text = new TextView(getActivity());
        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
       
        String content = upper + "\n" +middle + "\n" + lower;
        text.setText(content);
        return text;
    	
    }
    
   
    
  
    
    
}
/*Notes:
		
		map reference:
 		size mapa original11836x4141
		size mapa modificado full 10310x3100
		cada crop debe ser de 1031x310
		West_Bounding_Coordinate: -74.979220 long left	
		East_Bounding_Coordinate: -60.626548 long right --
		North_Bounding_Coordinate: 19.659721 lat	up--
		South_Bounding_Coordinate: 14.730666 lat	down
		
		Coordinates given by reference documents do not give optimal results, the docs state lat 14.73 but only shows up to 16(more or less)

		API reference
		mapView.registerGeolocator(North_Bounding_Coordinate, 
								   West_Bounding_Coordinate, --had to massage the number
								   South_Bounding_Coordinate, -- had to massage the number
								   East_Bounding_Coordinate);
								   
		mapView.addMarker(currentLocationMarker, lat,long);
 * 		// add marker requires different view variable for each add
 * 
 * 		To do:
 * 		-needed params: listOfAllDLmaps,mapName, mapwidth/height(croped),tileSize, coordinates,
 * 		-Remove all references of mark2 and replace with a stack of waypoints
 * 		-
 * 
 * 
 * 
 * 
 * 
 */

package com.example.navlog.calculator;


import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;





import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.navlog.calculator.R;
import com.example.navlog.calculator.FlightModel.Waypoint;
import com.qozix.mapview.*;
import com.qozix.mapview.MapView.MapEventListener;


import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

public class MapActivity extends Activity 
{	
	private static final String allWaypointLongitudes = "Waypoint_Longitudes";
	private static final String	allwaypointLatitudes = "Waypoint_Latitudes";
	
	
	private String TAG = "MapActivity";
	private MapView mapView;
	private ImageView currentLocationMarker;
	private FlightModel flightData = new FlightModel();

	
	private MapEventListener myMapListener;
	private OnItemSelectedListener mySpinnerListener;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		/*Action bar Style
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ActionBar actionBar = getActionBar(); 
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#550000ff")));
		*/
		
		
		mapView = new MapView(this);
		//mapView.registerGeolocator(19.659721, -74.979220 , 14.730666, -60.626548);//works but not 100% accurate
		int mapWidth = 10310;
		int mapHeight = 3100;
		mapView.registerGeolocator(19.659721, -73.200000 , 16.00000, -60.626548);//better but numbers are hammered
		String mapPath = "CJ-27-20-South/CJ-27-20-South-%col%_%row%.jpg";
		mapView.addZoomLevel(mapWidth, mapHeight, mapPath, 1031, 310);
		//mapView.addZoomLevel(mapWidth, mapHeight, "tiles/CJ-27-20-South-%col%_%row%.jpg", 1031, 310);
		
	      
        currentLocationMarker = new ImageView(this);
	    currentLocationMarker.setImageResource(R.drawable.ic_current_location);
	   
	    //subscribing listeners at the onResume methos
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();  
        myMapListener = new NLMapListner();
       
        
        FrameLayout frame = new FrameLayout(this);
		setContentView(frame);
		FrameLayout.LayoutParams mapViewLayout = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        frame.addView(mapView, mapViewLayout);
		setTitle("");
		
		
		this.placeDepartureAirport();
		this.placeDestinationAirport();
	
		/*try
		{
			//boolean contains = savedInstanceState.containsKey(allWaypointLongitudes);
			
			if(savedInstanceState != null && savedInstanceState.containsKey(allWaypointLongitudes))
			{
				double[] latitudes = savedInstanceState.getDoubleArray(allwaypointLatitudes);
				double[] longitudes = savedInstanceState.getDoubleArray(allWaypointLongitudes);
				int waypointCount = latitudes.length;
				
				if(waypointCount > 0)
				{
					for(int i=0;i<waypointCount;i++)
					{
						placeWaypointOnMap(latitudes[i], longitudes[i]);
					}
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.getMessage()); 
		}*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		
		String[] maps = {"cj-70", "pr-11", "zz-68","cj-70", "pr-11", "zz-68",
    			"cj-70", "pr-11", "zz-68","cj-70", "pr-11", "zz-68","cj-70", 
    			"pr-11","zz-68","cj-70", "pr-11", "zz-68"}; //This should be replaced by the contents of the MAPS XML FILE
		
        Spinner spinner = (Spinner) menu.findItem(R.id.map_select).getActionView();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
        	(this, android.R.layout.simple_spinner_item, maps);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        mySpinnerListener = new SpinnerListener();
        spinner.setOnItemSelectedListener(mySpinnerListener);// reference to a OnItemSelectedListener, that you can use to perform actions based on user selection
        
	
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle out)
	{
		super.onSaveInstanceState(out);
		double[] latitudes = flightData.getAllWaypointLatitudes();
		double[] longitudes = flightData.getAllWaypointLongitudes();
		out.putDoubleArray(allwaypointLatitudes, latitudes);
		out.putDoubleArray(allWaypointLongitudes, longitudes);
		
		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle in)
	{
		super.onRestoreInstanceState(in);
		double[] latitudes = in.getDoubleArray(allwaypointLatitudes);
		double[] longitudes = in.getDoubleArray(allWaypointLongitudes);
		int waypointCount = latitudes.length;
		
		if(waypointCount > 0)
		{
			for(int i=0;i<waypointCount;i++)
			{
				placeWaypointOnMap(latitudes[i], longitudes[i]);
			}
		}

		
	}
	
	@Override
	protected void onStart() 
	{
	    super.onStart();
	    
	}



	@Override
	protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener); 
	    mapView.addMapEventListener(myMapListener);
	    
	}



	@Override
	public void onStop()
	{
		super.onStop();
		locationManager.removeUpdates(locationListener);
		mapView.removeMapEventListener(myMapListener);
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	        case R.id.waypoint_add_setting:
	        	
	        	
	        	
	        	break;
	        
	        case R.id.waypoint_remove_setting:
	        	removeLastPlacedWaypointOnMap();
	        	
	        	break;
	        	
	        case R.id.action_settings:
	        
	               
	        	break;
	            
	        case R.id.map_select:
	        	
	        	
	        	
	        	
	        	break;
	        	
	        case R.id.center_map_setting:
	        	centerMap();
	        
	        	break;
	        
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
	}
	
	//fix crashing when no current location has been set
	private void centerMap()
	{
		double lat = flightData.getCurrentLocation().getLatitude();
    	double lon = flightData.getCurrentLocation().getLongitude();
    	
    	mapView.moveToAndCenter(lat, lon);
    	mapView.requestRender();
		
	}



	private void removeLastPlacedWaypointOnMap()
	{
		if(flightData.getWaypointCount() > 0)
		{
			Waypoint point = flightData.removeLastWaypoint();
			mapView.removeMarker(point.getMarker());
		}
	
	}
	
	private void placeDepartureAirport()
	{
		Intent i = getIntent();
		double lat = i.getDoubleExtra(NewFlightActivity.departureLatitude, 0);
		double lon = i.getDoubleExtra(NewFlightActivity.departureLongitude, 0);
		
		ImageView marker = new ImageView(this);
		marker.setImageResource(R.drawable.ic_departure_airport);
		flightData.setDepartureLocation(marker, lat, lon);
		mapView.addMarker(marker,lat, lon); //with true value computes addmarker with real pixel values
		
	}
	
	private void placeDestinationAirport()
	{
		Intent i = getIntent();
		double lat = i.getDoubleExtra(NewFlightActivity.destinationLatitude, 0);
		double lon = i.getDoubleExtra(NewFlightActivity.destinationLongitude, 0);
		
		ImageView marker = new ImageView(this);
		marker.setImageResource(R.drawable.ic_destination_airport);
		flightData.setArrivalLocation(marker, lat, lon);
		mapView.addMarker(marker,lat, lon); //with true value computes addmarker with real pixel values
		
	}
	
    private void placeWaypointOnMap(double lat, double lon)
	{
		try
		{
			ImageView marker = new ImageView(this);
			marker.setImageResource(R.drawable.ic_waypoint);
				
			flightData.addWaypoint(marker, lat, lon); 
			mapView.addMarker(marker,lat, lon); //with true value computes addmarker with real pixel values
			
			Toast.makeText(getApplicationContext(), " Lat  : "+ lat + "\n Long: " + lon, Toast.LENGTH_SHORT).show();
		}
		catch(Exception e)
		{
			Log.d(TAG, e.getMessage()); 
		}
		
	}



	public class SpinnerListener implements OnItemSelectedListener
    {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) 
		{
			TextView view = (TextView) arg1;
			Toast.makeText(getApplicationContext(),"Selected Map : " + view.getText().toString(), Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    public class NLMapListner implements MapEventListener
    {

		@Override
		public void onDoubleTap(int arg0, int arg1) 
		{
			double latLong[] = new double[2];
			latLong = mapView.pixelsToLatLng(arg0, arg1);
			placeWaypointOnMap(latLong[0], latLong[1]);//Latitude, Longitude
		}

		@Override
		public void onDrag(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFingerDown(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFingerUp(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFling(int arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFlingComplete() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPinch(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPinchComplete(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPinchStart(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRenderComplete() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRenderStart() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScaleChanged(double arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrollChanged(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTap(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onZoomComplete(double arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onZoomLevelChanged(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onZoomStart(double arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFlingComplete(int x, int y) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    public class MyLocationListener implements LocationListener
    {

		@Override
		public void onLocationChanged(Location location) 
		{
			
			/*String text = "My current location is: " 
			+"\nLatitude = "+ location.getLatitude() 
			+ "\nLogitude = " + location.getLongitude() 
			+ "\nAccuracy = " + location.getAccuracy()
		    + "\nSpeed = " + location.getSpeed();*/
			//Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			// label.setText(text);
			
			try
			{
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				
				mapView.removeMarker(currentLocationMarker);
				mapView.addMarker(currentLocationMarker, latitude, longitude);
				flightData.setCurrentLocation(currentLocationMarker, latitude, longitude);
				//mapView.slideToAndCenter(latitude, longitude);
				mapView.requestRender();
			}
			catch(Exception e)
			{
				Log.d(TAG, e.getMessage()); 
			}
		}
		

		@Override
		public void onProviderDisabled(String provider) 
		{
			Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_SHORT).show();
			
			
		}

		@Override
		public void onProviderEnabled(String provider) 
		{
			//Toast.makeText(getApplicationContext(), "GPS ENABLED", Toast.LENGTH_LONG).show();
			
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
    }

}

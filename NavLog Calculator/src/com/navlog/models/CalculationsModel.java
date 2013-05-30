package com.navlog.models;

import java.text.DecimalFormat;

import java.util.Iterator;
import java.util.List;



/**
* Calculations Model Class. All formulas evaluate using radians.
* There are 2 constructors available. There are also 4 conversion methods for degrees, nautical
* miles and radians values. Finally the getData() public method retuns a list with all values required.
* @author Capstone Group
* @version 1.0
*/
public class CalculationsModel
extends FlightWaypointsModel
    implements java.io.Serializable 
    {
		private static final long serialVersionUID = 1L;
		private List<legData> listData;
		
		/**
	   * Calculations Class Constructor 
	   * @param incFlightModel class with Flight Information. It contains departure, destination and waypoints.
	   * @param _dataEntry The List from LegDataEntry Class. It contains a List with leg by leg data entered by the user.*/
		public CalculationsModel(FlightWaypointsModel incFlightModel, List<legData> _dataEntry)
	    {		
			destination = incFlightModel.getDepartureLocation();
			departure = incFlightModel.getArrivalLocation();
			points = incFlightModel.getPoints();
			listData = _dataEntry;
    	}
		
		
		public CalculationsModel(FlightWaypointsModel incFlightModel)
	    {		
			destination = incFlightModel.getDepartureLocation();
			departure = incFlightModel.getArrivalLocation();
			points = incFlightModel.getPoints();
    	}
		
		/**
		 * Default Constructor
		 */
		public CalculationsModel()
		{			
		}
		
		
		/**
		 * Method to retrieve data with all Calculations
		 * @return
		 */
		public List<legData> getData(List<legData> _dataEntry)
		{
			listData = _dataEntry;
			return getData();
		}
		
		public List<legData> getData()
		{
			if(listData != null)
			{
				// Using the Fligh Model information we calculate the total values of Distance, Time and Fuel Consumption.
				totalDistance = this.getDistance(this.departure, this.destination);
				totalTime = this.getTotalTime();
				totalFuel = this.getTotalFuel();
				
				// We calculate the initial True Curse for later operations.
				double[]tcs = this.calculateTrueCourse();
										
				// Iterate through the Leg Quantity
				int i=0; double tte=0;
				Iterator<com.navlog.models.legData> entries = listData.iterator();		
				while (entries.hasNext()) 
				{			
					legData tmpData = listData.get(i);
					
					//Calculating Leg values
					//WCA
					double wca=this.calculateWCA(tmpData.getDIR(), tmpData.getSPD(), tcs[i] , tmpData.getTAS() , tmpData.getALT());
					listData.get(i).setWCA(wca);
					
					//TH
					double th= this.calculateTH(tcs[i], wca );
					listData.get(i).setTH(th);
					
					//VAR
					double var = this.calculateVariation(this.points.get(i).getAltitude(), this.points.get(i).getLongitude());
					listData.get(i).setVAR(var);
					
					//MH
					double mh = this.calculateMH(th, var);
					
					//LEG DISTANCE
					double legDistance=0;
					
					if(i==0)
						legDistance = this.getDistance(this.departure, this.points.get(i));
									
					else if(this.points.size()-1 !=i)
						legDistance = this.getDistance(this.points.get(i-1), this.points.get(i));
					
					
					else 
						legDistance = this.getDistance(this.points.get(i), this.destination);
					
					listData.get(i).setLEG_DIST(legDistance);
					
					
					//LEG REMAINING DISTANCE
					double legRemDistance = this.getDistance(this.points.get(i), this.destination);
					listData.get(i).setLEG_DIST_REM(legRemDistance);
					
					//ESTIMATED GROUND SPEED
					double gs = this.calculateGS(tmpData.getDIR(), tmpData.getSPD(), tcs[i], tmpData.getTAS());
					listData.get(i).setEST_GS(gs);
					
					//ETE
					double ete = this.calculateTE(legRemDistance, gs);
					listData.get(i).setETE(ete);
					tte =+ete;
								
					//LEG FUEL
					double fuel=0;
					listData.get(i).setLEG_FUEL(fuel);
					
					//FUEL REMAINING
					double fuelRemaining=0;
					listData.get(i).setLEG_FUEL_REM(fuelRemaining);
					
					//NEXT VALUE
						i++;
					
				}
				
				// Add the Total Time Enroute
				totalTE= tte;
				
				// Devuelvo la lista
				return listData;
			}
			else
			{
				return null;
			}
		}
		
		public double totalDistance;
		public double totalTime;
		public double totalFuel;
		public double totalTE;
		
		/**
		 * haversine formula to calculate distance between 2 Waypoints.
		 * @param A Waypoint (From)
		 * @param B Waypoint (To)
		 * @return double value in radians
		 */
		private double getDistance(Waypoint A, Waypoint B)
		{ 
			double R = 6371.0087714; 		// Earch radius in Kilometers
			double dLat = this.degToRadians(B.getLatitude()-B.getLatitude());
			double dLon = this.degToRadians(B.getLongitude()-A.getLongitude());
			double lat1 = this.degToRadians(A.getLatitude());
			double lat2 = this.degToRadians(B.getLatitude());					

			double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
			return (R * c);
		}
		
		/**
		 * Total Time calculation
		 */
		private double getTotalTime()
		{
			double time=0;
			return time;
		}
		
		private double getTotalFuel()
		{
			double fuel=0;
			return fuel;
		}
		
		 /** Calculates True Course
		 * @return (double)True Curse value
		 **/
		private double[] calculateTrueCourse()
		{
			int length = this.points.size() + 1;
			double[]TCs = new double[length];
					
			double lat1, lat2, lon1, lon2;
			
			//Calculamos salida -> Waypoint1
			lat1 = Math.toRadians(this.departure.getLatitude());
			lon1 = Math.toRadians(this.departure.getLongitude());
			lat2 = Math.toRadians(this.points.get(0).getLatitude());
			lon2 = Math.toRadians(this.points.get(0).getLongitude());
			
			if (Math.sin(lon2-lon1)<0)       
				   TCs[0]=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));    
			    else       
				   TCs[0]=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));
			
			//Calculamos Waypoint 1 -> Waypoint N
			for(int i=0; i<this.points.size()-1; i++)
			{				
				lat1 = Math.toRadians(this.points.get(i).getLatitude());
				lon1 = Math.toRadians(this.points.get(i).getLongitude());
				lat2 = Math.toRadians(this.points.get(i+1).getLatitude());
				lon2 = Math.toRadians(this.points.get(i+1).getLongitude());
				
				if (Math.sin(lon2-lon1)<0)       
					   TCs[i+1]=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));    
				    else       
					   TCs[i+1]=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));    
			}
						
			//Calculamos Waypoint N -> Llegada
			lat1 = Math.toRadians(this.points.get(length).getLatitude());
			lon1 = Math.toRadians(this.points.get(length).getLongitude());
			lat2 = Math.toRadians(this.destination.getLatitude());
			lon2 =  Math.toRadians(this.destination.getLongitude());
			if (Math.sin(lon2-lon1)<0)       
				   TCs[length]=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));    
			    else       
				   TCs[length]=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(getDistance(this.departure,this.points.get(0))))/(Math.sin(getDistance(this.departure,this.points.get(0)))*Math.cos(lat1)));
			
			return TCs;
		}
		
		/**
		 * Method to calculate the WCA at a certain LEG.
		 * @param _windDirection Wind Directions.
		 * @param _windVel Wind Velocity
		 * @param _TC True Curse
		 * @param _TAS TRue Airspeed
		 * @param _altitude Altitude
		 * @return double with the Wind Correction Angle
		 */
		private double calculateWCA(double _windDirection, double _windVel, double _TC, double _TAS, double _altitude)
		{
			double wca=0;						
			double m = Math.sin( _windDirection - _TC);
			double xw = m * _windVel;
			wca = xw / (_TAS*_altitude);
			return wca;
		}
		
		/**
		 * Method to calculate the True Heading.
		 * @param _tc True Curse for the leg.
		 * @param _wca Wind Correction angle for that leg.
		 * @return
		 */
		private double calculateTH(double _tc, double _wca)
		{		
			return _tc + _wca;
		}
				
		/**
		 * Method to calculate the variation.
		 * @return double in radians
		 */
		private double calculateVariation(double lat, double lon)
		{			
			double var = -65.6811 + (0.99*lat) + (0.0128899*Math.pow(lat,2)) - 0.0000905928*Math.pow(lat, 3)+ 2.87622*lon - 
				        0.0116268*lat*lon - 0.00000603925*(Math.pow(lat,3))*lon - 0.0389806*(Math.pow(lon, 2)) - 
				        0.0000403488*lat*(Math.pow(lon,2)) + 0.000168556*(Math.pow(lon,3));
			return var;
		}
		
		/**
		 * Method to calculate the Magnetic Heading
		 * @param _th True Heading
		 * @param _var Variation
		 * @return value in radians
		 */
		private double calculateMH(double _th, double _var)
		{
			return _th + _var;			
		}
		
		/**
		 * Method to calculate the Ground Speed 
		 * @param windDir Wind Direction
		 * @param windVel Wind Velocity
		 * @param tc True Curse
		 * @param tas True Airspeed
		 * @return Ground Speed
		 */
		private double calculateGS(double windDir,double windVel, double tc,double tas)
		{
			double l = Math.cos(windDir - tc);
			double m = Math.sin(windDir - tc);
			double k = (windVel / tas)*m;
			k = Math.pow(k,2);
			double gs = tas*(Math.sqrt(1-k))-windVel*l;
			return gs;				
		}
	
		/**
		 * Method to calculate the Time Enroute for a determined distance.
		 * @param distance Distance to be evaluated
		 * @param groundSpeed Ground Speed.
		 * @return
		 */
		private double calculateTE(double distance, double groundSpeed)
		{	
			double time= (distance / groundSpeed) * 60.0;
			DecimalFormat twoDForm = new DecimalFormat("#0.0");
			return (Double.valueOf(twoDForm.format(time)));
		}
				
		/**
		   * Transforma valor Radianes a Millas Nauticas
		   * @param incValue valor en radianes
		   * @return valor en Millas Nauticas
		   */
		private double radiansToNm(double incValue)
		{
			return (incValue*180*60/Math.PI);
		}
		
		/**
		   * Transform values from Radians to Degrees
		   * @param incValue valor en radianes
		   * @return Resultado en grados
		   */
		private double radiansToDeg(double incValue)
		{
			return ((180/Math.PI)*incValue);
		}
		
		/**
		 * Transform a double value to radians.
		 * @param value Value to be converted
		 * @return
		 */
		private double doubleToRadians(double value)
		{
			double valor= value;
			return (Math.toRadians(valor));
		}
		
		private double degToRadians(double value)
		{
			return (value *  Math.PI / 180);
		}
							
   }




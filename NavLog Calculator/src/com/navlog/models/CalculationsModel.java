package com.navlog.models;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.*;
import org.apache.commons.math3.analysis.TrivariateFunction;
import org.apache.commons.math3.analysis.interpolation.TricubicSplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator;

import com.navlog.support.Magfield;


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
		private String flightName;
		private static final long serialVersionUID = 1L;
		private ArrayList<LegData> listData;
		AirplaneProfileModel _profiles;
		
		/**
	   * Calculations Class Constructor 
	   * @param incFlightModel class with Flight Information. It contains departure, destination and waypoints.
	   * @param _dataEntry The List from LegDataEntry Class. It contains a List with leg by leg data entered by the user.*/
		public CalculationsModel(FlightWaypointsModel incFlightModel, ArrayList<LegData> _dataEntry, AirplaneProfileModel a_profiles)
	    {		
			destination = incFlightModel.getDepartureLocation();
			departure = incFlightModel.getArrivalLocation();
			points = incFlightModel.getPoints();
			listData = _dataEntry;
			_profiles = a_profiles;
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
		 * 
		 * @return ArrayList with calculations
		 */
		public ArrayList<LegData> getCalculatedData(ArrayList<LegData> _dataEntry)
		{
			listData = _dataEntry;
			double tte=0;
			// Using the Fligh Model information we calculate the total values of Distance, Time and Fuel Consumption.
			setTotalDistance(calculateTotalDistance());
			setTotalFuel(this.getTotalFuel());
			
			
			
			// We calculate the initial True Curse for later operations.
			double[]tcs = this.calculateTrueCourse();
			
			
			if(listData.size() >1)
			{	
				// Iterate through the Leg Quantity
				int i=0; 
				int size = listData.size();
				while (i <= size-1) 
				{
					LegData tmpData = listData.get(i);
					
					double[] tmp = this.getCruisePerformanceData(tmpData.getALT(), tmpData.getRPM(), tmpData.getTMP());
					listData.get(i).setTAS(tmp[0]);
					listData.get(i).setLEG_FUEL(tmp[1]);
					
					//Refreshing with new data
					tmpData = listData.get(i);
					
					listData.get(i).setTC(tcs[i]);
					//Calculating Leg values
					//WCA
					double wca=this.calculateWCA(tmpData.getDIR(), tmpData.getSPD(), tcs[i] , tmpData.getTAS());
					listData.get(i).setWCA(wca);
					
					//TH
					double th= this.calculateTH(tcs[i], wca );
					listData.get(i).setTH(th);
					
					//VAR
					double var=0;
					
					if(this.points.size() == i)
						var = this.calculateVariation(this.destination.getLatitude(), this.destination.getLongitude());
					else		
						var = this.calculateVariation(this.points.get(i).getLatitude(), this.points.get(i).getLongitude());
					
					listData.get(i).setVAR(var);
					
					//MH
					double mh = this.calculateMH(th, var);
					
					//LEG DISTANCE
					double legDistance;
					
					if(i==0)
						legDistance = this.getDistance(this.departure, this.points.get(i));
					
					else if(this.points.size() == i)
						legDistance = this.getDistance(this.points.get(i-1), this.destination);
									
					else
						legDistance = this.getDistance(this.points.get(i-1), this.points.get(i));
										
					listData.get(i).setLEG_DIST(legDistance);
					
					//LEG REMAINING DISTANCE
					double legRemDistance = 0;
					if(this.points.size() == i)
						legRemDistance = 0; //check
					else
						legRemDistance = this.getDistance(this.points.get(i), this.destination);
					
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
				this.setTotalTE(totalTE);
			}
			else if(listData.size()==1)
			{
				//No waypoints
				//Just 1 leg available
				
				LegData tmpData = listData.get(0);
				
				double[] tmp = this.getCruisePerformanceData(tmpData.getALT(), tmpData.getRPM(), tmpData.getTMP());
				listData.get(0).setTAS(tmp[0]);
				listData.get(0).setLEG_FUEL(tmp[1]);
				
				//Refreshing with new data
				tmpData = listData.get(0);
				
				//WCA
				double wca=this.calculateWCA(tmpData.getDIR(), tmpData.getSPD(), tcs[0] , tmpData.getTAS());
				listData.get(0).setWCA(wca);
				
				//TH
				double th= this.calculateTH(tcs[0], wca );
				listData.get(0).setTH(th);
				
				//VAR
				double var= this.calculateVariation(this.destination.getLatitude(), this.destination.getLongitude());
				listData.get(0).setVAR(var);
				
				//MH
				double mh = this.calculateMH(th, var);
				
				//LEG DISTANCE
				double legDistance = this.getDistance(this.departure, this.destination);
				listData.get(0).setLEG_DIST(legDistance);
				
				//REMAINING DISTANCE
				listData.get(0).setLEG_DIST_REM(legDistance);
				
				//ESTIMATED GROUND SPEED
				double gs = this.calculateGS(tmpData.getDIR(), tmpData.getSPD(), tcs[0], tmpData.getTAS());
				listData.get(0).setEST_GS(gs);
				
				//ETE
				double ete = this.calculateTE(legDistance, gs);
				listData.get(0).setETE(ete);
				tte =+ete;
							
				//LEG FUEL
				double fuel=0;
				fuel = this.calculateLegFuel(tmpData.getLEG_FUEL(), ete);
				listData.get(0).setLEG_FUEL(fuel);
				totalFuel=+fuel;
				
				//FUEL REMAINING
				double fuelRemaining=0;
				listData.get(0).setLEG_FUEL_REM(fuelRemaining);
				
				// Add the Total Time Enroute
				totalTE= tte;
			}
			else
			{
				//some error
			}
			
			return listData;
		}
		
		/**
		 * Method to calculate the Fuel Consumption
		 * @param leg_DIST Leg Distance
		 * @param GPH GPH Consumption for this Leg 
		 * @param ete Estimated Time
		 * @return GPH for Leg
		 */
		private double calculateLegFuel(double GPH, double ete) 
		{
			return (ete/60)*GPH;
		}

		private double totalDistance;
		private double totalFuel;
		private double totalTE;
		
		/**
		* Total distance
		*/
		private double calculateTotalDistance()
		{
			double distance=0;
			if(points.size()>0)
			{
				//Calculate from Departure -> Waypoint 1
				distance = this.getDistance(departure, this.points.get(0));
				
				//Calculate from Waypoint 1 -> Waypoint N if N>1
				int size=this.points.size();
				if(size>1)
				{
					for(int i=0;i<size-1;i++)
					{
						double tmp = this.getDistance(this.points.get(i), this.points.get(i+1));
						distance = distance+tmp;
					}
				}
				
				//Calculate from Waypoint N -> Destination
				distance = distance + this.getDistance(this.points.get(size-1), destination);
			}
			else
			{
				distance = this.getDistance(departure, destination);
				//Calculate from Departure -> Destination
			}
			
			return distance;
		}
		
		/**
		 * haversine formula to calculate distance between 2 Waypoints.
		 * @param A Waypoint (From)
		 * @param B Waypoint (To)
		 * @return double value in Kilometers
		 */
		private double getDistance(Waypoint A, Waypoint B)
		{ 
			double R = 6371.0087714; 		// Earch radius in Kilometers
			double dLat = CalculationsModel.doubleToRadians(B.getLatitude()-A.getLatitude());
			double dLon = CalculationsModel.doubleToRadians(B.getLongitude()-A.getLongitude());
			double lat1 = CalculationsModel.doubleToRadians(A.getLatitude());
			double lat2 = CalculationsModel.doubleToRadians(B.getLatitude());					

			double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
			return (R * c);
		}
							
		 /** Calculates True Course
		 * @return Double array with all True Curse values
		 **/
		private double[] calculateTrueCourse()
		{
			int length = this.points.size();
			double[]TCs = new double[length+1];
					
			
			if(length>0)
			{
				//Hay al menos 1 waypoint
				
				//Calculamos salida -> Waypoint1
				TCs[0] = this.evaluateTC(this.departure, this.points.get(0));
				
				//Calculamos Waypoint 1 -> Waypoint N
				for(int i=0; i<this.points.size()-1; i++)
				{				
					TCs[i+1] = this.evaluateTC(this.points.get(i), this.points.get(i+1));
				}
							
				//Calculamos Waypoint N -> Llegada
				TCs[length]= this.evaluateTC(this.points.lastElement(), this.destination);
			}
			else
			{
				//No hay waypoints
				TCs[0] = this.evaluateTC(this.departure, this.destination);
			}
			return TCs;
		}
		
		/**
		 * Function to evaluate TC for 2 points
		 * @param A From
		 * @param B TO
		 * @return true curse
		 */
		private double evaluateTC(Waypoint A, Waypoint B)
		{
			double tc=0;
			double lat1 = CalculationsModel.doubleToRadians(this.departure.getLatitude());
			double lon1 = CalculationsModel.doubleToRadians(this.departure.getLongitude());
			double lat2 = CalculationsModel.doubleToRadians(this.destination.getLatitude());
			double lon2 = CalculationsModel.doubleToRadians(this.destination.getLongitude());
			
			tc=(Math.atan2(Math.sin(lon1-lon2)*Math.cos(lat2), Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)))%2*Math.PI;
		
			return tc;
			
		}
		/**
		 * Method to calculate the WCA at a certain LEG.
		 * @param _windDirection Wind Directions as degree
		 * @param _windVel Wind Velocity 
		 * @param _TC True Curse in radian
		 * @param _TAS TRue Airspeed 
		 * @return double with the Wind Correction Angle in radians
		 */
		private double calculateWCA(double _windDirection, double _windVel, double _TC, double _TAS)
		{
			double wca=0;						
			double m = Math.sin(CalculationsModel.degToRadians(_windDirection) - _TC);
			double xw = m * _windVel;
			wca = xw / (_TAS);
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
		 * Method to calculate the Magnetic Variation
		 * @param lat latitude
		 * @param lon longitude
		 * @return variation
		 */
		private double calculateVariation(double lat, double lon)
		{	
			lon = lon * -1;
		    double height = 0;
		    int yy = 13;//13;
		    int mm = 6;//7;
		    int dd = 6;//15;
		    int model = 7;
		    double[] field = new double[6];
		    int latsign = +1; // N
		    int lonsign = -1; // W
		    int varsign = -1; // W

		    double variation = Magfield.rad_to_deg(varsign*Magfield.SGMagVar(latsign*CalculationsModel.doubleToRadians(lat),lonsign*CalculationsModel.doubleToRadians(lon),height,
		                Magfield.yymmdd_to_julian_days(yy,mm,dd),model,field));
		    
		    return ((double)(Math.round( variation * 10 ) )/10);
			  
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
			double gs;
			double swc = (windVel/tas)*Math.sin(windDir - CalculationsModel.radiansToDeg(tc));
			if(Math.abs(swc)>1)
				return 0; //Course cannot be flown -- Wind too strong
			else
			{
				double hd = CalculationsModel.radiansToDeg(tc)+Math.asin(swc);
				if(hd<0)
					hd = hd + 2*Math.PI;
				if(hd>2*Math.PI)
					hd= hd-2*Math.PI;
				
				gs = tas*Math.sqrt(1-Math.pow(swc, 2))-windVel*Math.cos(windDir-CalculationsModel.radiansToDeg(tc));
				
				if(gs<0)
					return 0;
			}
			return gs;
		}
	
		/**
		 * Method to calculate the Time Enroute for a determined distance.
		 * @param distance Distance to be evaluated in Km
		 * @param groundSpeed Ground Speed in knots
		 * @return Time in hours
		 */
		private double calculateTE(double distance, double groundSpeed)
		{	
			double time= (CalculationsModel.KmtoKnots(distance) / groundSpeed) * 60.0;
			DecimalFormat twoDForm = new DecimalFormat("#0.0");
			return (Double.valueOf(twoDForm.format(time)));
		}
		
		/**
		 * MEthod to evaluate if the values are in range of the array.
		 * Values outside are not accepted.
		 * @param altitudes All altitudes from the profile
		 * @param rpms All RPM from the profile
		 * @param alt altitude to evaluate
		 * @param rpm Rpm to evaluate
		 * @return 0 if False, 1 if OK
		 */
		public static int isArrayInValidRange(double[] altitudes, double[]rpms, double alt, double rpm)
		{
			int result=1;
			
            ArrayList<Double> newAltitudes = new ArrayList<Double>();
			ArrayList<Double> newRpms = new ArrayList<Double>();
			
			for(int i=0;i<altitudes.length;i++)
			{
				newAltitudes.add(altitudes[i]);
			}

			double minAltitude = Collections.min(newAltitudes);
			double maxAltitude = Collections.max(newAltitudes);
			
			
			if((alt>maxAltitude)||(alt<minAltitude))
				return 0;
			
			else
			{
				for(int i=0;i<newAltitudes.size();i++)
				{
					if(newAltitudes.get(i).equals(alt))
						newRpms.add(rpms[i]);
				}
				double minRpms = Collections.min(newRpms);
				double maxRpms = Collections.max(newRpms);
				
				
				if((rpm>maxRpms)||(rpm<minRpms))
					return 0;
			}
			return result;
		}
		
		/**
		 * Method to evaluate and return True Air Speed and GPH values according to the
		 * entered data in the aircraft profile
		 * @param _altitude Desired Altitude
		 * @param _RPMS Desired RPMS
		 * @param _temperature Temperature
		 * @return double array with the True Airspeed and GPH
		 */
		private double[] getCruisePerformanceData(double _altitude, double _RPMS, double _temperature)
        {
			//Data respuesta donde indice 0 = TAS, indice 1= GPH
			double[] returnData = new double[2];
			
            //Get all profile Altitudes
            double[] altitudes = _profiles.getAllAltitudes();

            //Get all profile RPMS
            double[] rpms = _profiles.getAllRpms();

            //determine temp bracket for TAS
            double[] tempTAS;double[] tempGPH;
            if(_temperature ==20)
            {
            	tempTAS = _profiles.getAllStdKTAS();
            	tempGPH = _profiles.getAllstdGPH();
            }
            else if(_temperature<20)
            {
            	tempTAS = _profiles.getAllBelow20cKTAS();
            	tempGPH = _profiles.getAllBelow20cGPH();
            }
            else 
            {
            	tempTAS = _profiles.getAllAbove20cKTAS();
            	tempGPH = _profiles.getAllAbove20cGPH();
            }
            
            //Los arreglos deben tener el mismo size.
            if((altitudes.length != rpms.length) != (tempTAS.length != tempGPH.length))
            	return null;
            //Debe haber minimo 2 rows de data para efectos de interpolacion.
            if(altitudes.length<3)
            	return null;
            
            //Primero verificamos que la altitud solicitada exista en el profile.
            //Si encontramos valor guardaremos un arreglo con los indices para luego buscar valores basados en estos indices.
            double[] indexes= new double[0];
            for(int i=0;i<altitudes.length;i++)
            {
            	if(altitudes.equals(_altitude))
            	{
            		//Creo arreglo nuevo
            		double[] tmp =new double[indexes.length+1];
            		//Busco size
            		int len = tmp.length;
            		//Agrego el indice en el ultimo row
            		tmp[len-1]= i;
            		//copiamos los valores del arreglo viejo al nuevo
            		for(int j=0;j<indexes.length-1;j++)
            		{
            			tmp[j] = indexes[j];
            		}
            		//copiamos el nuevo arreglo al viejo
            		indexes = tmp;
            	}
            }
            
            //Si el arreglo de indices >0 la altitud tiene valores
            if(indexes.length>0)
            {
            	// 2 Casos aparecen en este escenario
            	
            	// 1. Los RPMS estan definidos y la data esta
            	for(int i=0;i<indexes.length;i++)
            	{
            		if(_RPMS == indexes[i])
            		{
            			returnData[0] = tempTAS[i];
            			returnData[1] = tempGPH[i];
            			break;
            		}
            	}
            	
            	// 2. Los RPMS no estan.
            	double belowRPMvalue=0;
            	int belowRPMindex =-1;
            	double overRPMvalue=999999;
            	int overRPMindex=-1;
        	
            	for(int i=0; i< rpms.length;i++)
            	{
            		if((rpms[i]<_RPMS)&&(rpms[i]>belowRPMvalue))
            		{
            			belowRPMvalue = rpms[i];
            			belowRPMindex=i;
            		}
            		if((rpms[i]>_RPMS)&&(rpms[i]<overRPMvalue))
            		{
            			overRPMvalue = rpms[i];
            			overRPMindex=i;
            		}
            	}

            	// busco los valores de GPH y KTA para los indices conseguidos.
            	double belowGPH = tempGPH[belowRPMindex];
            	double overGPH = tempGPH[overRPMindex];
            	double belowTAS = tempTAS[belowRPMindex];
            	double overTAS = tempTAS[overRPMindex];
            	
            	//Interpolacion Lineal para evaluar el resultado
            	//Calculamos TAS
            	returnData[0] = lineal(_altitude, belowRPMvalue, overRPMvalue, belowTAS, overTAS);
            	//Calculamos GPH
            	returnData[1] = lineal(_altitude, belowRPMvalue, overRPMvalue, belowGPH, overGPH);
            	
            	//End
            	return returnData;
            
            }
            // Si el arreglo es 0 significa que hay que interpolar con data las altitudes y luego lineal.
            else
            {
            	//Primero buscamos el indice y el valor de una altitud menor y una mayor a la solicitada.
            	double belowAltvalue=0;
            	ArrayList<Integer>belowAltindexes = new ArrayList<Integer>();
            	double overAltValue=9999999;
            	ArrayList<Integer>overAltindexes = new ArrayList<Integer>();
            	
            	for(int i=0; i< rpms.length;i++)
            	{
            		if((rpms[i]<_RPMS)&&(rpms[i]>belowAltvalue))
            		{
            			belowAltvalue = rpms[i];
            			belowAltindexes.add(i);
            		}
            		if((rpms[i]>_RPMS)&&(rpms[i]<overAltValue))
            		{
            			overAltValue = rpms[i];
            			overAltindexes.add(i);
            		}
            	}
            	
            	//Ahora creamos un arreglo con todos los RPMS para la altitud below y otro para over.
            	ArrayList<Double> belowRpms = new ArrayList<Double>();            	
            	ArrayList<Double> overRpms = new ArrayList<Double>();
            	//Aprovechamos y creamos un arreglo con todos los GPH para la altitud below y otro para over.
            	ArrayList<Double> belowGPH = new ArrayList<Double>();            	
            	ArrayList<Double> overGPH = new ArrayList<Double>();
            	//Aprovechamos y creamos un arreglo con todos los TAS para la altitud below y otro para over.
            	ArrayList<Double> belowTAS = new ArrayList<Double>();
            	ArrayList<Double> overTAS = new ArrayList<Double>();
            	
            	//Recorremos y asignamos
            	for(int i=0;i<belowAltindexes.size();i++)
            	{
            		belowRpms.add(rpms[i]);
            		belowGPH.add(tempGPH[i]);
            		belowTAS.add(tempTAS[i]);
            	}
            	for(int i=0;i<overAltindexes.size();i++)
            	{
            		overRpms.add(rpms[i]);
            		overGPH.add(tempGPH[i]);
            		overTAS.add(tempTAS[i]);
            	}
            	                    
                // Ahora vamos a calcular una dupla que no este directamente en la tabla, menor y mayor.
            	//GPH para rpm dado
                double x_gph_below = interpolateX(_RPMS, belowRpms, belowGPH);
                double x_gph_over = interpolateX(_RPMS, overRpms, overGPH);
                
                //TAS para rpm dado
                double x_tas_below = interpolateX(_RPMS, belowRpms, belowTAS);
                double x_tas_over = interpolateX(_RPMS, overRpms, overTAS);
                
                // Ya que tenemos los datos, hacemos una
                // interpolacion simple para saber los valores finales.
                //indice 1 = GPH, indice 0 = TAS
                returnData[1] = lineal(_RPMS, belowAltvalue, overAltValue, x_gph_below, x_gph_over);
                returnData[0] = lineal(_RPMS, belowAltvalue, overAltValue, x_tas_below, x_tas_over);
                return returnData;
            }
        }
		
		/**
		 * Method to interpolate
		 * @param x the value to interpolate
		 * @param xd Array with Values Below
		 * @param yd Array with values Over
		 * @return Interpolated value
		 */
        static public double interpolateX(double x, ArrayList<Double> xd, ArrayList<Double> yd)
        {
            double answer = 0;
            for (int i = 0; i <= xd.size() - 1; i++)
            {
            	
                double numerator = 1;
                double denominator = 1;
                
                for (int c = 0; c <= xd.size() - 1; c++)
                {
                    if (c != i)
                    {
                        numerator *= (x - xd.get(c));
                        denominator *= (xd.get(i) - xd.get(c));
                    }
                }
                answer += (double)(yd.get(i) * (numerator / denominator));
            }
            return answer;
        }

        /**
         * Method to do linear interpolation at a single Point
         * @param x Altitude to evaluate
         * @param x0 below RPM
         * @param x1 over RPM
         * @param y0 below Value
         * @param y1 over Value
         * @return interpolated value
         */
        static public double lineal(double x, double x0, double x1, double y0, double y1)
        {
            if ((x1 - x0) == 0)
            {
                return (y0 + y1) / 2;
            }
            return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
        }
        
        /**
		 * Method to convert Kilometers to Nautical Miles
		 * @param incValue value in kilometers
		 * @return value in Nautical miles
		 */
		public static double KmtoNM(double incValue)
		{
			return (incValue*0.539957);
		}
		
		/**
		 * Method to convert Kilometers to Knots
		 * @param incValue value in Kilometers 
		 * @return value in Knots
		 */
		public static double KmtoKnots(double incValue)
		{
			//How many km/h in 1 knots? The answer is 1.852.
			return (incValue/1.852);		
		}
		/**
		   * Transforma valor Radianes a Millas Nauticas
		   * @param incValue valor en radianes
		   * @return valor en Millas Nauticas
		   */
		public static double radiansToNm(double incValue)
		{
			return (incValue*180*60/Math.PI);
		}
		
		/**
		   * Transform values from Radians to Degrees
		   * @param incValue valor en radianes
		   * @return Resultado en grados
		   */
		public static double radiansToDeg(double incValue)
		{
			return ((180/Math.PI)*incValue);
		}
		
		/**
		 * Transform a double value to radians.
		 * @param value Value to be converted
		 * @return
		 */
		public static double doubleToRadians(double value)
		{
			double valor= value;
			return (Math.toRadians(valor));
		}
		
		public static double degToRadians(double value)
		{
			return (value *  Math.PI / 180);
		}

		public String getFlightName() {
			return flightName;
		}


		public void setFlightName(String flightName) {
			this.flightName = flightName;
		}


		public double getTotalFuel() {
			return totalFuel;
		}


		public void setTotalFuel(double totalFuel) {
			this.totalFuel = totalFuel;
		}


		public double getTotalTE() 
		{
			return totalTE;
		}


		public void setTotalTE(double totalTE) 
		{
			this.totalTE = totalTE;
		}


		public void setTotalDistance(double totalDistance) 
		{
			this.totalDistance = totalDistance;
		}
		
		public double getTotalDistance()
		{
			return this.totalDistance;
		}
		public void setLegsData(ArrayList<LegData> aLegs)
		{
			this.listData = aLegs;
		}
		
		public ArrayList<LegData> getLegsData()
		{
			return this.listData;
		}
   }



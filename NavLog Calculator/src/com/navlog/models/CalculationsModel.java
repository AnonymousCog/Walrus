package com.navlog.models;

import java.text.DecimalFormat;
import java.util.Stack;

import com.navlog.models.FlightModel.Waypoint;


/**
* Clase Java para executar calculos basados en coordenadas
* @author Grupo A Capstone
* @version 1.0
*/
public class CalculationsModel
    implements java.io.Serializable
    {
		private static final long serialVersionUID = 1L;
		private Waypoint departure;
		private Waypoint destination;
		private Stack<Waypoint> points=new Stack<Waypoint>();

		/**
	   * Constructor Clase NavCalculations
	   * @param incFlightModel modelo con todos los Waypoints, departure y destination
	   */
		public CalculationsModel(FlightModel incFlightModel)
	    {
			destination = incFlightModel.getDepartureLocation();
			departure = incFlightModel.getArrivalLocation();
			points = incFlightModel.getPoints();
    	}
		
		/**
		   * Transforma valor Radianes a Millas Nauticas
		   * @param incValue valor en radianes
		   * @return valor en Millas Nauticas
		   */
		public double radiansToNm(double incValue)
		{
			return (incValue*180*60/Math.PI);
		}
		
		/**
		   * Transforma valor Radianes a Grados
		   * @param incValue valor en radianes
		   * @return Resultado en grados
		   */
		public double radiansToDeg(double incValue)
		{
			return ((180/Math.PI)*incValue);
		}
		
		/**
		 * Arreglo con las Direcciones de Viento de todos los Legs
		 */
		private double[] WindDirs;
		/**
		 * Implementacion para entrar los datos de Weather
		 */
		private void setWindDirs()
		{
		
		}
		
		/**
		   * Get Wind Direction
		   * @param index indice
		   * @return double con la direccion en radianes
		   */
		public double getWindDir(int index)
		{			
			return Math.toRadians(WindDirs[index]);
		}
		
		/**
		 * Arreglo con las velocidades de Viento de todos los Legs
		 */
		private double[] WindVels;
		
		/**
		 * Implementacion para entrar los datos de Weather
		 */
		private void setWindVels()
		{
			
		}
		
		/**
		   * Wind Velocity
		   * @param index indice 
		   * @return Wind velocity
		   */
		
		public double getWindVel(int index)
		{
			return Math.toRadians(WindVels[index]);
		}
		
		
		private double[] WindTemps;
		/**
		   * Wind Temperature
		   * @return temperatura en Kelvin
		   */		
		public double getWindTemp(int index)
		{
			return (WindTemps[index]);
		}
		
		/**
		   * Calcular True Air Speed 
		   * @param alt altidude del avion para ese waypoint
		   * @return 3 valores del TAS (20 under, standard, 20 over).
		   */
		public double TAS(double alt)
		{			
			double TASs = 0;
			//Implementar funcion de getTAS desde el profile
			return TASs;
		}
		
		private double[]TCs;
		/**
		 * Calculates Initial True Course
		 * @return (double)array with the list of TC's
		   */
		public double[] getTCs()
		{
			int length = this.points.size() + 1;
			TCs = new double[length];
					
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
		
		private double[]WCAs;
		/**
	   * Calcular los Wind Correction Angles
	   * @return (double)array con los resultados en radianes (debes convertir a angular) 
	   */
		public double[] getWCA()
		{
			int length = this.points.size()+1;
			WCAs = new double[length];
					
			double lat1, lat2, lon1, lon2;
			
			//Calculamos salida -> Waypoint1			
			double m = Math.sin(this.getWindDir(0) - TCs[0]);
			double xw = m * this.getWindVel(0);
			WCAs[0] = xw / (this.TAS(this.points.get(0).getAltitude())); //radianes
			
			//Calculamos Waypoint 1 -> Waypoint N
			for(int i=0; i<this.points.size()-1; i++)
			{				
				m = Math.sin(this.getWindDir(i+1) - TCs[i+1]);
				xw = m * this.getWindVel(i+1);
				WCAs[i+1] = xw / (this.TAS(this.points.get(i+1).getAltitude())); //radianes
			}
						
			//Calculamos Waypoint N -> Llegada
			m = Math.sin(this.getWindDir(length) - TCs[TCs.length]);
			xw = m * this.getWindVel(length);
			WCAs[length] = xw / (this.TAS(this.points.get(length).getAltitude())); //radianes
			
			return WCAs;
		}
		
		
		private double[]THs;
		/**
	   * Calcular True Heading
	   * Cuando saco la variacion
	   * @return (double) array con los Valores en radianes
	   */
		public double[] getTHs()
		{
			int size = this.points.size()+1;
			THs = new double[size];
			for(int i=0; i<size ;i++)
			{
				THs[i] = TCs[i] + WCAs[i];
			}			
			return THs;
		}
		
		private double[]Vars;
		/**
	   * Calcular Variation de un waypoint.
	   * Variation es 12 para PR y varia segun longitudes y latitudes pero es bastante general por una zona/mapa
	   * @return (double)array en radianes
	   */
		public double[] Var()
		{
			int size = this.points.size()+1;
			Vars = new double[size];
			for(int i=0; i<this.points.size() ;i++)
			{
				Vars[i] = -65.6811 + (0.99*(this.points.get(i).getLatitude())) + (0.0128899*Math.pow(this.points.get(i).getLatitude(),2)) - 0.0000905928*Math.pow(this.points.get(i).getLatitude(), 3)+ 2.87622*this.points.get(i).getLongitude() - 
				        0.0116268*this.points.get(i).getLatitude()*this.points.get(i).getLongitude() - 0.00000603925*(Math.pow(this.points.get(i).getLatitude(),3))*this.points.get(i).getLongitude() - 0.0389806*(Math.pow(this.points.get(i).getLongitude(), 2)) - 
				        0.0000403488*this.points.get(i).getLatitude()*(Math.pow(this.points.get(i).getLongitude(),2)) + 0.000168556*(Math.pow(this.points.get(i).getLongitude(),3));
			}
			Vars[size]= -65.6811 + (0.99*(this.destination.getLatitude())) + (0.0128899*Math.pow(this.destination.getLatitude(),2)) - 0.0000905928*Math.pow(this.destination.getLatitude(), 3)+ 2.87622*this.destination.getLongitude() - 
			        0.0116268*this.destination.getLatitude()*this.destination.getLongitude() - 0.00000603925*(Math.pow(this.destination.getLatitude(),3))*this.destination.getLongitude() - 0.0389806*(Math.pow(this.destination.getLongitude(), 2)) - 
			        0.0000403488*this.destination.getLatitude()*(Math.pow(this.destination.getLongitude(),2)) + 0.000168556*(Math.pow(this.destination.getLongitude(),3));;
	        return Vars;
		}
		
		private double[]MHs;
		/**
	   * Calcular Magnetic Heading
	   */
		public double[] MH()
		{
			int size = this.points.size()+1;
			MHs = new double[size];
			for(int i=0; i<size;i++)
			{
				MHs[i] = THs[i] + Vars[i];
			}			
			return MHs;		
		}
		
		/**
	   * Calcular Distancia Total
	   * usando haversine formula
	   * @return double con el total de la distancia en radianes
	   */
		public double TotalDist()
		{ 
			double R = 6371.0087714; 																// Earth Radius in KM
			double latDistance = Math.toRadians(this.destination.getLatitude() - this.departure.getLatitude());
		    double lngDistance = Math.toRadians(this.destination.getLongitude() - this.departure.getLongitude());

		    double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +					
		                    (Math.cos(Math.toRadians(this.departure.getLatitude()))) *
		                    (Math.cos(Math.toRadians(this.destination.getLatitude()))) *
		                    (Math.sin(lngDistance / 2)) *
		                    (Math.sin(lngDistance / 2));

		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			return R * c;
		}
		
		private double[] LegDists;
		/**
		   * Calcula distancia entre 2 puntos
		   * @return (double)array con las distancias en todos los LEGS
		   */
		public double[] LegDist()
		{
			int size = this.points.size()+1;
			LegDists = new double[size];
			Waypoint A; Waypoint B;
			
			if(size ==1)
			{
				LegDists[0] = TotalDist();
				return LegDists;
			}
							
			//Leg distance departure -> waypoint 1
			A = this.departure;
			B = this.points.get(0);			
			LegDists[0] = getDistance(A,B);
			
			// Waypoint 1 -> N
			
			for(int i =0;i<this.points.size()-1;i++)
			{
				A = this.points.get(i);
				B = this.points.get(i+1);
				LegDists[i+1] = getDistance(A,B);
			}
			
			// Waypoint N -> Final
			A = this.points.get(points.size());
			B = this.destination;					
			LegDists[size] = getDistance(A,B);
			
			return LegDists;
		}
		
		private double getDistance(Waypoint A, Waypoint B)
		{
			double lat1 = A.getLatitude();
			double lat2 = B.getLatitude();
			double lon1 = A.getLongitude();
			double lon2 = B.getLongitude();	
			return (Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1-lon2)));			
		}
		
		
		/**
		   * Calcula distancia entre punto y destino
		   */
		public double LegRemaining(Waypoint A, Waypoint B)
		{
			double remaining = TotalDist() - getDistance(A,B);
			return remaining;
		}
		
		/**
		 * Ground Speed Array
		 */
		private double[] GSs;
		/**
		   * Calcula Estimated Ground Speed
		   * @return double con valor calculado
		   */
		public double[] EstGS()
		{
			int size= this.points.size()+1;
			GSs = new double[size];
			for(int i=0;i<size;i++)
			{
				double l = Math.cos(this.getWindDir(i) - TCs[i]);
				double m = Math.sin(this.getWindDir(i) - TCs[i]);
				double k = (this.getWindVel(i) / TAS(this.points.get(i).getAltitude()))*m;
				k = Math.pow(k,2);
				double gs = TAS(this.points.get(i).getAltitude())*(Math.sqrt(1-k))-this.getWindVel(i)*l;
				GSs[i] = gs;
			}
			return GSs;
		}
	
		/**
		 * Estimated Time Enroute Array
		 */
		private double[] ETEs;
		/**
		   * Calcula Estimated Time Enroute
		 * @return double value
		   */
		public double[] ETE()
		{	
			double time;
			int size = this.points.size()+1;
			ETEs = new double[size];
			for(int i=0 ; i<this.points.size()-1;i++)
			{
				time= (TotalDist() / GSs[i]) * 60.0;
				DecimalFormat twoDForm = new DecimalFormat("#0.0");
				ETEs[i]= Double.valueOf(twoDForm.format(time));
			}
			return ETEs;			
		}
		
		/**
		   * Calcula Total Time Enroute
		   */
		public void TTE()
		{
			
		}		
		
		/**
		   * Calcular Estimated Time Arrival
		   */
		public void ETA()
		{
			
		}
							
   }




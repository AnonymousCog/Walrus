package com.example.navlog.calculator;

import java.text.DecimalFormat;
import java.util.Stack;

import com.example.navlog.calculator.FlightModel.Waypoint;


/**
* Clase Java para executar calculos basados en coordenadas
* 
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
	   * @param incCoordenadas trae los puntos de salida y llegada.
		 * @return 
	   */
		public CalculationsModel(FlightModel incFlightModel)
	    {
			destination = incFlightModel.getDepartureLocation();
			departure = incFlightModel.getArrivalLocation();
			points = incFlightModel.getPoints();
    	}
		
		/**
		   * Transforma valor Radianes a Millas Nauticas
		   * @param valor en radianes
		   * @return valor en NM
		   */
		public double radiansToNm(double incValue)
		{
			return (incValue*180*60/Math.PI);
		}
		
		/**
		   * Transforma valor Radianes a Grados
		   * @param valor en radianes
		   * @return valor en grados
		   */
		public double radiansToDeg(double incValue)
		{
			return ((180/Math.PI)*incValue);
		}
		
		/**
		   * Calcular Wind Direction
		   */
		public double WindDir()
		{			
			return Math.toRadians((double)0);
		}
		
		/**
		   * Wind Velocity
		   * @return Wind velocity
		   */
		public double WindVel()
		{
			return Math.toRadians((double)0);
		}
		
		/**
		   * Wind Temperature
		   * @return temperatura en Kelvin
		   */
		public double WindTemp()
		{
			return (double)0;
		}
		
		/**
		   * Calcular True Air Speed 
		   * @param alt altidude del avion para ese waypoint
		   * @param power Porciento de power% que el piloto piensa usar
		   * @return valor del TAS.
		   */
		public double TAS(double alt, double power)
		{			
			double tas = 0;
			//Implementar funcion de getTAS desde el profile
			return tas;
		}
		
		/**
	   * Calculates Initial True Course
		 * @return (double)array with the list of TC's
		   */
		public double[] TC()
		{
			int length = this.points.size();
			double[]TC = new double[length+1];
					
			double lat1, lat2, lon1, lon2;
			
			//Calculamos salida -> Waypoint1
			lat1 = this.departure.getLatitude();
			lon1 = this.departure.getLongitude();
			lat2 = this.points.get(0).getLatitude();
			lon2 = this.points.get(0).getLongitude();
			
			if (Math.sin(lon2-lon1)<0)       
				   TC[0]=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(LegDist(this.departure,this.points.get(0))))/(Math.sin(LegDist(this.departure,this.points.get(0)))*Math.cos(lat1)));    
			    else       
				   TC[0]=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(LegDist(this.departure,this.points.get(0))))/(Math.sin(LegDist(this.departure,this.points.get(0)))*Math.cos(lat1)));
			
			//Calculamos Waypoint 1 -> Waypoint N
			for(int i=0; i<length-1; i++)
			{
				Waypoint A = this.points.get(i);
				Waypoint B = this.points.get(i+1);
				
				if (Math.sin(B.getLongitude()-A.getAltitude())<0)       
				   TC[i+1]=Math.acos((Math.sin(B.getLatitude())-Math.sin(A.getLatitude())*Math.cos(LegDist(A,B)))/(Math.sin(LegDist(A,B))*Math.cos(A.getLatitude())));    
			    else       
				   TC[i+1]=2*Math.PI-Math.acos((Math.sin(B.getLatitude())-Math.sin(A.getLatitude())*Math.cos(LegDist(A,B)))/(Math.sin(LegDist(A,B))*Math.cos(A.getLatitude())));    
			}
			
			
			//Calculamos Waypoint N -> Llegada
			lat1 = this.points.get(length).getLatitude();
			lon1 = this.points.get(length).getLongitude();
			lat2 = this.destination.getLatitude();
			lon2 =  this.destination.getLongitude();
			if (Math.sin(lon2-lon1)<0)       
				   TC[length]=Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(LegDist(this.departure,this.points.get(0))))/(Math.sin(LegDist(this.departure,this.points.get(0)))*Math.cos(lat1)));    
			    else       
				   TC[length]=2*Math.PI-Math.acos((Math.sin(lat2)-Math.sin(lat1)*Math.cos(LegDist(this.departure,this.points.get(0))))/(Math.sin(LegDist(this.departure,this.points.get(0)))*Math.cos(lat1)));
			
			return TC;
		}
		
		/**
	   * Calcular Wind Correction Angle
	   * @param A waypoint a evaluar
	   * @param B waypoint a evaluar
	   * @return double en radianes 
	   */
		public double WCA(Waypoint A, Waypoint B)
		{
			double wca=0;
			wca= Math.asin((WindVel()/EstGS(A,B))*Math.sin(MH(A,B)-WindDir())); 
			return wca;
		}
		
		/**
	   * Calcular True Heading
	   * Cuando saco la variacion
	   * @param A waypoint a evaluar
	   * @param B waypoint a evaluar
	   * @return Valor en radianes
	   */
		public double TH(Waypoint A, Waypoint B)
		{
			double th = TC(A,B) + WCA(A,B);
			return th;
		}
		
		/**
	   * Calcular Variation de un waypoint.
	   * Variation es 12 para PR y varia segun longitudes y latitudes pero es bastante general por una zona/mapa
	   * @param Waypont a calcular
	   * @return double en radianes
	   */
		public double Var(Waypoint A)
		{
			double var=  -65.6811 + (0.99*(A.getLatitude())) + (0.0128899*Math.pow(A.getLatitude(),2)) - 0.0000905928*Math.pow(A.getLatitude(), 3)+ 2.87622*A.getLongitude() - 
			        0.0116268*A.getLatitude()*A.getLongitude() - 0.00000603925*(Math.pow(A.getLatitude(),3))*A.getLongitude() - 0.0389806*(Math.pow(A.getLongitude(), 2)) - 
			        0.0000403488*A.getLatitude()*(Math.pow(A.getLongitude(),2)) + 0.000168556*(Math.pow(A.getLongitude(),3));
	        return var;
		}
		
		/**
	   * Calcular Magnetic Heading
	   */
		public double MH(Waypoint A, Waypoint B)
		{
			double mh = TH(A,B) + Var(A);
			return mh;		
		}
				
		/**
	   * Calcular Deviation
	   * Este valor sale del avion en particular. No se puede calcular.
	   * Pueden haber 2 aviones identicos, con valores distintos.
	   */
		public double Dev()
		{
			return plane.getDeviation();
		}
		
		/**
	   * Calcular Course o compass Heading.
	   * Es el numero que buscas en la brujula y apuntas cuando vuelas.
	   */
		public double CH(Waypoint A, Waypoint B)
		{
			double ch = MH(A, B)+ Dev();
			return ch;
		}
		
		/**
	   * Calcular Distancia Total
	   * usando haversine formula
	   * @return double con el total de la distancia en radianes
	   */
		public double TotalDist()
		{ 
			double R = 6371.0087714; 																// Earth Radius in KM
			double latDistance = Math.toRadians(coordPrincipales.getLatitudFinal() - coordPrincipales.getLatitudInicial());
		    double lngDistance = Math.toRadians(coordPrincipales.getLongitudFinal() - coordPrincipales.getLongitudInicial());

		    double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +					
		                    (Math.cos(Math.toRadians(coordPrincipales.getLatitudInicial()))) *
		                    (Math.cos(Math.toRadians(coordPrincipales.getLatitudFinal()))) *
		                    (Math.sin(lngDistance / 2)) *
		                    (Math.sin(lngDistance / 2));

		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			return R * c;
		}
		
		/**
		   * Calcula distancia entre 2 puntos
		   * @return double con distancia entre 2 waypoints en radianes
		   */
		public double LegDist(Waypoint A, Waypoint B)
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
			double remaining = TotalDist() - LegDist(A,B);
			return remaining;
		}
		
		/**
		   * Calcula Estimated Ground Speed
		   * Es el TAS tomando en consideracion la direccion del viento y obviamente la velocidad del mismo.
		   * @return double con valor calculado
		   */
		public double EstGS(Waypoint A, Waypoint B)
		{
			int n = 1;					
			double x = Math.toRadians(WindDir());						// Wind direction in Radians;
			double y = TC(A,B);											// True Course in Radians
			double l = Math.cos(x-y);
			double m = Math.sin(x-y);		
			double k = (WindVel() / TAS())*m;
			k = Math.pow(k,2);
			double gs = TAS()*(Math.sqrt(n-k))-WindVel()*l;
			return gs;
		}
		
		/**
		   * Calcula Actual Ground Speed
		   */
		public void ActGS()
		{
			
		}
		
		/**
		   * Calcula Estimated Time Enroute
		   * @param A Waypoint A a evaluar
		   * @param B Waypoint B a evaluar
		 * @return double value
		   */
		public Double ETE(Waypoint A, Waypoint B)
		{								
			double time;
			time= (TotalDist() / EstGS(A,B)) * 60.0;
			DecimalFormat twoDForm = new DecimalFormat("#0.0");
			return Double.valueOf(twoDForm.format(time));
		}
		
		/**
		   * Calcula Total Time Enroute
		   */
		public void TTE()
		{
			
		}
		
		/**
		   * Calcula Actual Time Enroute
		   */
		public void ATE()
		{
		
		}
		
		
		/**
		   * Calcular Estimated Time Arrival
		   */
		public void ETA()
		{
			
		}
				
		/**
		   * Calcular Actual Time Arrival
		   */
		public void ATA()
		{
			
		}
	
    }




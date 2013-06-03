package com.navlog.models;

	public class LegData implements java.io.Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7675868350973807759L;
		private int LEG;
		private double ALT;
		private double RPM;
		private double TAS;
		private double DIR;
		private double SPD;
		private double TMP;
		private double TC;
		private double WCA;
		private double TH;
		private double VAR;
		private double MH;
		private double LEG_DIST;
		private double LEG_DIST_REM;
		private double EST_GS;
		private double ACT_GS;
		private double ETE;
		private double LEG_FUEL;
		private double LEG_FUEL_REM;
		
		public LegData(){}
		
		public LegData(int legIndex, double altitude, double TAS, double rpms, double windSpeed, double windDir, double windTemp)
		{
			setLEG(legIndex);
			setALT(altitude);
			setTAS(TAS);
			setRPM(rpms);
			setSPD(windSpeed);
			setDIR(windDir);
			setTMP(windTemp);
		}
		
		public String toString()
		{
			String values = "Leg# " + LEG +"\n" 
							+"ALT " + ALT +"\n"
							+ "RPM " + RPM+"\n"
							+ "TAS " + TAS+"\n"
							+ "DIR " + DIR+"\n"
							+ "SPD " + SPD+"\n"
							+ "TMP " + TMP+"\n"
							+ "TC " + TC+"\n"
							+"WCA " + WCA+"\n"
							+ "TH " + TH+"\n"
							+ "VAR " + VAR+"\n"
							+ "MH " + MH+"\n"
							+ "LEGDIST " + LEG_DIST +"\n"
							+ "LEG_DIST_REM " + LEG_DIST_REM +"\n"
							+ "EST_GS " + EST_GS +"\n"
							+ "ACT_GS " + ACT_GS +"\n"
							+ "ETE " + ETE;
			return values;
					
		}
		
		public int getLEG() {
			return LEG;
		}
		public void setLEG(int lEG) {
			LEG = lEG;
		}
		public double getALT() {
			return ALT;
		}
		public void setALT(double aLT) {
			ALT = aLT;
		}
		public double getRPM() {
			return RPM;
		}
		public void setRPM(double rPM) {
			RPM = rPM;
		}
		public double getTAS() {
			return TAS;
		}
		public void setTAS(double tAS) {
			TAS = tAS;
		}
		public double getDIR() {
			return DIR;
		}
		public void setDIR(double dIR) {
			DIR = dIR;
		}
		public double getSPD() {
			return SPD;
		}
		public void setSPD(double sPD) {
			SPD = sPD;
		}
		public double getTMP() {
			return TMP;
		}
		public void setTMP(double tMP) {
			TMP = tMP;
		}
		public double getTC() {
			return TC;
		}
		public void setTC(double tC) {
			TC = tC;
		}
		public double getWCA() {
			return WCA;
		}
		public void setWCA(double wCA) {
			WCA = wCA;
		}
		public double getTH() {
			return TH;
		}
		public void setTH(double tH) {
			TH = tH;
		}
		public double getVAR() {
			return VAR;
		}
		public void setVAR(double vAR) {
			VAR = vAR;
		}
		public double getMH() {
			return MH;
		}
		public void setMH(double mH) {
			MH = mH;
		}
		public double getLEG_DIST() {
			return LEG_DIST;
		}
		public void setLEG_DIST(double lEG_DIST) {
			LEG_DIST = lEG_DIST;
		}
		public double getLEG_DIST_REM() {
			return LEG_DIST_REM;
		}
		public void setLEG_DIST_REM(double lEG_DIST_REM) {
			LEG_DIST_REM = lEG_DIST_REM;
		}
		public double getEST_GS() {
			return EST_GS;
		}
		public void setEST_GS(double eST_GS) {
			EST_GS = eST_GS;
		}
		public double getACT_GS() {
			return ACT_GS;
		}
		public void setACT_GS(double aCT_GS) {
			ACT_GS = aCT_GS;
		}
		public double getETE() {
			return ETE;
		}
		public void setETE(double eTE) {
			ETE = eTE;
		}
		public double getLEG_FUEL() {
			return LEG_FUEL;
		}
		public void setLEG_FUEL(double lEG_FUEL) {
			LEG_FUEL = lEG_FUEL;
		}
		public double getLEG_FUEL_REM() {
			return LEG_FUEL_REM;
		}
		public void setLEG_FUEL_REM(double lEG_FUEL_REM) {
			LEG_FUEL_REM = lEG_FUEL_REM;
		}

}


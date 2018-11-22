package model;

import java.util.ArrayList;

public class BusRideActivity implements Activity{

	public String lineName;
	public String enterStationName;
	public String exitStationName;
	public int numberOfStations;
	public String timeLength;
	public ArrayList<String> oneHourTimeTable;
	
	public String toString() {
		String retVal = "";
		
		retVal += lineName+"\n";
		retVal += "Enter station: " + enterStationName + "\n";
		retVal += "Exit station: " + exitStationName + "\n";
		
		return retVal;
	}
}

package model;

public class WalkingActivity implements Activity {

	public CoordinatesGPS startX;
	public CoordinatesGPS endX;
	public String timeLength;
	
	public WalkingActivity() {
		
	}
	
	public String toString() {
		String retVal = "";
		
		retVal += "Walking activity:\n";
		retVal += "Start Coord: " + startX.toString() + "\n";
		retVal += "End Coord: " + endX.toString() + "\n";
		retVal += "Time: " + timeLength + "\n";
		
		return retVal;
	}
}

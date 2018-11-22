package model;

public class CoordinatesGPS {
	
	private double latitude;
	private double longitude;
	
	public CoordinatesGPS() {
		
	}
	
	public CoordinatesGPS(double lat, double lon) {
		setLatitude(lat);
		setLongitude(lon);
	}	

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String toString() {
		return "Lat: " + getLatitude() + "; Lon: " + getLongitude();
	}
}

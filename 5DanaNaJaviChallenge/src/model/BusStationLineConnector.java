package model;

public class BusStationLineConnector {

	private String lineName;
	private int indexOfPointBefore;
	private int indexOfPointAfter;
	private double distanceFromLastStation;
	private double distanceFromNextStation;
	
	public BusStationLineConnector() {
		
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public int getIndexOfPointBefore() {
		return indexOfPointBefore;
	}

	public void setIndexOfPointBefore(int indexOfPointBefore) {
		this.indexOfPointBefore = indexOfPointBefore;
	}

	public int getIndexOfPointAfter() {
		return indexOfPointAfter;
	}

	public void setIndexOfPointAfter(int indexOfPointAfter) {
		this.indexOfPointAfter = indexOfPointAfter;
	}

	public double getDistanceFromLastStation() {
		return distanceFromLastStation;
	}

	public void setDistanceFromLastStation(double distanceFromLastStation) {
		this.distanceFromLastStation = distanceFromLastStation;
	}

	public double getDistanceFromNextStation() {
		return distanceFromNextStation;
	}

	public void setDistanceFromNextStation(double distanceFromNextStation) {
		this.distanceFromNextStation = distanceFromNextStation;
	}
}

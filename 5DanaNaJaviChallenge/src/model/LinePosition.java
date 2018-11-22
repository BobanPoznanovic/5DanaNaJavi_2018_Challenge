package model;

public class LinePosition {
	
	private CoordinatesGPS lineCoordinate;
	private double distanceFromLastCoordinateLinePoint;
	
	public LinePosition() {
		
	}

	public CoordinatesGPS getLineCoordinate() {
		return lineCoordinate;
	}

	public void setLineCoordinate(CoordinatesGPS lineCoordinate) {
		this.lineCoordinate = lineCoordinate;
	}

	public double getDistanceFromLastCoordinateLinePoint() {
		return distanceFromLastCoordinateLinePoint;
	}

	public void setDistanceFromLastCoordinateLinePoint(double distance) {
		this.distanceFromLastCoordinateLinePoint = distance;
	}
}

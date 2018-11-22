package model;

import java.util.ArrayList;

public class BusStation {

	private String name;
	private CoordinatesGPS stationCoordinates;
	private ArrayList<String> lineStops;
	private ArrayList<BusStationLineConnector> stationConnector;
	
	
	
	public BusStation() {
		setLineStops(new ArrayList<>());
		setStationCoordinates(new CoordinatesGPS());
		setStationConnector(new ArrayList<>());
	}
	
	public BusStation(String name) {
		setName(name);
		setLineStops(new ArrayList<>());
		setStationCoordinates(new CoordinatesGPS());
		setStationConnector(new ArrayList<>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CoordinatesGPS getStationCoordinates() {
		return stationCoordinates;
	}

	public void setStationCoordinates(CoordinatesGPS stationCoordinates) {
		this.stationCoordinates = stationCoordinates;
	}

	public ArrayList<String> getLineStops() {
		return lineStops;
	}

	public void setLineStops(ArrayList<String> lineStops) {
		this.lineStops = lineStops;
	}

	public ArrayList<BusStationLineConnector> getStationConnector() {
		return stationConnector;
	}

	public void setStationConnector(ArrayList<BusStationLineConnector> stationConnector) {
		this.stationConnector = stationConnector;
	}

	
}

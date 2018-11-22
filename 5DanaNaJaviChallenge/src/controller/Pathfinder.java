package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import model.BusRideActivity;
import model.BusStation;
import model.BusStationLineConnector;
import model.CoordinatesGPS;
import model.LinePosition;
import model.Route;
import model.WalkingActivity;
import repository.DataPool;

public class Pathfinder {
	
	public double walkingSpeed = 1.11;
	public double busSpeed = 11.11;
	
	public BusStationNetworkCreator bsnc;
	
	public Pathfinder() {
		this.bsnc = new BusStationNetworkCreator();
	}

	public void checkForRoute(CoordinatesGPS startX, CoordinatesGPS endX, String time) {
		
		ArrayList<Route> routes = new ArrayList<>();
		
		Route justWalking = new Route();
		WalkingActivity wa = checkWalkinOption(startX, endX);	
		justWalking.getActivities().add(wa);
		routes.add(justWalking);
		
		String closestStartStation = findClosestBusStation(startX);
		String closestEndStation = findClosestBusStation(endX); 
		
		HashMap<String, String> linesOnStartStation = listOfLinesAvailableOnStation(
				closestStartStation);
		HashMap<String, String> linesOnEndStation = listOfLinesAvailableOnStation(
				closestEndStation); 
		
		//Result has name of the lines
		ArrayList<String> result = directBusLineConnections(
				linesOnStartStation, 
				linesOnEndStation);
		
		for(String lineName : result) {
			
			//Pre-ride walking
			WalkingActivity preRide = checkWalkinOption(startX, findStationCoordinatesByName(closestStartStation));
			
			BusRideActivity temp = busRideCalculations(lineName, closestStartStation, closestEndStation);
			
			//Post-ride walking
			WalkingActivity postRide = checkWalkinOption(findStationCoordinatesByName(closestEndStation), endX);
			
			Route tempRoute = new Route();
			tempRoute.getActivities().add(preRide);
			tempRoute.getActivities().add(temp);
			tempRoute.getActivities().add(postRide);
			
			routes.add(tempRoute);
			
		}
		
		BusStation startStation = DataPool.getInstance().getBusStations().get(closestStartStation);
		BusStation endStation = DataPool.getInstance().getBusStations().get(closestEndStation);
		
		double distanceFromStartPointToStartStation = 0;
		double distanceFromEndStationToEndPoint = 0;
		
		distanceFromStartPointToStartStation = bsnc.calculateGPSDistance(startStation.getStationCoordinates(), startX);
		distanceFromEndStationToEndPoint = bsnc.calculateGPSDistance(endX, endStation.getStationCoordinates());
		
		//v = s/t; t = s/v
		
		double timeFromStartPointToStartStation = 0;
		double timeFromEndStationToEndPoint = 0;
		
		timeFromStartPointToStartStation = distanceFromStartPointToStartStation/walkingSpeed;
		timeFromEndStationToEndPoint = distanceFromEndStationToEndPoint/walkingSpeed;
		
		
		for(int i = 0; i < routes.size(); i++) {
			
			for(int j = 0; j < routes.get(i).getActivities().size(); j++) {
			
					routes.get(i).getActivities().get(j).toString();
			}
		}
	}
	
	public BusRideActivity busRideCalculations(String lineName, String startStation, String endStation) {
		BusRideActivity bra = new BusRideActivity();
		
		bra.lineName = lineName;
		bra.enterStationName = startStation;
		bra.exitStationName = endStation;
		
		
		
		return bra;
	}
	
	public WalkingActivity checkWalkinOption(CoordinatesGPS startX, CoordinatesGPS endX) {
		WalkingActivity wa = new WalkingActivity();
		
		double walkingDistance = bsnc.calculateGPSDistance(startX, endX);
		
		double walkingTime = walkingDistance/walkingSpeed;
		
		wa.startX = startX;
		wa.endX = endX;
		
		wa.timeLength = doubleToTime(walkingTime);
		
		return wa;
	}
	
	public String doubleToTime(double time) {
		String retVal = "";
		
		double divide = time/60;
		divide = Math.ceil(divide);
		double hours = 0.0;
		
		int justMinutes = (int)Math.round(divide);
		
		if(divide > 60.0) {
			hours = divide/60.0;
			double newHours = Math.floor(hours);
			int finalHours = (int)newHours;
			double doubleMinutes = hours - newHours;
			doubleMinutes = Math.round(doubleMinutes * 100);
			double minutes = doubleMinutes / 1.66;
			int finalMinutes = (int)Math.round(minutes);
			
			if(finalHours < 10) {
				retVal = "0"+finalHours+":"+finalMinutes;
			}
			else {
				retVal = ""+finalHours+":"+finalMinutes;
			}
			
		}
		else {
			if(justMinutes < 10) {
				retVal = "00:"+"0"+justMinutes;
			}
			else {
				retVal = "00:"+justMinutes;
			}
			
		}
		
		return retVal;
	}
	
	public ArrayList<String> directBusLineConnections(
			HashMap<String, String> startStation,
			HashMap<String, String> endStation) {
		
		ArrayList<String> retVal = new ArrayList<>();
		
		Set<Entry<String, String>> set = startStation.entrySet();
		
		for(Entry<String, String> entry : set) {
			if(endStation.containsKey(entry.getKey())) {
				retVal.add(endStation.get(entry.getKey()));
			}
		}
		
		return retVal;
	}
	
	public HashMap<String, String> listOfLinesAvailableOnStation(String stationName) {
		HashMap<String, String> retVal = new HashMap<>();
	 
		BusStation station = DataPool.getInstance().getBusStations().get(stationName);
		
		ArrayList<BusStationLineConnector> connectors = station.getStationConnector();
		for(BusStationLineConnector connector : connectors) {
			retVal.put(connector.getLineName(), connector.getLineName());
		}
		
		
	 	return retVal;
	}
	
	public String findClosestBusStation(CoordinatesGPS coordinates) {
		
		String retVal = "";
		
		HashMap<String, BusStation> busStations = DataPool.getInstance().getBusStations();
		Set<Entry<String, BusStation>> setBusStations = busStations.entrySet();
		ArrayList<LinePosition> distances = new ArrayList<>();
		
		for(Entry<String, BusStation> entry : setBusStations) {
			BusStation station = entry.getValue();
			CoordinatesGPS stationGPS = station.getStationCoordinates();
			BusStationNetworkCreator bsnc = new BusStationNetworkCreator();
			
			LinePosition temp = new LinePosition();
			temp.setLineCoordinate(stationGPS);
			
			
			double distance = bsnc.calculateGPSDistance(stationGPS, coordinates);
			temp.setDistanceFromLastCoordinateLinePoint(distance);
			
			distances.add(temp);
		}
		
		ArrayList<LinePosition> sortedArray = sortArrayOfLinePositions(distances);
		
		retVal = findStationByGPS(sortedArray.get(0).getLineCoordinate());		
		
		return retVal;
	}
	
	public ArrayList<LinePosition> sortArrayOfLinePositions(ArrayList<LinePosition> list) {
		ArrayList<LinePosition> retVal = new ArrayList<>();
		ArrayList<LinePosition> temp = new ArrayList<>();
		
		while(retVal.size() != list.size() && list.size() > 0) {
			
			int index = findMinimumDistance(list);
			retVal.add(list.get(index));
			
			temp = new ArrayList<>();
			
			list.remove(index);
			temp = list;
			list = new ArrayList<>();
			list = temp;
			
		}
		
		return retVal;
	}
	
	public int findMinimumDistance(ArrayList<LinePosition> list) {
		int retVal = 0;
		
		LinePosition min = list.get(0);
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getDistanceFromLastCoordinateLinePoint() < min.getDistanceFromLastCoordinateLinePoint()) {
				retVal = i;
				min = list.get(i);
			}
		}
		
		return retVal;
	}
	
	public String findStationByGPS(CoordinatesGPS coordinates) {
		
		String retVal = "";
		
		HashMap<String, BusStation> busStations = DataPool.getInstance().getBusStations();
		Set<Entry<String, BusStation>> setBusStations = busStations.entrySet();
		
		for(Entry<String, BusStation> entry : setBusStations) {
			if(areCoordinatesTheSame(entry.getValue().getStationCoordinates(), coordinates)) {
				retVal = entry.getKey();
			}
		}
		
		return retVal;
	}
	
	public CoordinatesGPS findStationCoordinatesByName(String stationName) {
		CoordinatesGPS retVal = new CoordinatesGPS();
		
		retVal = DataPool.getInstance().getBusStations().get(stationName).getStationCoordinates();
		
		return retVal;
	}
	
	public boolean areCoordinatesTheSame(CoordinatesGPS coord1, CoordinatesGPS coord2) {
		
		boolean retVal = false;
		
		if(coord1.getLatitude() == coord2.getLatitude() && coord1.getLongitude() == coord2.getLongitude()) {
			retVal = true;
		}
		
		return retVal;
	}
}

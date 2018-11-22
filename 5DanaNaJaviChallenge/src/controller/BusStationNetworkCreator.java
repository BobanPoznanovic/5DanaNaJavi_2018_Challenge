package controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import model.BusLine;
import model.BusStation;
import model.BusStationLineConnector;
import model.CoordinatesGPS;
import model.LinePosition;
import model.StationToLinePointDistance;
import repository.DataPool;

public class BusStationNetworkCreator {
	
	
	public void orderStationInLine() {
		
		HashMap<String, BusLine> busLines = DataPool.getInstance().getBusLines();
		Set<Entry<String, BusLine>> lineSet = busLines.entrySet();
		HashMap<String, BusStation> busStations = DataPool.getInstance().getBusStations();
		Set<Entry<String, BusStation>> stationsSet = busStations.entrySet();
		
		ArrayList<String> stationNames = new ArrayList<>();
		
		for(Entry<String, BusLine> line : lineSet) {
			String lineName = line.getKey();
			
			for(Entry<String, BusStation> station : stationsSet ) {
				ArrayList<BusStationLineConnector> connectors = station.getValue().getStationConnector();
				
				for(int i = 0; i < connectors.size(); i++) {
					if(connectors.get(i).getLineName().equals(lineName)) {
						stationNames.add(station.getValue().getName());
					}
				}
			}
			
			DataPool.getInstance().getOrderOfStationsForLine().put(lineName, stationNames);
			stationNames = new ArrayList<>();
		}
	}

	public void connectLinesAndStations() {
		
		HashMap<String, BusStation> busStations = DataPool.getInstance().getBusStations();
		Set<Entry<String, BusStation>> s = busStations.entrySet();
		
		for(Entry<String, BusStation> e : s) {
			String busStationCodeName = e.getValue().getName();
			ArrayList<String> lineList = e.getValue().getLineStops();
			for(String lineName : lineList) {
				ArrayList<StationToLinePointDistance> result = addBusStationConnectors(busStationCodeName, lineName);
				
				//Find shortest distance between bus station and line point
				//Compare to distances around it
				//Determine which index point to take and save in object
				
				StationToLinePointDistance shortestDistance = result.get(0);
				
				for(StationToLinePointDistance data : result) {
					if(data.getDistance() < shortestDistance.getDistance()) {
						shortestDistance = data;
					}
				}
				
				if(shortestDistance.getIndexOfPoint() > 0 && shortestDistance.getIndexOfPoint() < result.size()-1) {
					StationToLinePointDistance data1 = result.get(shortestDistance.getIndexOfPoint()-1);
					StationToLinePointDistance data2 = result.get(shortestDistance.getIndexOfPoint()+1);
					
					if(data1.getDistance() < data2.getDistance()) {
						//data1 index must be save as BeforePoint
						//shortestDistance index must be save as AfterPoint
						BusStationLineConnector newConnector = new BusStationLineConnector();
						newConnector.setLineName(lineName);
						newConnector.setIndexOfPointBefore(data1.getIndexOfPoint());
						newConnector.setIndexOfPointAfter(shortestDistance.getIndexOfPoint());
						
						DataPool.getInstance().getBusStations().get(busStationCodeName).getStationConnector().add(newConnector);
					}
					else {
						//shortestDistance index must be save as BeforePoint
						//data2 index must be save as AfterPoint
						BusStationLineConnector newConnector = new BusStationLineConnector();
						newConnector.setLineName(lineName);
						newConnector.setIndexOfPointBefore(shortestDistance.getIndexOfPoint());
						newConnector.setIndexOfPointAfter(data2.getIndexOfPoint());
						
						DataPool.getInstance().getBusStations().get(busStationCodeName).getStationConnector().add(newConnector);
					}
				}
				else if (shortestDistance.getIndexOfPoint() == 0) {
										
					BusStationLineConnector newConnector = new BusStationLineConnector();
					newConnector.setLineName(lineName);
					newConnector.setIndexOfPointBefore(0);
					newConnector.setIndexOfPointAfter(1);
					
					DataPool.getInstance().getBusStations().get(busStationCodeName).getStationConnector().add(newConnector);
				}
				else if (shortestDistance.getIndexOfPoint() == result.size()-1) {
										
					BusStationLineConnector newConnector = new BusStationLineConnector();
					newConnector.setLineName(lineName);
					newConnector.setIndexOfPointBefore(result.size()-2);
					newConnector.setIndexOfPointBefore(result.size()-1);
					
					DataPool.getInstance().getBusStations().get(busStationCodeName).getStationConnector().add(newConnector);
				}
								
			}
		}
	}
	
	
	public ArrayList<StationToLinePointDistance> addBusStationConnectors(
			String busStationCodeName, 
			String lineName) {
		
		ArrayList<StationToLinePointDistance> listOfDistances = new ArrayList<>();
		
		BusStation busStation = DataPool.getInstance().getBusStations().get(busStationCodeName);
		BusLine busLine = DataPool.getInstance().getBusLines().get(lineName);
		
		CoordinatesGPS busStationGPSCoordinates = busStation.getStationCoordinates();
		
		ArrayList<LinePosition> listOfLinePositions = busLine.getLinePositions();
		ArrayList<CoordinatesGPS> listOfLinePoints = new ArrayList<>();
		
		for(int i = 0; i < listOfLinePositions.size(); i++) {
			listOfLinePoints.add(listOfLinePositions.get(i).getLineCoordinate());
		}	
		
		int i = 0;
		for(CoordinatesGPS linePointGPS : listOfLinePoints) {
						
			StationToLinePointDistance result = calculateDistanceBetweenStationAndLinePoint(
					busStationGPSCoordinates, 
					linePointGPS, 
					i);
			i++;
			
			listOfDistances.add(result);
		}
		
		
		return listOfDistances;		
	}
	
	public int checkForMinimalDistanceFromTheStation(
			StationToLinePointDistance data1,
			StationToLinePointDistance data2,
			StationToLinePointDistance data3) {
		
		
		if(data2.getDistance() < data1.getDistance() && data2.getDistance() < data3.getDistance()) {
			return data2.getIndexOfPoint();
		}
		if(data1.getDistance() < data2.getDistance() && data2.getDistance() < data3.getDistance()) {
			return data1.getIndexOfPoint();
		}
		//If third distance point is the closest continue with search maybe there is closer one
		
		return -1;
	}
	
	public StationToLinePointDistance calculateDistanceBetweenStationAndLinePoint(
			CoordinatesGPS stationPosition, 
			CoordinatesGPS linePointPosition,
			int linePointIndex) {
		
		StationToLinePointDistance indexAndDistance = new StationToLinePointDistance();
		
		double distance = calculateGPSDistance(stationPosition, linePointPosition);
		
		
		indexAndDistance.setDistance(distance);
		indexAndDistance.setIndexOfPoint(linePointIndex);
		
		return indexAndDistance;
		
	}
	
	public double calculateGPSDistance(
			CoordinatesGPS stationPosition, 
			CoordinatesGPS linePointPosition) {
		
		double distance = 0;
		
		double stationLat = stationPosition.getLatitude();
		double stationLon = stationPosition.getLongitude();
		double linePointLat = linePointPosition.getLatitude();
		double linePointLon = linePointPosition.getLongitude();
		
		int earthRadius = 6371000;
		
		double phi1 = Math.toRadians(stationLat);
		double phi2 = Math.toRadians(linePointLat);
		double deltaPhi = Math.toRadians((linePointLat - stationLat));
		double deltaAlpha = Math.toRadians((linePointLon - stationLon));
		
		double a = Math.pow(Math.sin(deltaPhi/2), 2) + Math.cos(phi1)*Math.cos(phi2)*Math.pow(Math.sin(deltaAlpha/2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		distance = earthRadius * c;
		
		
		return distance;
	}
	
	
	public void calculateDistanceBetweenLinePoints() {
		HashMap<String, BusLine> busLines = DataPool.getInstance().getBusLines();
		Set<Entry<String, BusLine>> setOfBusLines = busLines.entrySet();
		
		for(Entry<String, BusLine> entry : setOfBusLines) {
			BusLine busLine = entry.getValue();
			
			ArrayList<LinePosition> linePositions = busLine.getLinePositions();
			
			for(int i = 0; i < linePositions.size(); i++) {
				if(i == 0) {
					linePositions.get(i).setDistanceFromLastCoordinateLinePoint(0);
				}
				else {
					CoordinatesGPS x0 = linePositions.get(i-1).getLineCoordinate();
					CoordinatesGPS x1 = linePositions.get(i).getLineCoordinate();
					
					double distance = calculateGPSDistance(x0, x1);
					
					linePositions.get(i).setDistanceFromLastCoordinateLinePoint(distance);
				}
			}
		}
	}
	
	public void calculateDistancesFromTheLastStation() {
			
		HashMap<String, ArrayList<String>> orderOfStationsForLine = DataPool.getInstance().getOrderOfStationsForLine();
		Set<Entry<String, ArrayList<String>>> set = orderOfStationsForLine.entrySet();
		
		for(Entry<String, ArrayList<String>> entry : set ) {
			String lineName = entry.getKey();
			ArrayList<String> stationNames = entry.getValue();
			
			for(int i = 0; i < stationNames.size(); i++) {
				String stationName = stationNames.get(i);
				if(i == 0) {
					DataPool.getInstance().getBusStations().get(stationName)
					.getStationConnector().get(
							DataPool.getInstance().getConnectorIndex(lineName, stationName))
					.setDistanceFromLastStation(
							totalDistanceToStation(lineName, stationName));
				}
				else {
					double distance1 = totalDistanceToStation(lineName, stationNames.get(i-1));
					double distance2 = totalDistanceToStation(lineName, stationName);
					
					double finalDistance = distance2 - distance1;
					
					DataPool.getInstance().getBusStations().get(stationName)
					.getStationConnector().get(
							DataPool.getInstance().getConnectorIndex(lineName, stationName))
					.setDistanceFromLastStation(finalDistance);
				}
			}
			
		}
		
	}

	public double totalDistanceToStation(String lineName, String stationName) {
		
		double finalDistance = 0;
		
		int beforeStationPointIndex = DataPool.getInstance().getOrderIndexOfStation(lineName, stationName);
		
		BusLine busLine = DataPool.getInstance().getBusLines().get(lineName);
		BusStation busStation = DataPool.getInstance().getBusStations().get(stationName);
		ArrayList<LinePosition> positions = busLine.getLinePositions();
		
		for(int i = 0; i <= beforeStationPointIndex; i++) {
			finalDistance += positions.get(i).getDistanceFromLastCoordinateLinePoint();
		}
		
		CoordinatesGPS busStationGPS = busStation.getStationCoordinates();
		CoordinatesGPS linePointGPS = positions.get(beforeStationPointIndex).getLineCoordinate();
		
		finalDistance += calculateGPSDistance(busStationGPS, linePointGPS);
		
		return finalDistance;
	}
}

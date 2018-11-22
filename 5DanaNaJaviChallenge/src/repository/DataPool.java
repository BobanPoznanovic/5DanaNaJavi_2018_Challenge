package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import model.BusLine;
import model.BusStation;
import model.BusStationLineConnector;
import model.LinePosition;
import model.SortingStationsData;

public class DataPool {
	
	private static DataPool instance = null;
	
	private HashMap<String, BusLine> busLines;
	private HashMap<String, BusStation> busStations;
	private HashMap<String, ArrayList<String>> orderOfStationsForLine;
	
	private DataPool() {
		this.busLines = new HashMap<>();
		this.busStations = new HashMap<>();
		this.setOrderOfStationsForLine(new HashMap<>());
	}
	
	public static DataPool getInstance() {
		if(instance == null) {
			instance =  new DataPool();
		}
		return instance;
	}

	public HashMap<String, BusLine> getBusLines() {
		return busLines;
	}

	public HashMap<String, BusStation> getBusStations() {
		return busStations;
	}

	public HashMap<String, ArrayList<String>> getOrderOfStationsForLine() {
		return orderOfStationsForLine;
	}

	public void setOrderOfStationsForLine(HashMap<String, ArrayList<String>> orderOfStationsForLine) {
		this.orderOfStationsForLine = orderOfStationsForLine;
	}
	
	public void sortStationsInTheLine() {
		Set<Entry<String, ArrayList<String>>> set = this.orderOfStationsForLine.entrySet();
		
		for(Entry<String, ArrayList<String>> data : set) {
			
			ArrayList<String> nameOfTheStations = data.getValue();
			ArrayList<BusStation> busStations = new ArrayList<>();
			
			HashMap<String, BusStation> stationsInHashMap = this.busStations;
			
			
			//Collecting bus stations so we can sort them according to the closest line point
			for(String stationName : nameOfTheStations) {
				BusStation station = stationsInHashMap.get(stationName);
				busStations.add(station);
			}
			
			ArrayList<SortingStationsData> sorting = new ArrayList<>();
			HashMap<Integer, SortingStationsData> sortMap = new HashMap<>();
			
			for(BusStation station : busStations) {
				SortingStationsData temp = new SortingStationsData(station.getName(), getOrderIndexOfStation(data.getKey(), station.getName()));
				
				sorting.add(temp);
				sortMap.put(new Integer(temp.getIndex()), temp);
			}
			
			
			
			ArrayList<SortingStationsData> sorted = new ArrayList<>();
			ArrayList<SortingStationsData> temp = sorting;
			
			
			while(sorted.size() != busStations.size()) {
				//find lowest value - and index
				//add it to sorted
				//black temp
				//copy everything except index to temp
				//black working list
				//copy temp to working list
				
				int index = findMinimunIndexValue(sorting);
				sorted.add(sorting.get(index));
				temp = new ArrayList<>();
				
				sorting.remove(index);
				temp = sorting;
				sorting = new ArrayList<>();
				
				sorting = temp;
			}
			
			nameOfTheStations = new ArrayList<>();
			for(int i = 0; i < sorted.size(); i++) {
				nameOfTheStations.add(sorted.get(i).getStationName());
			}
			
			this.orderOfStationsForLine.put(data.getKey(), nameOfTheStations);
		}	
	}
	
	public int findMinimunIndexValue(ArrayList<SortingStationsData> list) {
		int retVal = 0;
		
		SortingStationsData min = list.get(0);
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getIndex() < min.getIndex()) {
				retVal = i;
				min = list.get(i);
			}
		}
		
		return retVal;
	}
	
	public void printOrderOfTheStation() {
		Set<Entry<String, ArrayList<String>>> set = this.orderOfStationsForLine.entrySet();
		
		for(Entry<String, ArrayList<String>> data : set) {
			String lineName = data.getKey();
			ArrayList<String> stations = data.getValue();
			System.out.println("Linija: " + lineName);
			for(String stationName : stations) {
				System.out.println("\t"+stationName+"; index: " + getOrderIndexOfStation(lineName, stationName));
			}
			System.out.println();
		}
	}
	
	public int getConnectorIndex(String lineName, String stationName) {
		
		int retVal = 0;
		HashMap<String, BusStation> stationsInHashMap = this.busStations;
		BusStation station = stationsInHashMap.get(stationName);
		
		for(int i = 0; i < station.getStationConnector().size(); i++) {
			if(station.getStationConnector().get(i).getLineName().equals(lineName)) {
				retVal = i;
			}
		}
		
		return retVal;
	}
	
	public int getOrderIndexOfStation(String lineName, String stationName) {
		HashMap<String, BusStation> stationsInHashMap = this.busStations;
		BusStation station = stationsInHashMap.get(stationName);
		
		for(BusStationLineConnector connector : station.getStationConnector()) {
			if(connector.getLineName().equals(lineName)) {
				return connector.getIndexOfPointBefore();
			}
		}
		
		return -1;
	}

	public void printBusLine() {
		HashMap<String, BusLine> busLines = this.busLines;
		Set<Entry<String, BusLine>> setOfBusLines = busLines.entrySet();
		
		for(Entry<String, BusLine> entry : setOfBusLines) {
			BusLine busLine = entry.getValue();
			
			System.out.println(busLine.getCodeName());
			System.out.println("Time table:");
			for(String time : busLine.getTimeTable()) {
				System.out.println("\t" + time);
			}
			System.out.println("Line positions and distances:");
			for(LinePosition position : busLine.getLinePositions()) {
				System.out.println(
						"\t" +
						"Lat: " + position.getLineCoordinate().getLatitude() + "; " +
						"Lon: " + position.getLineCoordinate().getLongitude() + "; " +
						"Distance: " + position.getDistanceFromLastCoordinateLinePoint());
			}
			
		}
	}
	
	public void printBusStations() {
		HashMap<String, BusStation> busStations = this.busStations;
		Set<Entry<String, BusStation>> setOfBusStations = busStations.entrySet();
		
		for(Entry<String, BusStation> entry : setOfBusStations) {
			BusStation busStation = entry.getValue();
			
			System.out.println(busStation.getName());
			
			ArrayList<BusStationLineConnector> connectors = busStation.getStationConnector();		
			for(BusStationLineConnector con : connectors) {
				System.out.println("Linija: "+con.getLineName());
				System.out.println("\tDistance from prior station: " + con.getDistanceFromLastStation());
				System.out.println();
			}
			
		}
		
	}
	
	
	
	
}

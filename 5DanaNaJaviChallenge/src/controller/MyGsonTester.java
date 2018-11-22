package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import model.BusLine;
import model.BusStation;
import model.CoordinatesGPS;
import model.LinePosition;
import repository.DataPool;

public class MyGsonTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JsonParser parser = new JsonParser();
				
		File fileDIR = new File(args[0]);		
		
		try {
			JsonElement jsontree = parser.parse(new FileReader(fileDIR));
			
			JsonObject jo = jsontree.getAsJsonObject();
			
			
			JsonElement lines = jo.get("lines");
			JsonElement stops = jo.get("stops");
			
			JsonArray arrayLines = lines.getAsJsonArray();
			JsonArray arrayStops = stops.getAsJsonArray();
			
			for(JsonElement line : arrayLines) {
				BusLine tempBusLine = new BusLine();
				
				JsonObject lineObj = line.getAsJsonObject();
				String codeName = lineObj.get("name").getAsString();
				String description = lineObj.get("description").getAsString();
				
				tempBusLine.setCodeName(codeName);
				tempBusLine.setDescription(description);
				
				JsonArray coordinates = lineObj.get("coordinates").getAsJsonArray();
				ArrayList<CoordinatesGPS> tempCoordinatesList = new ArrayList<>();
				ArrayList<LinePosition> tempLinePositionList = new ArrayList<>();
 				
				for(JsonElement coordinate : coordinates) {
					JsonObject coordinateObj = coordinate.getAsJsonObject();
					double lat = coordinateObj.get("lat").getAsDouble();
					double lon = coordinateObj.get("lon").getAsDouble();
					
					CoordinatesGPS tempCoordinates = new CoordinatesGPS(lat, lon);
					
					LinePosition tempLinePosition = new LinePosition();
					tempLinePosition.setLineCoordinate(tempCoordinates);
					
					tempCoordinatesList.add(tempCoordinates);
					tempLinePositionList.add(tempLinePosition);
				}
				
				tempBusLine.setLinePositions(tempLinePositionList);
				
				JsonArray timeTable = lineObj.get("timeTable").getAsJsonArray();
				ArrayList<String> tempTimeTable = new ArrayList<>();
				
				for(JsonElement time : timeTable) {
					String tempTime = time.getAsString();
					tempTimeTable.add(tempTime);
				}
				
				tempBusLine.setTimeTable(tempTimeTable);
				
				DataPool.getInstance().getBusLines().put(tempBusLine.getCodeName(), tempBusLine);
				
			}
			
			System.out.println();
			
			for(JsonElement stop : arrayStops) {
				BusStation tempBusStation = new BusStation();
				
				JsonObject stopObj = stop.getAsJsonObject();
				String name = stopObj.get("name").getAsString();
				double lat = stopObj.get("lat").getAsDouble();
				double lon = stopObj.get("lon").getAsDouble();
				CoordinatesGPS tempStationCoordinate = new CoordinatesGPS(lat, lon);
				
				tempBusStation.setName(name);
				tempBusStation.setStationCoordinates(tempStationCoordinate);				
				
				JsonArray linesOnThisStop = stopObj.get("lines").getAsJsonArray();
				ArrayList<String> tempLinesOnThisStop = new ArrayList<>();
				
				for(JsonElement line : linesOnThisStop) {
					String tempLine = line.getAsString();
					tempLinesOnThisStop.add(tempLine);
				}
				
				tempBusStation.setLineStops(tempLinesOnThisStop);
				
				DataPool.getInstance().getBusStations().put(tempBusStation.getName(), tempBusStation);
			}
			
			BusStationNetworkCreator BSNC = new BusStationNetworkCreator();
			BSNC.connectLinesAndStations();
			BSNC.orderStationInLine();
			
			DataPool.getInstance().sortStationsInTheLine();
			
			BSNC.calculateDistanceBetweenLinePoints();
			BSNC.calculateDistancesFromTheLastStation();
			
			//DataPool.getInstance().printBusLine();
			//DataPool.getInstance().printOrderOfTheStation();
			//DataPool.getInstance().printBusStations();
			
			Pathfinder pathfinder = new Pathfinder();
			
			String startTime = args[5];
			
			CoordinatesGPS myCoord = new CoordinatesGPS(45.257583, 19.830311);
			CoordinatesGPS bazarCoord = new CoordinatesGPS(45.254303, 19.842488);
			CoordinatesGPS faks = new CoordinatesGPS(45.246661, 19.850897);
			CoordinatesGPS futog = new CoordinatesGPS(45.2419, 19.720331);
			
			double lat1 = Double.parseDouble(args[1]);
			double lon1 = Double.parseDouble(args[2]);
			double lat2 = Double.parseDouble(args[3]);
			double lon2 = Double.parseDouble(args[4]);
			
			CoordinatesGPS myNewCoord = new CoordinatesGPS(lat1, lon1);
			CoordinatesGPS newFaks = new CoordinatesGPS(lat2, lon2);
			
			pathfinder.checkForRoute(myNewCoord, newFaks, startTime);
			System.out.println(startTime);
			
			
			
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}

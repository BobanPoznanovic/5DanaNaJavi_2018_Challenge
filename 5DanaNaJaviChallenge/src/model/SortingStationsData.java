package model;

public class SortingStationsData{

	private String stationName;
	private int index;
	
	public SortingStationsData() {
		
	}
	
	public SortingStationsData(String name, int index) {
		setStationName(name);
		setIndex(index);
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}

package model;

import java.util.ArrayList;

public class BusLine {
	
	private String codeName;
	private String description;
	private ArrayList<LinePosition> linePositions;
	private ArrayList<String> timeTable;
	
	public BusLine() {
		setLinePositions(new ArrayList<>());
		setTimeTable(new ArrayList<>());
	}
	
	public BusLine(String id, String desc) {
		setCodeName(id);
		setDescription(desc);
		setLinePositions(new ArrayList<>());
		setTimeTable(new ArrayList<>());
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<String> getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(ArrayList<String> timeTable) {
		this.timeTable = timeTable;
	}

	public ArrayList<LinePosition> getLinePositions() {
		return linePositions;
	}

	public void setLinePositions(ArrayList<LinePosition> linePositions) {
		this.linePositions = linePositions;
	}
}

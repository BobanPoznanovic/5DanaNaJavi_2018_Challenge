package model;

import java.util.ArrayList;

public class Route {

	private ArrayList<Activity> activities;
	private String ArrivalTime;
	
	public Route() {
		setActivities(new ArrayList<>());
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}

	public String getArrivalTime() {
		return ArrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		ArrivalTime = arrivalTime;
	}
	
	public String toString() {
		String retVal = "";
		
		for(int i = 0; i < getActivities().size(); i++) {
			getActivities().get(i).toString();
		}
		
		return retVal;
	}
}

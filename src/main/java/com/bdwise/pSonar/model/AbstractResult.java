package com.bdwise.pSonar.model;

import java.util.ArrayList;

public class AbstractResult {
	protected String startTime;
	protected String tool;
	protected String destination;	
	protected ArrayList<String> participants;

	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getTool() {
		return tool;
	}
	public void setTool(String tool) {
		this.tool = tool;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public ArrayList<String> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<String> participants) {
		this.participants = participants;
	}
	
}

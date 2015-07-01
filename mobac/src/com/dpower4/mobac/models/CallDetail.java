package com.dpower4.mobac.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CallDetail {
	
	private String secondParty;
	private String duration;
	private String type;
	private String time;
	private int id;
	private String callerName;
	
	public CallDetail(String second_party, String duration, String type,
			String time, String callerName, int id) {
		if (callerName != null)
			this.callerName = callerName.replaceAll("[^A-Za-z0-9]", "");;
		this.secondParty = second_party;
		this.duration = duration;
		this.type = type;
		this.time = time;
		this.id = id;
	}

	public String getSecondParty() {
		return secondParty;
	}

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public void setSecondParty(String secondParty) {
		this.secondParty = secondParty;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public JSONObject getJSONObject() throws JSONException{
		
		JSONObject data = new JSONObject();
		
		data.put("secondParty", this.secondParty);
		data.put("callDuration", this.duration);
		data.put("type", this.type);
		data.put("time", this.time);		
		data.put("callerName", this.callerName);
		
		return data;
		
	}
	public String toJsonString() throws JSONException{
		
		JSONObject data = new JSONObject();
		
		data.put("secondParty", this.secondParty);
		data.put("callDuration", this.duration);
		data.put("type", this.type);
		data.put("time", this.time);		
		data.put("callerName", this.callerName);
		
		JSONObject root = new JSONObject();
		root.put("root", data );
		
		return root.toString();
		
	}
	

}

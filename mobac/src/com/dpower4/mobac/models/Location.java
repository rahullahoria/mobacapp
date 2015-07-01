package com.dpower4.mobac.models;

import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
	
	private String latitude;
	private String longitude;
	private String time;
	private int id; 
	
	public Location (String latitude, String longitude, int id){
		this.latitude = latitude;
		this.longitude = longitude;
		java.util.Date date = new Date();
		this.time = new Timestamp(date.getTime() - 300).toString();
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Location [latitude=" + this.latitude + ", longitude=" + this.longitude
				+ ", time=" + this.time + "]";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toJsonString() throws JSONException{
		
		JSONObject data = new JSONObject();
		data.put("latitude", this.latitude);
		data.put("longitude", this.longitude);
		data.put("fromTime", this.time);		
		data.put("toTime", this.time);		
		
		JSONObject root = new JSONObject();
		root.put("root", data );
		
		return root.toString();
		
	}
	
	

}

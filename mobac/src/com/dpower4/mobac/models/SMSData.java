package com.dpower4.mobac.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class represents SMS.
 * 
 * @author itcuties
 * 
 */
public class SMSData {

	// Number from witch the sms was send
	private String number;
	private String body;

	private int id;
	private String type;
	private String time;

	public SMSData(String number, String body, String type, String time, int id) {

		this.number = number;
		this.body = body;
		this.id = id;
		this.type = type;
		this.time = time;
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toJsonString() throws JSONException {

		JSONObject data = new JSONObject();

		data.put("fromTo", this.number);
		data.put("messageText", this.body);
		data.put("type", this.type);
		data.put("time", this.time);

		JSONObject root = new JSONObject();
		root.put("root", data);

		return root.toString();

	}

	public Object getJSONObject() throws JSONException {
		JSONObject data = new JSONObject();

		data.put("fromTo", this.number);
		data.put("messageText", this.body);
		data.put("type", this.type);
		data.put("time", this.time);
		
		return data;
	}
}

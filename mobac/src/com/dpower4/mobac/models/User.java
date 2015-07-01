package com.dpower4.mobac.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;


public class User implements Serializable  {
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String password;
	
	private int callDetailId = 0;
	private int SMSId = 0;
	private int contactId = 0;
	
	private int callDetailCount = 0;
	private int SMSCount = 0;
	private int contactCount = 0;
	
	public int getCallDetailCount() {
		return callDetailCount;
	}

	public void setCallDetailCount(int callDetailCount) {
		this.callDetailCount = callDetailCount;
	}

	public int getSMSCount() {
		return SMSCount;
	}

	public void setSMSCount(int sMSCount) {
		SMSCount = sMSCount;
	}

	public int getContactCount() {
		return contactCount;
	}

	public void setContactCount(int contactCount) {
		this.contactCount = contactCount;
	}

	private Location location = null;
	
	private String authKey = null;
	private String PROJECT = "Mobac";
	
	private static final long serialVersionUID = 46543445; 
		  
	public User(String firstName, String lastName, String phone, String email,
			String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	
	public int getCallDetailId() {
		return callDetailId;
	}

	public void setCallDetailId(int callDetailId) {
		this.callDetailId = callDetailId;
	}

	public int getSMSId() {
		return SMSId;
	}

	public void setSMSId(int sMSId) {
		SMSId = sMSId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}
	
	public String toJsonString() throws JSONException{
		
		/*
		 *
		 * {
               "firstName": "Rajnish",
               "lastName": "Panwar",
               "email": "rajnish_pawar90@yahoo.com",
               "phoneNo": "890141422",
               "password": "redhat"    
         * }
		 * 
		 * */
		
		JSONObject data = new JSONObject();
		
		data.put("firstName", this.firstName);
		data.put("lastName", this.lastName);
		data.put("email", this.email);
		data.put("phoneNo", this.phone);
		data.put("password", this.password);
		data.put("project", this.PROJECT);
		data.put("username", this.phone);
				
		JSONObject root = new JSONObject();
		root.put("root", data );
		
		return root.toString();
		
	}


}

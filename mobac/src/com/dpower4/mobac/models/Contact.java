package com.dpower4.mobac.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Contact {
	
	String phone = null;
	String email = null;
	String emailType = null;
	String name = null;
	
	public Contact(String phone, String email, String emailType, String name) {
	
		this.phone = phone;
		this.email = email;
		this.emailType = emailType;
		if (name != null)
			this.name = name.replaceAll("[^A-Za-z0-9]", "");;
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

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public JSONObject getJSONObject() throws JSONException{
		
		JSONObject data = new JSONObject();
		
		data.put("name", this.name);
		data.put("email_contact", this.email);
		data.put("email_type", this.emailType);
		data.put("phone", this.phone);		
		
		return data;
		
	}
	
	
	
	

}

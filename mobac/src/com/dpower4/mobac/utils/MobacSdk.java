package com.dpower4.mobac.utils;

import org.json.JSONObject;

public class MobacSdk extends MobacApiClient {

	private static final String DOMAIN = "api.mobac.dpower4.com";
	private static final String VERSIOM = "v0";
	
	public MobacSdk() {
		super(DOMAIN, VERSIOM);
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject postMessages(String Data) throws Exception{
		return api("messages", "POST", null, Data);
		
	}
	
	public JSONObject postLocations(String Data) throws Exception{
		return api("locations", "POST", null, Data);
		
	}
	
	public JSONObject postCallDetails(String Data) throws Exception{
		return api("call-details", "POST", null, Data);
		
	}
	
	public JSONObject postContacts(String Data) throws Exception{
		
		return api("contacts", "POST", null, Data);
	}

	public JSONObject registorUser(String Data) throws Exception {
		
		return api("user", "POST", null, Data);
	}
	

}

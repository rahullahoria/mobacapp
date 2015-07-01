package com.dpower4.mobac.utils;

import org.json.JSONObject;

import android.util.Log;

public class MobacAuthSdk extends MobacApiClient {

	private static final String DOMAIN = "auth.dpower4.com";
	private static final String VERSIOM = "v0";

	public MobacAuthSdk() {
		super(DOMAIN, VERSIOM);
		// TODO Auto-generated constructor stub
	}
	
	public String getAuthKey(String DATA) throws Exception{
		JSONObject response = api("auth", "POST", null,
				DATA);
		Log.d("getauthkey Response", response.toString());
		return response.getString("auth-key");
		
	}
	

}

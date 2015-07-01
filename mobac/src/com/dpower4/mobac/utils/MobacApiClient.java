package com.dpower4.mobac.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.Iterator;

import org.json.JSONException;

import com.dpower4.mobac.services.MobacService;

import android.util.Log;

import java.net.URI;

public class MobacApiClient {

	
	private String BASE_URL = null;

	HttpClient http;
	HttpResponse httpResponse;
	String DomainName;
	
	public MobacApiClient(String DomainName, String version) {
		this.DomainName = DomainName;
		this.BASE_URL = "http://" + DomainName + "/" + version
				+ "/";

		http = new DefaultHttpClient();
	}

	private String getPramString(JSONObject param) {
		if (param == null)
			return "";
		Iterator<?> keys = param.keys();
		String paramString = "?";
		try {
			while (keys.hasNext()) {
				String key = (String) keys.next();
				paramString += key + "=" + param.get(key) + "&";

			}
		} catch (JSONException e) {
			return "";
		}
		return paramString.substring(0, paramString.length() - 1);
	}

	private JSONObject evaluvate(JSONObject response) throws MobacException {
		Log.v("API Client Res",response.toString());
		try {
			
			JSONObject internalStatus = response
					.getJSONObject("internal_status");
	
			if (response.has("data")) {

				return response.getJSONObject("data");
			}
			//throw new MobacException(internalStatus.getString("message"));
			return null;
		} catch (JSONException e) {
			return null;
		}
	}

	public JSONObject api(String endpoint, String httpMethod, JSONObject param,
			String data) throws Exception {

		String url = this.BASE_URL + endpoint
				+ getPramString(param);
		Log.d("api",url + " data " + data);
		if (httpMethod.equals("POST")) {

			HttpPost post = new HttpPost();
			post.setEntity(new StringEntity(data));
			return evaluvate(makeApiCall(url, null, post));
		} else if (httpMethod.equals("GET")) {

			return evaluvate(makeApiCall(url, null, new HttpGet()));
		} else if (httpMethod.equals("PUT")) {

			return evaluvate(makeApiCall(url, null, new HttpPut()));
		}

		return null;

	}

	private JSONObject makeApiCall(String url, String data,
			HttpRequestBase method) throws Exception {
		
		if(DomainName != "auth.dpower4.com"){
			if(MobacService.user != null)
				method.setHeader("auth-key", MobacService.user.getAuthKey());
		}
		
		method.setURI(new URI(url));
		
		httpResponse = http.execute(method);
		HttpEntity entity = httpResponse.getEntity();
		Log.e("reached","here-----------");
		if (entity != null) {

			String jsonString = EntityUtils.toString(entity);
			Log.v("response str", jsonString);
			
			return new JSONObject(jsonString);
		} else {

			throw new Exception("No data");
		}
	}

}
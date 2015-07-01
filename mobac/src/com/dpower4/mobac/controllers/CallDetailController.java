package com.dpower4.mobac.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import com.dpower4.mobac.database.DatabaseHandler;
import com.dpower4.mobac.models.CallDetail;
import com.dpower4.mobac.models.User;
import com.dpower4.mobac.services.MobacService;
import com.dpower4.mobac.utils.MobacApiClient;
import com.dpower4.mobac.utils.MobacSdk;

public class CallDetailController {
	private DatabaseHandler db;
	private Context context;
	
	public CallDetailController(Context context){
		this.context = context;
		this.db = new DatabaseHandler(context);
	}
	
	public Boolean saveCurrentCallDetails() throws JSONException, Exception{
		Boolean status = false;
		int lastId = MobacService.user.getCallDetailId();
		// StringBuffer sb = new StringBuffer();
		Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
				CallLog.Calls._ID + " > "  + lastId	, null, null);
		
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
		
		int i = 0;
		JSONObject bulkCallDetails = new JSONObject();
		JSONObject root = new JSONObject();
		JSONArray callDetailsArray = new JSONArray();
		
		MobacSdk mobac = new MobacSdk();
		
		int idR = lastId;
		int toSetId = lastId;
		
		while (managedCursor.moveToNext()) {
			i++;
			Log.e("kkkkkkkkkkkkkkkk",lastId + "  " + managedCursor.getCount() + "  " + i);	
			idR = managedCursor.getInt(id);
			if (idR > toSetId) toSetId = idR;
			String phNumber = managedCursor.getString(number);
			
			
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String callDuration = managedCursor.getString(duration);
			String callerName = managedCursor.getString(name);
			
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
				case CallLog.Calls.OUTGOING_TYPE:
					dir = "OUTGOING";
					break;
	
				case CallLog.Calls.INCOMING_TYPE:
					dir = "INCOMING";
					break;
	
				case CallLog.Calls.MISSED_TYPE:
					dir = "MISSED";
					break;
			}
			
			CallDetail callDetail = new CallDetail(phNumber, callDuration, dir, format.format(callDayTime), callerName , 0);
			
			callDetailsArray.put(callDetail.getJSONObject());
			
			if (i >= 20 ){
				
				bulkCallDetails.put("callDetails", callDetailsArray);
				root.put("root", bulkCallDetails);
				
				mobac.postCallDetails(root.toString());
				
				MobacService.user.setCallDetailId(toSetId);
				MobacService.user.setCallDetailCount(MobacService.user.getCallDetailCount() + i);
				
				bulkCallDetails.remove("callDetails");
				root.remove("root");
				
				
				i = 0;
				callDetailsArray = new JSONArray();
				
				
			}
			
			
		}
		
		managedCursor.close();
		
		if(i > 0){
		
			bulkCallDetails.put("callDetails", callDetailsArray);
			root.put("root", bulkCallDetails);
			
			mobac.postCallDetails(root.toString());
			MobacService.user.setCallDetailCount(MobacService.user.getCallDetailCount() + i);
		}
		MobacService.user.setCallDetailId(toSetId);
		
		return status;
		
	}
	
}

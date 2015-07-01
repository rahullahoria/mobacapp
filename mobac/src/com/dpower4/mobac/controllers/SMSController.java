package com.dpower4.mobac.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony.Sms;
import android.util.Log;

import com.dpower4.mobac.models.SMSData;

import com.dpower4.mobac.services.MobacService;
import com.dpower4.mobac.utils.MobacApiClient;
import com.dpower4.mobac.utils.MobacSdk;

public class SMSController {

	List<SMSData> smsList;
	private Context context;

	public SMSController(Context context) {
		this.context = context;
		this.smsList = new ArrayList<SMSData>();
	}

	public Boolean getCurrentSMS() throws JSONException, Exception {

		int lastId = MobacService.user.getSMSId();
		Uri uri = Uri.parse("content://sms/");
		Cursor c = context.getContentResolver().query(uri, null,
				Sms._ID + " > " + lastId, null, null);

		int id = c.getColumnIndex(Sms._ID);

		int currentId = lastId;
		int toSetId = lastId;

		int i = 0;
		JSONObject bulkSMS = new JSONObject();
		JSONObject root = new JSONObject();
		JSONArray smsArray = new JSONArray();
		MobacSdk mobac = new MobacSdk();

		if (c.moveToFirst()) {
			while (c.moveToNext()) {
				i++;

				currentId = c.getInt(id);
				if (currentId > toSetId) toSetId = currentId;
				Date smsDayTime = new Date(Long.valueOf(c.getString(c
						.getColumnIndexOrThrow("date"))));

				String type = null;
				if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
					Log.d("type", "inbox");
					type = "INBOX";
				} else {
					Log.d("type", "sent");
					type = "SENT";
				}

				SMSData sms = new SMSData(c.getString(c
						.getColumnIndexOrThrow("address")), c.getString(c
						.getColumnIndexOrThrow("body")), type,
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(smsDayTime), 0);

				Log.d("message",
						c.getString(c.getColumnIndexOrThrow("_id"))
								+ " "
								+ c.getString(c
										.getColumnIndexOrThrow("address"))
								+ " "
								+ c.getString(c.getColumnIndexOrThrow("body"))
								+ " " + c.getString(c.getColumnIndex("read"))
								+ " "
								+ c.getString(c.getColumnIndexOrThrow("date")));

				smsArray.put(sms.getJSONObject());

				if (i >= 20) {

					bulkSMS.put("messages", smsArray);
					root.put("root", bulkSMS);
					mobac.postMessages(root.toString());

					bulkSMS.remove("messages");
					root.remove("root");

					MobacService.user.setSMSId(toSetId);
					MobacService.user.setSMSCount(MobacService.user.getSMSCount() + i);
					
					i = 0;
					smsArray = new JSONArray();

				}
			}
		}
		c.close();

		if (i > 0) {
			bulkSMS.put("messages", smsArray);
			root.put("root", bulkSMS);
			mobac.postMessages(root.toString());
			MobacService.user.setSMSCount(MobacService.user.getSMSCount() + i);
		}

		MobacService.user.setSMSId(toSetId);

		return null;

	}
}

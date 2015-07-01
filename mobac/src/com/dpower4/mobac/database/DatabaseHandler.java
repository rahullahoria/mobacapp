package com.dpower4.mobac.database;

import java.util.ArrayList;
import java.util.List;

import com.dpower4.mobac.models.CallDetail;
import com.dpower4.mobac.models.Location;
import com.dpower4.mobac.models.SMSData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "mobac_loc";
	
	private static final String TABLE_CALL_DETAILS = "call_details";
	private static final String TABLE_MESSAGES = "messages";
	private static final String TABLE_LOCATIONS = "locations";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
				
	/*	String CREATE_TABLE_CALL_DETAILS = "CREATE TABLE " + TABLE_CALL_DETAILS 
					+ "( id INTEGER PRIMARY KEY, second_party TEXT, duration TEXT, type TEXT, time Text, status INTEGER )";
		db.execSQL(CREATE_TABLE_CALL_DETAILS);
	
		String CREATE_TABLE_MESSAGES = "CREATE TABLE " + TABLE_MESSAGES 
				+ "( id INTEGER PRIMARY KEY, number TEXT, body TEXT, type TEXT, time Text, status INTEGER )";
		db.execSQL(CREATE_TABLE_MESSAGES);
	*/		
		String CREATE_TABLE_LOCATIONS  = "CREATE TABLE " + TABLE_LOCATIONS   
				+ "( id INTEGER PRIMARY KEY, latitude TEXT, longitude TEXT, time Text, status INTEGER )";
		db.execSQL(CREATE_TABLE_LOCATIONS);
		
		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

		// Create tables again
		onCreate(db);
	}

	void addCallDetail(CallDetail callDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("second_party", callDetail.getSecondParty());
		values.put("duration", callDetail.getDuration());
		values.put("type", callDetail.getType());
		values.put("time", callDetail.getTime());
		values.put("status", 0);

		db.insert(TABLE_CALL_DETAILS, null, values);
		db.close();
	}

	// Getting single contact
	CallDetail getCallDetail(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CALL_DETAILS, new String[] { "id",
				"second_party", "duration", "type", "time" }, "id" + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		CallDetail callDetail = new CallDetail(
				cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(0)));
		return callDetail;
	}
	
	// Getting All Contacts
	public List<CallDetail> getAllContacts() {
		List<CallDetail> callDetailList = new ArrayList<CallDetail>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CALL_DETAILS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CallDetail callDetail = new CallDetail(
						cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(0)));
				callDetailList.add(callDetail);
			} while (cursor.moveToNext());
		}

		// return contact list
		return callDetailList;
	}

	// Updating single contact
	public int updateCallDetail(CallDetail callDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("second_party", callDetail.getSecondParty());
		values.put("duration", callDetail.getDuration());
		values.put("type", callDetail.getType());
		values.put("time", callDetail.getTime());
		values.put("status", 0);

		// updating row
		return db.update(TABLE_CALL_DETAILS, values, "id" + " = ?",
				new String[] { String.valueOf(callDetail.getId()) });
	}

	// Deleting single contact
	public void deleteCallDetail(CallDetail contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CALL_DETAILS, "id = ?",
				new String[] { String.valueOf(contact.getId()) });
		db.close();
	}


	// Getting contacts Count
	public int getCallDetailsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CALL_DETAILS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
	public List<CallDetail> getAllContactsToSend() {
		List<CallDetail> callDetailList = new ArrayList<CallDetail>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CALL_DETAILS + "WHERE status = 0";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CallDetail callDetail = new CallDetail(
						cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5), Integer.parseInt(cursor.getString(0)));
				callDetailList.add(callDetail);
			} while (cursor.moveToNext());
		}

		// return contact list
		return callDetailList;
	}
	
	// Updating single contact
	public int updateCallDetailStatus(CallDetail callDetail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("second_party", callDetail.getSecondParty());
		values.put("duration", callDetail.getDuration());
		values.put("type", callDetail.getType());
		values.put("time", callDetail.getTime());
		values.put("status", 1);

		// updating row
		return db.update(TABLE_CALL_DETAILS, values, "id" + " = ?",
				new String[] { String.valueOf(callDetail.getId()) });
	}
	
	void addSMSData(SMSData smsData) {
		SQLiteDatabase db = this.getWritableDatabase();

		
		ContentValues values = new ContentValues();
		values.put("number", smsData.getNumber());
		values.put("body", smsData.getBody());
		values.put("type", smsData.getType());
		values.put("time", smsData.getTime());
		values.put("status", 0);

		db.insert(TABLE_MESSAGES, null, values);
		db.close();
	}
	
	public List<SMSData> getAllSMSDataToSend() {
		List<SMSData> smsDataList = new ArrayList<SMSData>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + "WHERE status = 0";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SMSData smsData = new SMSData(
						cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Integer.parseInt(cursor.getString(0)));
				smsDataList.add(smsData);
			} while (cursor.moveToNext());
		}

		// return contact list
		return smsDataList;
	}
	
	// Updating single contact
	public int updateSMSDataStatus(SMSData smsData) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("number", smsData.getNumber());
		values.put("body", smsData.getBody());
		values.put("type", smsData.getType());
		values.put("time", smsData.getTime());
		values.put("status", 1);

		// updating row
		return db.update(TABLE_MESSAGES, values, "id" + " = ?",
				new String[] { String.valueOf(smsData.getId()) });
	}
	//////////////////////////////////Location//////////////////////////////
	public void addLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		values.put("time", location.getTime());
		values.put("status", 0);

		db.insert(TABLE_LOCATIONS, null, values);
		db.close();
	}
	
	public List<Location> getAllLocationToSend() {
		List<Location> locationList = new ArrayList<Location>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS + " WHERE status = 0";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Location location = new Location(
						cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(0)));
				locationList.add(location);
			} while (cursor.moveToNext());
		}

		// return contact list
		return locationList;
	}
	
	// Updating single contact
	public int updateLocationStatus(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		values.put("time", location.getTime());
		values.put("status", 1);

		// updating row
		return db.update(TABLE_LOCATIONS, values, "id" + " = ?",
				new String[] { String.valueOf(location.getId()) });
	}

	public int deleteLocation(Location location) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_LOCATIONS, "id" + " = ?",
				new String[] { String.valueOf(location.getId()) });
		
	}

}
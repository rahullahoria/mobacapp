package com.dpower4.mobac.controllers;

import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.widget.Toast;

import com.dpower4.mobac.database.DatabaseHandler;
import com.dpower4.mobac.models.Location;
import com.dpower4.mobac.models.User;
import com.dpower4.mobac.utils.GPSTracker;
import com.dpower4.mobac.utils.MobacApiClient;
import com.dpower4.mobac.utils.MobacSdk;

public class LocationController {

	private GPSTracker gps;
	private Context context;
	private DatabaseHandler dbHandler = null;
	
	public LocationController(Context context){
		
		this.context = context;
		this.dbHandler = new DatabaseHandler(context);
	}
	
	public void saveCurrentLocation() {
	
		List<Location> locationList = dbHandler.getAllLocationToSend();
		MobacSdk mobac = new MobacSdk();
		
		for (int i = 0; i < locationList.size(); i++) {
			
			try {
				mobac.postLocations( locationList.get(i).toJsonString() );
				dbHandler.deleteLocation( locationList.get(i) );
			} catch (Exception e) {
				
			}
			
		}
				
	}
	
	public void insertCurrentLocation(){
		this.gps = new GPSTracker(context);
		if (gps.canGetLocation()) {
			
			Location location = new Location(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()), 0);
		
			dbHandler.addLocation(location);
			
	
		} else {
			gps.showSettingsAlert();
		}
	
	}
}

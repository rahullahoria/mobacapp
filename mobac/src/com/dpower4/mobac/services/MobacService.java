package com.dpower4.mobac.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dpower4.mobac.MainActivity;
import com.dpower4.mobac.controllers.CallDetailController;
import com.dpower4.mobac.controllers.ContactController;
import com.dpower4.mobac.controllers.LocationController;
import com.dpower4.mobac.controllers.SMSController;
import com.dpower4.mobac.models.User;
import com.dpower4.mobac.utils.CheckInternetStatus;
import com.dpower4.mobac.utils.MobacApiClient;
import com.dpower4.mobac.utils.MobacAuthSdk;
import com.dpower4.mobac.utils.SerializeDeserilizeObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MobacService extends Service {

	public static User user = null;
	public static boolean other = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		//Toast.makeText(this, " Mobac backup service started ",
			//	Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (other) {
			//Toast.makeText(this, " Mobac brother is already running ",
				//	Toast.LENGTH_LONG).show();
			return;
		}
		other = true;
		Toast.makeText(this, " Starting backup ", Toast.LENGTH_LONG).show();
		Runnable r = new Runnable() {
			public void run() {

				if (CheckInternetStatus.isNetworkAvailable(MobacService.this)) {

					try {
						
						 MobacService.user = (User) SerializeDeserilizeObject
								.loadSerializedObject(MainActivity.USER_FILE);
					} catch (Exception e) {

						Log.e("Exeption in deserializing at MobacService.java",
								e.toString());

						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);

						startActivity(i);
						Log.e("Error : ", e.toString());
						MobacService.other = false;

						return;
					}

					if (MobacService.user.getAuthKey() == null) {
						MobacAuthSdk auth = new MobacAuthSdk();

						int i = 0;
						String authKey = null;
						try {
							do {
								i++;
								authKey = auth.getAuthKey(user.toJsonString());
								Thread.sleep(5000);
							} while (authKey == null && i < 6);

							if (authKey == null) {
								File file = new File(MainActivity.USER_FILE);

								if (file.exists())
									file.delete();

								MobacService.other = false;
								return;
							}

							user.setAuthKey(authKey);
							SerializeDeserilizeObject.saveObject(MobacService.user, MainActivity.USER_FILE );

						} catch (Exception e) {
							Log.e("Error : ", e.toString());
							Toast.makeText(MobacService.this,
									"Error: " + e.toString(),
									Toast.LENGTH_SHORT).show();
							MobacService.other = false;

							return;
						}

					}

					CallDetailController callDetailController = new CallDetailController(
							MobacService.this);

					try {
						callDetailController.saveCurrentCallDetails();
					} catch (Exception e) {
						Log.e("Error : ", e.toString());
					} finally {

						try {
							SerializeDeserilizeObject.saveObject(MobacService.user, MainActivity.USER_FILE );
						} catch (IOException e) {
							Toast.makeText(MobacService.this,
									" Sorry, unable to update User",
									Toast.LENGTH_SHORT).show();
							Log.e("Error : ", e.toString());
						}
					}

					ContactController contactController = new ContactController(
							MobacService.this);

					try {
						contactController.saveContacts();
					} catch (Exception e) {
						Log.e("Contacts Error : ", e.toString());
						Toast.makeText(MobacService.this,
								e.toString(),
								Toast.LENGTH_SHORT).show();
						
					} finally {

						try {
							SerializeDeserilizeObject.saveObject(MobacService.user, MainActivity.USER_FILE );
						} catch (IOException e) {
							Toast.makeText(MobacService.this,
									" Sorry, unable to update User",
									Toast.LENGTH_SHORT).show();
							Log.e("Error : ", e.toString());
						}
					}

					LocationController locationController = new LocationController(
							MobacService.this);

					locationController.saveCurrentLocation();
					
					SMSController smsController = new SMSController(
							MobacService.this);

					try {
						smsController.getCurrentSMS();
					} catch (Exception e) {
						Log.e("Error : ", e.toString());
						// e.printStackTrace();
					} finally {

						try {
							SerializeDeserilizeObject.saveObject(MobacService.user, MainActivity.USER_FILE );
						} catch (IOException e) {
							Toast.makeText(MobacService.this,
									" Sorry, unable to update User",
									Toast.LENGTH_SHORT).show();
							Log.e("Error : ", e.toString());
						}
					}

				} else {
					//Toast.makeText(MobacService.this,
						//	" Sorry, No internet available", Toast.LENGTH_SHORT)
							//.show();
				}

				MobacService.other = false;
				stopSelf();
			}
		};
		Thread t = new Thread(r);
		t.start();
		LocationController locationController = new LocationController(
				MobacService.this);

		
		locationController.insertCurrentLocation();
		
		
		return;
	}

	@Override
	public void onDestroy() {
		MobacService.other = false;

		//Toast.makeText(this, " Mobac Servics Stopped", Toast.LENGTH_SHORT)
			//	.show();
		super.onDestroy();
	}

}

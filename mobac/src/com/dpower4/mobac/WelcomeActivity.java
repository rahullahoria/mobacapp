package com.dpower4.mobac;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.Calendar;

import com.dpower4.mobac.models.User;
import com.dpower4.mobac.services.MobacService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony.Sms;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		TextView t2 = (TextView) findViewById(R.id.text2);
		TextView lastCDTV = (TextView) findViewById(R.id.text3);
		TextView lastSMSTV = (TextView) findViewById(R.id.text4);
		TextView contactBackupPercent = (TextView) findViewById(R.id.text5);

		try {
			User user = (User) loadSerializedObject(new File(
					MainActivity.USER_FILE));
			
			lastCDTV.setText("Call Details Backup :  " + ((float)user.getCallDetailCount() / (float)getCount(CallLog.Calls.CONTENT_URI)) * 100 + "%" );
			Log.d("see............", user.getCallDetailCount() + "  " + getCount(CallLog.Calls.CONTENT_URI));
			lastSMSTV.setText("SMS Backup :  " + (((float)user.getSMSCount() / (float)getCount(Uri.parse("content://sms/"))) * 100) + "%");
			contactBackupPercent.setText("Contact Backup :  " + ((float)user.getContactCount() /(float) getCount(ContactsContract.Contacts.CONTENT_URI)) * 100 + "%");
			
			
		} catch (Exception e) {
		}
		
		t2.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
			    Intent intent = new Intent(getApplicationContext(), MobacWebView.class);
			    startActivity(intent);
			  }
	 
			});
		
		startService(new Intent(this, MobacService.class));
		

		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(this, MobacService.class);
		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		// Start service every hour
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				300 * 1000, pintent);

	}

	public Object loadSerializedObject(File f) throws StreamCorruptedException,
		FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		Object o = ois.readObject();
		return o;

	}
	
	public int getCount(Uri contentUri){
		int count = -1;
		Cursor c = getContentResolver().query(contentUri, null, null, null, null);
		count = c.getCount();
		c.close();
		return count;
	}
	
}

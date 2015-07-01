package com.dpower4.mobac;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;

import com.dpower4.mobac.models.SMSData;
import com.dpower4.mobac.utils.MobacApiClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {
    
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
     
    public void onReceive(Context context, Intent intent) {
     
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
 
        try {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
 

    				SMSData sms = 
    						new SMSData(senderNum,
    						message,
    						"INBOX",
    						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
    						0
    						);
    				
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    
                    /*MobacApiClient mobac = new MobacApiClient("api.mobac.dpower4.com","v0");
    				try {
    					mobac.api("messages", "POST", null, sms.toJsonString());
    				} catch (JSONException e) {
    					Log.d("Api Exception" , e.toString());
    				} catch (Exception e) {
    					Log.d("Api Exception" , e.toString());
    				}
 */
                   // Show Alert
                    /*int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                                 "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();
                    */ 
                } // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
    }   
}

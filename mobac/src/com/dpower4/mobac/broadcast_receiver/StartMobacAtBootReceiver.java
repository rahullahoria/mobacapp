package com.dpower4.mobac.broadcast_receiver;

import java.util.Calendar;

import com.dpower4.mobac.services.MobacService;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.content.Context;

public class StartMobacAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, MobacService.class);
            context.startService(serviceIntent);
                  
            Calendar cal = Calendar.getInstance();
            
            PendingIntent pintent = PendingIntent.getService(context, 0, serviceIntent, 0);
 
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // Start service every hour
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                    300*1000, pintent);
        }
    }
}

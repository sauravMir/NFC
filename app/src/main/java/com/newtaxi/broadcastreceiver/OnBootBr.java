package com.newtaxi.broadcastreceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.learn2crack.nfc.MainActivity;
import com.newtaxi.services.SyncService;

import java.util.Calendar;

public class OnBootBr extends BroadcastReceiver {

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    final String Pref="pref";
    final String Mode="mode";
    @Override
    public void onReceive(Context context, Intent intent) {
        sp=context.getSharedPreferences(Pref,0);
        ed=sp.edit();

        int mode=sp.getInt(Mode,0);
        if(mode==0)
            resetServiceAlarm(context);
    }



    void removeOldAlarm(Context ctx){
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(ctx, SyncService.class);
        PendingIntent pendingIntent = PendingIntent.getService(ctx, MainActivity.ServiceCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    public void resetServiceAlarm(Context ctx){
        AlarmManager alarmManager = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, SyncService.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getService(ctx, MainActivity.ServiceCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        long l=System.currentTimeMillis();
        alarmManager.setRepeating(AlarmManager.RTC, l+MainActivity.restartServiceMiliSec,
                MainActivity.restartServiceMiliSec,
                pendingIntent);

        //save it in shared pref

        ed.putInt(Mode,1);
        ed.commit();
    }
}

package com.newtaxi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.newtaxi.services.PostService;

public class UpdateBR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in=new Intent(context, PostService.class);
        context.startService(in);
    }
}

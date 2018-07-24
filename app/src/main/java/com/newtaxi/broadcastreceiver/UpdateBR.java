package com.newtaxi.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.newtaxi.services.PostService;

public class UpdateBR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in=new Intent(context, PostService.class);
        context.startService(in);

        makeToast("InBR",context);
    }

    void makeToast(String str, Context ctx){
        Toast.makeText(ctx,str, Toast.LENGTH_LONG).show();
    }
}

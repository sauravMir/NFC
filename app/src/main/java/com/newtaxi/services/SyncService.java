package com.newtaxi.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.learn2crack.nfc.AppController;
import com.learn2crack.nfc.db.DaoSession;
import com.learn2crack.nfc.db.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SyncService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DaoSession sessionDao=((AppController) getApplication()).getDaoSession();

        return Service.START_STICKY;
    }



    //////// NETWORK CALLS ////////////////////////
    public void syncproject(DaoSession sessionDao){
        String url = "http://hpmd.cayaconstructs.com/data/sync_data";

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>()  {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONArray!
                        if (response != null && response.length()>0){

                            for (int i = 0; i < response.length(); i++) {

                                try {
                                    JSONObject jobj= (JSONObject) response.get(i);

                                    //response = response.getJSONObject("args");
                                    String phone = jobj.getString("phone_number"),
                                            nfc_id = jobj.getString("nfc_id");
                                    int rideLeft= jobj.getInt("ride_left");

                                    User s = new User();

                                    Log.e("Site: " + phone + "\nNetwork: " + nfc_id, "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }
}

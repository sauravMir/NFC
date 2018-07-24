package com.newtaxi.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learn2crack.nfc.AppController;
import com.learn2crack.nfc.db.DaoSession;
import com.learn2crack.nfc.db.Scheduler;
import com.learn2crack.nfc.db.SchedulerDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.json.JSONObject;

public class PostService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PostService(String name) {
        super(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DaoSession sessionDao=((AppController) getApplication()).getDaoSession();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    //////////// NETWORL CALL///////////////////
    public void bookingSync( String nfcId, String phone, String type){
        String url = "http://hpmd.cayaconstructs.com/data/booking?nfc_id="+nfcId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONArray!
                        if (response != null){
                            //makeToast("Booking Successful");
                        }else{
                            //makeToast("Could not register");
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


    //delete all completed rows
    public void deleteCompleatedRow(DaoSession daoSession){
        final DeleteQuery<Scheduler> tableDeleteQuery = daoSession.queryBuilder(Scheduler.class)
                .where(SchedulerDao.Properties.Progress.eq(2))
                .buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
    }


}

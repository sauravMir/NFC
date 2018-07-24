package com.newtaxi.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learn2crack.nfc.AppController;
import com.learn2crack.nfc.db.DaoSession;
import com.learn2crack.nfc.db.Scheduler;
import com.learn2crack.nfc.db.SchedulerDao;
import com.newtaxi.NetworkConnectionTest;

import org.greenrobot.greendao.query.DeleteQuery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PostService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  name Used to name the worker thread, important only for debugging.
     */
    public PostService() {
        super("POST SErvice");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DaoSession sessionDao=((AppController) getApplication()).getDaoSession();
        Log.e("In handle","Hnadle");
        if(NetworkConnectionTest.isNetworkConnected(this)){
            Scheduler schedule=new Scheduler();

            //get all incomplete schedules
            List<Scheduler> incompleteList=schedule.getIncompletedShedules(sessionDao);
            if(incompleteList!=null) {
                for (Scheduler sc : incompleteList) {
                    sc.setProgress(1);
                    sc.updateScheduleItem(sc, sessionDao);
                }
                if(!incompleteList.isEmpty())
                bookingSync(incompleteList, sessionDao);
            }
            makeToast("INPOSTSERV",this);
        }
    }

    void makeToast(String str, Context ctx){
        Toast.makeText(ctx,str, Toast.LENGTH_LONG).show();
    }
    public void onCreate() {
        super.onCreate();
        Log.d("Server", ">>>onCreate()");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        return START_STICKY;
    }

    //////////// NETWORL CALL///////////////////
    public void bookingSync( List<Scheduler> incompleteList,DaoSession sessionDao){
        String url = "hpmd.cayaconstructs.com/data/bulk_requests";
        JSONArray array=new JSONArray();
        for(Scheduler sc: incompleteList){
            JSONObject jObj=new JSONObject();
            try {
                jObj.put("request_type",sc.getType());
                jObj.put("nfc_id",sc.getNfc_id());
                jObj.put("phone_number",sc.getPhone_number());

                array.put(jObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONArray>()  {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONArray!

                        if (response != null){
                            //makeToast("Booking Successful");
                            Log.e("Response: ", response.toString());
                            for(Scheduler sc: incompleteList){
                                sc.setProgress(2);
                                sc.updateScheduleItem(sc,sessionDao);
                            }

                            //now delete olders
                            deleteCompleatedRow(sessionDao);
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

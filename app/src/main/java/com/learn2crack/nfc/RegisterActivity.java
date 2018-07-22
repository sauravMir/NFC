package com.learn2crack.nfc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.learn2crack.nfc.db.DaoSession;
import com.learn2crack.nfc.db.User;
import com.learn2crack.nfc.db.UserDao;
import com.newtaxi.NetworkConnectionTest;
import com.newtaxi.util.RequestPermissionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends Activity {

    EditText et_phone;
    String firstFour="6789";
    String nfcId="";
    Button submit_bt;
    public static int RideLeft_MAX=30;
    DaoSession sessionDao;
    ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);
        nfcId=getIntent().getStringExtra("nfc_id");
        submit_bt=(Button) findViewById(R.id.submit_bt);
        submit_bt.setOnClickListener(view->submitPressed(view));
        et_phone=(EditText) findViewById(R.id.editTextPhone);

        //setTitle("Registration");
        //set

        sessionDao=((AppController) getApplication()).getDaoSession();
        pd=new ProgressDialog(this);
        pd.setTitle("Please Wait...");
        pd.setCancelable(false);

    }

    boolean validityCheck(){
         if(et_phone.getText().toString().startsWith(firstFour)){
            if(et_phone.getText().toString().length()==10)
                return true;
        }
        return false;
    }

    void submitPressed(View v){
        if(validityCheck()){
            //call volley
            if(NetworkConnectionTest.isNetworkConnected(RegisterActivity.this)){
                pd.show();
                syncproject(et_phone.getText().toString(), nfcId);
            }else{
                makeToast("No Internet Connection");
            }
        }else{
            makeToast("Phone number not Valid");
        }
    }

    //////// NETWORK CALLS ////////////////////////
    public void syncproject(String phone, String nfcId){
        String url = "http://hpmd.cayaconstructs.com/data/registration?phone_number="+phone+"&nfc_id="+nfcId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONArray!
                        if (response != null){
                            makeToast("Successfully registered");

                            UserDao ud=sessionDao.getUserDao();
                            User u= new User();
                            u.insertItem(nfcId,phone, RideLeft_MAX,sessionDao);


                            //send for registration
                            Intent in=new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();

                        }else{
                            makeToast("Could not register");
                        }

                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        pd.dismiss();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    void makeToast(String str){
        Toast.makeText(this,str, Toast.LENGTH_LONG).show();
    }
}



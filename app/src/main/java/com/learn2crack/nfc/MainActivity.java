package com.learn2crack.nfc;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.newtaxi.services.SyncService;
import com.newtaxi.util.RequestPermissionHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Listener {
    
    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEtMessage;
    private Button mBtWrite;
    private Button mBtRead;

    private NFCWriteFragment mNfcWriteFragment;
    private NFCReadFragment mNfcReadFragment;

    private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter mNfcAdapter;
    DaoSession sessionDao;
    String[] permission= {"android.permission.NFC","android.permission.ACCESS_NETWORK_STATE","android.permission.INTERNET",
            "android.permission.RECEIVE_BOOT_COMPLETED","android.permission.ACCESS_WIFI_STATE"};

    RequestPermissionHandler reqperm;
    boolean blockUI_ifNoPer=true;
    ProgressDialog pd;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    final String Pref="pref";
    final String Mode="mode";
    RelativeLayout rl;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl=(RelativeLayout) findViewById(R.id.activity_main);
        t=(TextView) findViewById(R.id.textView);

        rl.setOnClickListener(view->handlePermission());
        sessionDao=((AppController) getApplication()).getDaoSession();

        reqperm=new RequestPermissionHandler();

        handlePermission();

        sp=getSharedPreferences(Pref,0);
        ed=sp.edit();

        int mode=sp.getInt(Mode,0);
        if(mode==0)
        resetServiceAlarm();


        pd=new ProgressDialog(this);
        pd.setTitle("Please Wait...");
        pd.setCancelable(false);

//        UserDao ud=sessionDao.getUserDao();
//        List<User> items = ud.queryRaw("where nfc_id=?","1101");
//
//        //List<User> listATableObj = ud.queryRawCreate(", BTable BT WHERE BT.nameid = T.nameid").list();
//
//        for(User u: items){
//            Toast.makeText(this, u.getPhone_number(),Toast.LENGTH_LONG).show();
//        }
//
//
//        User u= new User();
//        u.insertItem("12321","989890", 30,sessionDao);

        initNFC();
    }


    private void initNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }


    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;
        isWrite = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        //Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_ID);

        Log.d(TAG, "onNewIntent: "+intent.getAction());
       // String s="";
//        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
//            if(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)!=null)
//             s+="\n ID: "+ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
//            else
//                s+="\n ID :: NuLL";
//            t.setText(s);
//        }

//        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
//            if(intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG) !=null)
//            s+="\n Tag"+ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_TAG));
//            else
//                s+="\n Tag :: NuLL";
//            t.setText(s);
//        }

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            if (intent.getByteArrayExtra(NfcAdapter.EXTRA_ID) != null)
            {
                String ss= ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                if(!blockUI_ifNoPer) {
                        readFromNFC(ss);
                }
                else
                    makeToast("Please Grant All Permissions.");
            }
        }

        }


            //////////// NETWORL CALL///////////////////
    public void bookingSync( String nfcId){
        String url = "http://hpmd.cayaconstructs.com/data/booking?nfc_id="+nfcId;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()  {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONArray!
                        if (response != null){
                            makeToast("Booking Successful");
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


    protected void startNfcSettingsActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        } else {
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    //// PERMISSION HANDLING /////////////////////////////////
    private void handlePermission(){
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdpt!=null) {
            if (nfcAdpt.isEnabled()) {
                //Nfc settings are enabled
            } else {
                startNfcSettingsActivity();
            }
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            reqperm.requestPermission(this,
                    permission
                    , 123, new RequestPermissionHandler.RequestPermissionListener() {
                        @Override
                        public void onSuccess() {
                            //Toast.makeText(MainActivity.this, "request permission success", Toast.LENGTH_SHORT).show();
                            blockUI_ifNoPer = false;
                        }

                        @Override
                        public void onFailed() {
                            Toast.makeText(MainActivity.this, "Please Give All The Permissions For Using The App", Toast.LENGTH_SHORT).show();
                            blockUI_ifNoPer = true;
                        }
                    });
        }else{
            blockUI_ifNoPer = false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        reqperm.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    ///////////// NFC /////////////
    private void readFromNFC(String str) {
        String Nfcmessage=str;

       /*     ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            if(ndefMessage!=null) {
                byte[] payload=ndefMessage.getRecords()[0].getPayload();
                String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
                int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
                // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

                try {
                    // Get the Text
                    Nfcmessage = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                } catch (UnsupportedEncodingException e) {
                    Log.e("UnsupportedEncoding", e.toString());
                }
                //Nfcmessage = new String(ndefMessage.getRecords()[0].getPayload());

            }
            */
            Log.d(TAG, "readFromNFC: "+Nfcmessage);

            //validation
            try{
                //long nfc=Long.parseLong(Nfcmessage);
                User u=new User();
                u=u.isValidNFC(Nfcmessage, sessionDao);
                if(u!=null){
                    //the tag is in db so deduct and update
                    if(NetworkConnectionTest.isNetworkConnected(this)){
                        if(u.getRide_left()>0){
                            u.setRide_left(u.getRide_left()-1);
                            u.updateItem(u,sessionDao);
                            pd.show();
                            makeToast("Rides Left: "+String.valueOf(u.getRide_left()));
                            bookingSync(u.getNfc_id());
                        }else{
                            makeToast("Please Recharge No Rides Left");
                        }

                    }else{
                        makeToast("No Internet Connection");
                    }


                }else{
                    //send for registration
                    Intent in=new Intent(this, RegisterActivity.class);
                    in.putExtra("nfc_id",Nfcmessage);
                    startActivity(in);
                    finish();
                }

            }catch (NumberFormatException e){
                e.printStackTrace();
                makeToast("Wrong NFC Tag");
            }
            //ndef.close();


    }

    ////////////// ALRM SET UP ////////////////

    public static int restartServiceMiliSec=900000;
    public static int restartBRMiliSec=60000;
    public static int ServiceCode=1000;
    public static int BroadcastCode=1001;

    void removeOldAlarm(){
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, SyncService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, ServiceCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    public void resetServiceAlarm(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SyncService.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getService(this, ServiceCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);



        long l=System.currentTimeMillis();
        alarmManager.setRepeating(AlarmManager.RTC, l+restartServiceMiliSec,restartServiceMiliSec,
                    pendingIntent);

        //////////*********** For 1min PostBR ******////////////
        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent2 = new Intent(this, SyncService.class);
        PendingIntent pendingIntent2;

        pendingIntent2 = PendingIntent.getBroadcast(this, BroadcastCode, intent2, PendingIntent.FLAG_UPDATE_CURRENT);



        long ll=System.currentTimeMillis();
        alarmManager2.setRepeating(AlarmManager.RTC, ll+restartBRMiliSec,restartBRMiliSec,
                pendingIntent2);


        //save it in shared pref
        ed.putInt(Mode,1);
        ed.commit();
    }


    void makeToast(String str){
        Toast.makeText(this,str, Toast.LENGTH_LONG).show();
    }
}

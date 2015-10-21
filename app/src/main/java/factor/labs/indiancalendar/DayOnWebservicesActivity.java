package factor.labs.indiancalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import factor.labs.indiancalendar.CalendarWebServices.DBController;
import factor.labs.indiancalendar.CalendarWebServices.DayOnBroadCastReceiver;

public class DayOnWebservicesActivity extends AppCompatActivity {

    // DB Class to perform DB related operations

    DBController controller = new DBController(this);

    TextView webSampletxtview;
    // Progress Dialog Object

    ProgressDialog prgDialog;

    HashMap<String, String> queryValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_on_webservices);

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setCancelable(false);

        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), DayOnBroadCastReceiver.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 3600 * 1000, pendingIntent);
        final Button syncbutton = (Button) findViewById(R.id.sync_button11);

        syncbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                syncSQLiteMySQLDB();
            }
        });

    }
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php

        client.post("http://ahamedtech2.site90.net/getevents.php?lastmodified=1445074312", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Need to update in db from getevents.php
                updateSQLite(response);
            }

            // When error occured
            @Override


            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {

                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {

                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public void updateSQLite(String response) {
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        webSampletxtview=(TextView)findViewById(R.id.WebserviceTxtview);
       webSampletxtview.setText(response);
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    //System.out.println(obj.get("userId"));
                    //System.out.println(obj.get("userName"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("userId", obj.get("eventId").toString());
                    // Add userName extracted from Object
                    queryValues.put("userName", obj.get("eventName").toString());
                    // Insert User into SQLite DB
                    controller.insertUser(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("eventId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                //updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
               //reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }

    // Reload MainActivity
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), DayOnWebservicesActivity.class);
        startActivity(objIntent);
    }
}

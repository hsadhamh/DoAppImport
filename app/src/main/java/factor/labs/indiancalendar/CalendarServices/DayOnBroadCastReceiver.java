package factor.labs.indiancalendar.CalendarServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class DayOnBroadCastReceiver extends BroadcastReceiver {

    public DayOnBroadCastReceiver() {}

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Toast.makeText(context, "Sync message received", Toast.LENGTH_SHORT).show();
        boolean bSyncData = intent.getBooleanExtra("SyncData", false);
        if(bSyncData) {
            // Start an Async task for running find the count.
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
            client.post("http://ahamedtech2.site90.net/getdbrowcount.php?lastmodified=1445074312", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = responseBody == null ? null : new String(responseBody, getCharset());
                        if(response != null) {
                            // Create JSON object out of the response sent by getdbrowcount.php
                            JSONObject obj = new JSONObject(response);
                            System.out.println(obj.get("count"));
                            // If the count value is not zero, call MyService to display notification
                            if (obj.getInt("count") != 0) {
                                final Intent intnt = new Intent(context, DayOnAndroidService.class);
                                // Set unsynced count in intent data
                                intnt.putExtra("TaskType", 1);
                                intnt.putExtra("CountSync", obj.get("count").toString());
                                // Call MyService
                                context.startService(intnt);
                            }
                        }
                    } catch (UnsupportedEncodingException e) {
                        onFailure(statusCode, headers, e, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onFailure(statusCode, headers, e, null);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    // TODO Auto-generated method stub
                    try {
                        String response = responseBody == null ? null : new String(responseBody, getCharset());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (statusCode == 404) {
                        Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(context, "500", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error occured!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


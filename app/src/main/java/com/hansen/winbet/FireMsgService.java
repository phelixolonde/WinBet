package com.hansen.winbet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FireMsgService extends FirebaseMessagingService {


    private static final String TAG = "VOLLEY ERROR";
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
    DatabaseReference nRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "created", Toast.LENGTH_SHORT).show();
        getFeed("http://wangchieng.000webhostapp.com/winbet/newsfeed.php");

    }

    public void getFeed(String url) {
        String tag_json_obj = "json_obj_req";
        Long tsLong = 1 - System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        nRef = dbref.child("newsfeed").child(ts);


        //getting the request object
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        if (response != null) {

                            JSONObject jsonObject;

                            try {
                                jsonObject = new JSONObject(response.toString());

                                JSONObject objectChannel = (JSONObject) jsonObject.get("channel");
                                JSONArray arrayItems = (JSONArray) objectChannel.get("item");

                                for (int i = 0; i < arrayItems.length(); i++) {

                                    String title = arrayItems.getJSONObject(i).getString("title");
                                    String description = arrayItems.getJSONObject(i).getString("description");
                                    String link = arrayItems.getJSONObject(i).getString("link");
                                    String pubDate = arrayItems.getJSONObject(i).getString("pubDate");
                                    //String author = arrayItems.getJSONObject(i).getString("author");
//                                    String image = arrayItems.getJSONObject(i).getString("media:thumbnail");

                                    //Toast.makeText(getContext(), "title= " + arrayItems.getJSONObject(i).getString("media"), Toast.LENGTH_SHORT).show();

                                    nRef.child("title").setValue(title);
                                    nRef.child("description").setValue(description);
                                    nRef.child("link").setValue(link);
                                    nRef.child("pubDate").setValue(pubDate);
                                    //nRef.child("author").setValue(author);
                                    //nRef.child("image").setValue(image);


                                }

                            } catch (JSONException e) {
                                Log.e("DECODING", "JSON EXCEPTION", e);
                            }

                            //repoList.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        WinBet.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received [" + remoteMessage + "]");

        // Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("New Post")
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
    }
}

package com.hansen.winbet;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fragment_News extends Fragment {
    private static final String TAG = "VOLLEY";
    RecyclerView recyclerView;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
    TextView loading, result;
    SharedPreferences sp;
    View v;
    SwipeRefreshLayout refresher;
    TextView feedTitle, feedDesc, feedLink;
    ProgressDialog pDialog;
    DatabaseReference nRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);


        /*recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);*/

        result = (TextView) v.findViewById(R.id.txtResult);
        /*
        feedDesc= (TextView) v.findViewById(R.id.feedDesc);
        feedLink= (TextView) v.findViewById(R.id.feedLink);*/


       /* final NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });*/
        loading = (TextView) v.findViewById(R.id.loading);
        dbref.keepSynced(true);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.edit().clear().apply();


        refresher = (SwipeRefreshLayout) v.findViewById(R.id.refresher);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });

        //getActivity().setTitle("TIPS ("+read.size()+")");
//\\getActivity().getActionBar().setTitle("TIPS ("+read.size()+")");


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        getFeed("http://wangchieng.000webhostapp.com/winbet/newsfeed.php");
    }

    public void getFeed(String url) {
        String tag_json_obj = "json_obj_req";
        Long tsLong = 1 - System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        nRef = dbref.child("newsfeed").child(ts);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        //getting the request object
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();


                        if (response != null) {
                            result.setText(response.toString());

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

                                    Toast.makeText(getContext(), "title= " + arrayItems.getJSONObject(i).getString("title"), Toast.LENGTH_SHORT).show();

                                    /*nRef.child("title").setValue(title);
                                    nRef.child("description").setValue(description);
                                    nRef.child("link").setValue(link);
                                    nRef.child("pubDate").setValue(pubDate);
                                    nRef.child("author").setValue(author);
                                    nRef.child("image").setValue(image);*/


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
                pDialog.hide();
            }
        });

        WinBet.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}

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

    public Fragment_News() {

    }

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
    }


}

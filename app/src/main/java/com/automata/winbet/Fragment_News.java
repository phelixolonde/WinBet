package com.automata.winbet;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_News extends Fragment {
    private static final String TAG = "WINBET";
    RecyclerView recyclerView;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
    TextView loading;

    View v;
    SwipeRefreshLayout refresher;
    ProgressDialog pDialog;
    DatabaseReference nRef;
    private String urlJsonObj = "https://newsapi.org/v1/articles?source=bbc-sport&apiKey=100c60eefc1a493c9ff9e0bada164f66";
    String title, description, image, url, time;
    List<NewsModel> data=new ArrayList<>();
    private AdapterNews mAdapter;

    private AdView mBannerAd;

    public Fragment_News() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);

        getNewsFeed();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_news);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(lm);


        mBannerAd = (AdView) v.findViewById(R.id.banner_AdView);
        showBannerAd();




        loading = (TextView) v.findViewById(R.id.loading);
        dbref.keepSynced(true);



       refresher = (SwipeRefreshLayout) v.findViewById(R.id.refresher);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });



        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
        getNewsFeed();


    }
    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

    }

    public void getNewsFeed() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray array = response.getJSONArray("articles");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject article = (JSONObject) array.get(i);
                        NewsModel newsModel=new NewsModel();
                        newsModel.title = article.getString("title");
                        newsModel.description = article.getString("description");
                        newsModel.url=article.getString("url");
                        newsModel.time="moments ago";
                        data.add(newsModel);


                    }
                    mAdapter=new AdapterNews(getContext(),data);
                    recyclerView.setAdapter(mAdapter);

                } catch (JSONException e) {
                    Log.e("WINBET",e.getMessage(),e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        WinBet.getInstance().addToRequestQueue(jsonObjReq);
    }
}



package com.automata.winbet;


import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment_News extends Fragment {
    private static final String TAG = "WINBET_NEWS";
    RecyclerView recyclerView;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
    TextView loading;

    View v;
    SwipeRefreshLayout refresher;

    // String urlJsonObj = "https://newsapi.org/v1/articles?source=bbc-sport&apiKey=100c60eefc1a493c9ff9e0bada164f66";
    String urlJsonObj = "https://skysportsapi.herokuapp.com/sky/getnews/football/v1.0/";
    String title, description, image, time;
    List<NewsModel> data = new ArrayList<>();
    private AdapterNews mAdapter;
    private AdView mBannerAd;


    public Fragment_News() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);

        getNewsFeed();
        recyclerView = v.findViewById(R.id.recycler_news);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(lm);
        loading = v.findViewById(R.id.txtLoading);
        dbref.keepSynced(true);
        refresher = v.findViewById(R.id.refresher);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });


        mBannerAd =  v.findViewById(R.id.banner_AdView);
        showBannerAd();


        return v;
    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

    }
    @Override
    public void onStart() {
        super.onStart();
        getNewsFeed();


    }


    public void getNewsFeed() {

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i("NEWS_RESPONSE", response.toString());

                loading.setVisibility(View.GONE);

                try {
                    JSONArray array = new JSONArray(response.toString());

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject article = (JSONObject) array.get(i);
                        NewsModel newsModel = new NewsModel();
                        newsModel.title = article.getString("title");
                        newsModel.description = article.getString("shortdesc");
                        newsModel.url = article.getString("link");
                        newsModel.time = "moments ago";
                        newsModel.image=article.getString("imgsrc");
                        data.add(newsModel);


                    }
                    mAdapter = new AdapterNews(getContext(), data);
                    recyclerView.setAdapter(mAdapter);

                } catch (Exception e) {
                    Log.e("WINBET", e.getMessage(), e);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                loading.setText("Could  not fetch news at this time. Kindly, try again later");
                Log.e("NEWS_RESPONSE", error.toString());
            }
        });

        WinBet.getInstance().addToRequestQueue(jsonObjReq);
    }
}



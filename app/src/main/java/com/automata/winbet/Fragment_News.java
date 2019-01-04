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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
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


    NativeBannerAd nativeBannerAd;
    RelativeLayout nativeBannerAdContainer;
    LinearLayout adView;


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


        nativeBannerAd = new NativeBannerAd(getContext(), "316921022146803_395182830987288");
        // load the ad
        nativeBannerAd.loadAd();

        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());

            }

            @Override
            public void onAdLoaded(Ad ad) {

                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }

                try {
                    // Inflate Native Banner Ad into Container
                    inflateAd(nativeBannerAd);
                }catch (Exception ignored){

                }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });



        return v;
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeBannerAdContainer = v.findViewById(R.id.native_banner_ad_container);
        nativeBannerAdContainer.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_unit, nativeBannerAdContainer, false);
        nativeBannerAdContainer.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(getContext(), nativeBannerAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
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



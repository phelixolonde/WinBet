package com.hansen.winbet;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


public class FragmentStandings extends Fragment {
    WebView webView;
    SwipeRefreshLayout refresher;
    private InterstitialAd mInterstitialAd;

    View v;
    String[] leagues = {"Barclays Premier League", "Spanish La Liga", "German Bundesliga", "French Ligue 1", "Italian Serie A"
            , "MLS", "UEFA Champions League", "UEFA Europa League", "England Championship", "England Ligue 1",
            "England Ligue 2", "Belgium Jupiler League", "Brazil Serie A", "Holland Eredivisie", "Portugal Primera Liga", "Turkish Super Lig"};

    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_h2h, container, false);

        listView = (ListView) v.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, leagues);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),Detailed.class);
                intent.putExtra("title",listView.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

        mInterstitialAd = createNewIntAd();


        // getSupportActionBar().setTitle(title);
        loadIntAdd();
        showIntAdd();
        return v;
    }

    private InterstitialAd createNewIntAd() {
        InterstitialAd intAd = new InterstitialAd(getContext());
        // set the adUnitId (defined in values/strings.xml)
        intAd.setAdUnitId(getString(R.string.interstitial));
        intAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdClosed() {
            }
        });
        return intAd;
    }

    private void loadIntAdd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showIntAdd() {

        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}

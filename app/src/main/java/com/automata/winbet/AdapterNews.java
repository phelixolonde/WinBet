package com.automata.winbet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<NewsModel> data;
    private InterstitialAd mInterstitialAd;

    public AdapterNews(Context context, List<NewsModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_row, parent, false);
        MyHolder holder = new MyHolder(view);

        mInterstitialAd = createNewIntAd();

        loadIntAdd();

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        final NewsModel current = data.get(position);

        myHolder.newsTitle.setText(current.title);
        myHolder.newsTime.setText(current.time);
        myHolder.newsDesc.setText(current.description);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        showIntAdd();
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Intent intent = new Intent(context, News_Detailed.class);
                        intent.putExtra("url", current.url);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onAdClosed() {
                        // Proceed to the next level.
                        Intent intent = new Intent(context, News_Detailed.class);
                        intent.putExtra("url", current.url);
                        context.startActivity(intent);
                    }
                });

            }
        });


    }

    private InterstitialAd createNewIntAd() {
        InterstitialAd intAd = new InterstitialAd(context);
        // set the adUnitId (defined in values/strings.xml)
        intAd.setAdUnitId(context.getString(R.string.interstitial));

        return intAd;
    }

    private void loadIntAdd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showIntAdd() {

// Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();

    }


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;
        TextView newsTime, newsDesc;


        public MyHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
            newsTime = (TextView) itemView.findViewById(R.id.newsTime);
            newsDesc = (TextView) itemView.findViewById(R.id.newsDesc);
        }

    }

}
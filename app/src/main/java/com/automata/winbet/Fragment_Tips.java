package com.automata.winbet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Tips extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet1");
    TextView loading;
    View v;
    SwipeRefreshLayout refresher;


    static SQLiteDatabase db;
    LinearLayoutManager lm;
    InterstitialAd mInterstitialAd;

    NativeBannerAd nativeBannerAd;
    RelativeLayout nativeBannerAdContainer;
    LinearLayout adView;
    private static final String TAG = "FACEBOOK_ADS";

    public Fragment_Tips() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_home, container, false);


        try {
            db = getActivity().openOrCreateDatabase("reads", MODE_PRIVATE, null);
            db.execSQL("create table if not exists table_read(ids varchar)");
        } catch (Exception e) {
            e.printStackTrace();
        }


        loading = v.findViewById(R.id.loading);
        // dbref.keepSynced(true);


        refresher = v.findViewById(R.id.refresher);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onStart();
            }
        });


        mInterstitialAd = createNewIntAd();
        loadIntAdd();

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
                } catch (Exception ignored) {

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
        recyclerView = v.findViewById(R.id.recycler);
        //recyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);

        refresher.setRefreshing(true);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                displayData();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                displayData();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });


    }

    private InterstitialAd createNewIntAd() {
        InterstitialAd intAd = new InterstitialAd(getContext());
        // set the adUnitId (defined in values/strings.xml)
        intAd.setAdUnitId(getString(R.string.interstitial));

        return intAd;
    }

    private void loadIntAdd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void displayData() {
        final FirebaseRecyclerAdapter<Model, MyViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(
                Model.class,
                R.layout.row,
                MyViewHolder.class,
                dbref

        ) {

            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Model model, int position) {
                final String postKey = getRef(position).getKey();


                viewHolder.setTitle(model.getTitle(), postKey);
                viewHolder.setBody(model.getBody());
                viewHolder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                refresher.setRefreshing(false);
                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent detailedIntent = new Intent(getActivity(), PostDetailed.class);
                        detailedIntent.putExtra("postKey", postKey);
                        startActivity(detailedIntent);

                    }
                });
            }

        };

        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View v;

        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;

        }

        void setTitle(String title, String post_key) {
            TextView postTitle = v.findViewById(R.id.postTitle);
            postTitle.setText(title.toUpperCase());
            try {
                String s = "select * from table_read";
                Cursor c = db.rawQuery(s, null);
                if (c != null && c.moveToFirst()) {
                    do {


                        String ids = c.getString(c.getColumnIndex("ids"));

                        if (ids.equals(post_key)) {
                            postTitle.setTextColor(ContextCompat.getColor(v.getContext(), R.color.grey));

                        }


                    } while (c.moveToNext());
                    c.close();
                }
            }catch (Exception ignored){

            }
        }

        public void setBody(String postBody) {
            TextView post = v.findViewById(R.id.post);
            post.setText(postBody);


        }

        public void setTime(Long time) {
            TextView txtTime = v.findViewById(R.id.postTime);
            //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
            try {
                long elapsedTime;
                long currentTime = System.currentTimeMillis();
                int elapsed = (int) ((currentTime - time) / 1000);
                if (elapsed < 60) {
                    if (elapsed < 2) {
                        txtTime.setText("Just Now");
                    } else {
                        txtTime.setText(elapsed + " sec ago");
                    }
                } else if (elapsed > 604799) {
                    elapsedTime = elapsed / 604800;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " week ago");
                    } else {

                        txtTime.setText(elapsedTime + " weeks ago");
                    }
                } else if (elapsed > 86399) {
                    elapsedTime = elapsed / 86400;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " day ago");
                    } else {
                        txtTime.setText(elapsedTime + " days ago");
                    }
                } else if (elapsed > 3599) {
                    elapsedTime = elapsed / 3600;
                    if (elapsedTime == 1) {
                        txtTime.setText(elapsedTime + " hour ago");
                    } else {
                        txtTime.setText(elapsedTime + " hours ago");
                    }
                } else if (elapsed > 59) {
                    elapsedTime = elapsed / 60;
                    txtTime.setText(elapsedTime + " min ago");


                }

            } catch (Exception e) {
                txtTime.setText("Moments ago");
            }
        }
    }
}

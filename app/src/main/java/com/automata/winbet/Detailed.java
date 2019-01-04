package com.automata.winbet;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

public class Detailed extends AppCompatActivity {

    String title;
    WebView webView;

    ProgressBar progressBar;
    NativeBannerAd nativeBannerAd;
    private RelativeLayout nativeBannerAdContainer;
    private LinearLayout adView;
    private static final String TAG ="FACEBOOK_ADS" ;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standing_detailed);

        title = getIntent().getExtras().getString("title");

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(100);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
            try {
                getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back));
            }catch (Exception ignored){

            }
        }


        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClientDemo());
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new myWebChrome());

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            LoadPage(title);

        } else {
            Toast.makeText(this, "Network problem, please reload the page", Toast.LENGTH_SHORT).show();
        }

        nativeBannerAd = new NativeBannerAd(Detailed.this, "316921022146803_395182830987288");
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
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeBannerAdContainer = findViewById(R.id.native_banner_ad_container);
        nativeBannerAdContainer.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(Detailed.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_unit, nativeBannerAdContainer, false);
        nativeBannerAdContainer.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(Detailed.this, nativeBannerAd, true);
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


    private void LoadPage(String title) {
        if (title.equalsIgnoreCase("Barclays Premier League")) {
            webView.loadUrl("https://www.bbc.com/sport/football/premier-league/table");
        } else if (title.equalsIgnoreCase("Spanish La liga")) {
            webView.loadUrl("https://www.bbc.com/sport/football/spanish-la-liga/table");
        } else if (title.equalsIgnoreCase("German Bundesliga")) {
            webView.loadUrl("https://www.bbc.com/sport/football/german-bundesliga/table");
        } else if (title.equalsIgnoreCase("French Ligue 1")) {
            webView.loadUrl("https://www.bbc.com/sport/football/french-ligue-one/table");
        } else if (title.equalsIgnoreCase("Italian Serie A")) {
            webView.loadUrl("https://www.bbc.com/sport/football/italian-serie-a/table");
        } else if (title.equalsIgnoreCase("MLS")) {
            webView.loadUrl("https://www.bbc.com/sport/football/us-major-league/table");

        } else if (title.equalsIgnoreCase("UEFA Champions League")) {
            webView.loadUrl("https://www.bbc.com/sport/football/champions-league/table");

        } else if (title.equalsIgnoreCase("UEFA Europa League")) {
            webView.loadUrl("https://www.bbc.com/sport/football/europa-league/table");

        } else if (title.equalsIgnoreCase("England Championship")) {
            webView.loadUrl("https://www.bbc.com/sport/football/championship/table");

        } else if (title.equalsIgnoreCase("England League 1")) {
            webView.loadUrl("https://www.bbc.com/sport/football/league-one/table");

        } else if (title.equalsIgnoreCase("England League 2")) {
            webView.loadUrl("https://www.bbc.com/sport/football/league-two/table");

        } else if (title.equalsIgnoreCase("Belgium Jupiler League")) {
            webView.loadUrl("https://www.bbc.com/sport/football/belgian-pro-league/table");

        } else if (title.equalsIgnoreCase("Brazil Serie A")) {
            webView.loadUrl("https://www.bbc.com/sport/football/brazilian-league/table");

        } else if (title.equalsIgnoreCase("Holland Eredivisie")) {
            webView.loadUrl("https://www.bbc.com/sport/football/dutch-eredivisie/table");

        } else if (title.equalsIgnoreCase("Portugal Primeira Liga")) {
            webView.loadUrl("https://www.bbc.com/sport/football/portuguese-primeira-liga/table");

        }else if (title.equals("Turkish Super Lig")){
            webView.loadUrl("https://www.bbc.com/sport/football/turkish-super-lig/table");

        }
        else if (title.equals("Greek Superleague")){
            webView.loadUrl("https://www.bbc.com/sport/football/greek-superleague/table");

        }
    }


    private class myWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if(newProgress==100){
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class WebViewClientDemo extends WebViewClient {


        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            //progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}

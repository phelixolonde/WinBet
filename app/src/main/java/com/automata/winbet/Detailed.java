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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Detailed extends AppCompatActivity {

    String title;
    WebView webView;

    ProgressBar progressBar;
    private AdView mBannerAd;

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
        mBannerAd =  findViewById(R.id.banner_AdView);
        showBannerAd();




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


    }
    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

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

package com.hansen.winbet;


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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fragment_News extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
    TextView loading;
    SharedPreferences sp;
    View v;
    SwipeRefreshLayout refresher;
    TextView feedTitle,feedDesc,feedLink;

/*    public static String DATABASE_NAME = "reads.db";
    public static String DATABASE_VERSION = "1";
    public static String TABLE_IDS = "ids";
    public static String IDS_COLUMN = "post_id";
    SQLiteDatabase db;*/
private List<RssFeedModel> feedList;
    private String sFeedTitle,sFeedDesc,sFeedLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);

        feedTitle= (TextView) v.findViewById(R.id.feedTitle);
        feedDesc= (TextView) v.findViewById(R.id.feedDesc);
        feedLink= (TextView) v.findViewById(R.id.feedLink);


        final NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });
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
       // new FetchFeed().execute((Void) null);
    }

    private class FetchFeed extends AsyncTask<Void,Void,Boolean> {
        private String urlLink;

        @Override
        protected void onPreExecute() {
            refresher.setRefreshing(true);
            urlLink = "https://xkcd.com/rss.xml";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                feedList = parseFeed(inputStream);
            } catch (MalformedURLException e) {
                Log.e("URLEXCEPTION", "Error", e);
            } catch (IOException e) {
                Log.e("INPUTSTREAMEXCEPTION", "Error", e);

            } catch (XmlPullParserException e1) {
                e1.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            refresher.setRefreshing(false);
            if (aBoolean) {
                feedTitle.append(sFeedTitle);
                feedDesc.append(sFeedDesc);
                feedLink.append(sFeedLink);
                recyclerView.setAdapter(new RssFeedAdapter(feedList));
            } else {
                Toast.makeText(getContext(), "error somewhere", Toast.LENGTH_SHORT).show();
            }

        }}
    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    else {
                        sFeedTitle = title;
                        sFeedLink = link;
                        sFeedDesc = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }
}

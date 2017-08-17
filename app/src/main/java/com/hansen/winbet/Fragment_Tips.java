package com.hansen.winbet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Tips extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference dbref;
    TextView loading;
    Intent serviceIntent;
    View v;
    SwipeRefreshLayout refresher;
    // private AdView mBannerAd;

    static SQLiteDatabase db;
    int seenPosts;
    LinearLayoutManager lm;
    String postKey;
    FirebaseRecyclerAdapter<Model, MyViewHolder> recyclerAdapter;


    public Fragment_Tips() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_home, container, false);
        //mBannerAd = (AdView) v.findViewById(R.id.banner_AdView);

        dbref = FirebaseDatabase.getInstance().getReference().child("winbet");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (recyclerAdapter != null)
                    recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        db = getActivity().openOrCreateDatabase("reads", MODE_PRIVATE, null);
        db.execSQL("create table if not exists table_read(ids varchar)");
        serviceIntent = new Intent(getActivity(), ShorcutService.class);

//checking the number of posts a user has read from sqliteDatabase
        String s = "select * from table_read";
        Cursor c = db.rawQuery(s, null);
        do {
            try {
                seenPosts = c.getCount();
                serviceIntent.putExtra("seenPosts", seenPosts);
                getActivity().startService(serviceIntent);

            } catch (Exception e) {
                Log.e("TABLE ERROR", null, e);
            }
        } while (c.moveToNext());
        c.close();


        //showNativeAd();

        final NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });


        loading = (TextView) v.findViewById(R.id.loading);


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


        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(false);
        lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);

        refresher.setRefreshing(true);
        recyclerAdapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(
                Model.class,
                R.layout.row,
                MyViewHolder.class,
                dbref

        ) {

            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, Model model, int position) {
                postKey = getRef(position).getKey();


                viewHolder.setTitle(model.getTitle(), postKey);
                viewHolder.setBody(model.getBody());
                viewHolder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                refresher.setRefreshing(false);
                viewHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.execSQL("insert into table_read values('" + postKey + "')");

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

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerAdapter.cleanup();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View v;


        public MyViewHolder(View itemView) {
            super(itemView);
            v = itemView;
        }

        public void setTitle(String title, String post_key) {
            TextView postTitle = (TextView) v.findViewById(R.id.postTitle);
            postTitle.setText(title.toUpperCase());
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
        }

        public void setBody(String postBody) {
            TextView post = (TextView) v.findViewById(R.id.post);
            post.setText(postBody);


        }

        public void setTime(Long time) {
            TextView txtTime = (TextView) v.findViewById(R.id.postTime);
            //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
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

        }
    }
}

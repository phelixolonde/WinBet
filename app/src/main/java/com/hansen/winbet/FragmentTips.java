package com.hansen.winbet;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FragmentTips extends Fragment {

    public RecyclerView recyclerListView;
    public MyRecyclerAdapter myAdapter;
    DatabaseReference databaseReference;
    TextView loading;
    View v;
    SwipeRefreshLayout refresher;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_home, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        creatingLayouts();

        final NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }

    public void creatingLayouts() {

        refresher = (SwipeRefreshLayout) v.findViewById(R.id.refresher);
        refresher.setRefreshing(true);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myAdapter != null) {
                    updateAdapter();

                }
            }
        });
        loading = (TextView) v.findViewById(R.id.loading);

        recyclerListView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerListView.setHasFixedSize(false);
        recyclerListView.setLayoutManager(new LinearLayoutManager(getContext()));

        myAdapter = new MyRecyclerAdapter(getContext());
        updateAdapter();
        recyclerListView.setAdapter(myAdapter);
    }


    //update adapter
    public void updateAdapter() {

        final List<Model> listPosts = new ArrayList<>();
        listPosts.clear();
        databaseReference.child("winbet").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listPosts.add(dataSnapshot.getValue(Model.class));
                displayPosts(listPosts);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void displayPosts(List<Model> ls) {
        refresher.setRefreshing(false);
        loading.setVisibility(View.GONE);
        recyclerListView.setVisibility(View.VISIBLE);
        myAdapter.setData(ls);
        myAdapter.notifyDataSetChanged();
    }


}

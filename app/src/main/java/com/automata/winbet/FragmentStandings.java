package com.automata.winbet;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class FragmentStandings extends Fragment {
    WebView webView;
    SwipeRefreshLayout refresher;


    View v;
    String[] leagues = {"Barclays Premier League", "Spanish La Liga", "German Bundesliga", "French Ligue 1", "Italian Serie A",
            "Belgium Jupiler League" , "Holland Eredivisie", "Portugal Primeira Liga","Greek Superleague", "UEFA Champions League", "UEFA Europa League", "England Championship", "England League 1",
            "England League 2",  "Brazil Serie A",
            "Turkish Super Lig","MLS"};

    ListView listView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_h2h, container, false);




        // getSupportActionBar().setTitle(title);


        listView = v.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, leagues);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                Intent intent = new Intent(getActivity(), Detailed.class);
                intent.putExtra("title", listView.getItemAtPosition(position).toString());
                startActivity(intent);



            }
        });


        return v;
    }


}

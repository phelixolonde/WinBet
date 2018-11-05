package com.automata.winbet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<NewsModel> data;
    private static final String TAG ="FACEBOOK_ADS" ;
    private InterstitialAd interstitialAd;

    public AdapterNews(Context context, List<NewsModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_row, parent, false);
        MyHolder holder = new MyHolder(view);

        interstitialAd = new InterstitialAd(context, "316921022146803_395199880985583");
        interstitialAd.loadAd();


        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        final NewsModel current = data.get(position);

        myHolder.newsTitle.setText(current.title);
       // myHolder.newsTime.setText(current.time);
        myHolder.newsDesc.setText(current.description);
        Picasso.with(context).load(current.image).into(myHolder.imageView);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }

                Intent intent = new Intent(context, News_Detailed.class);
                intent.putExtra("url", current.url);
                context.startActivity(intent);



            }
        });


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();

    }


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView newsTitle;
        TextView newsDesc;
        ImageView imageView;


        public MyHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
          //  newsTime = itemView.findViewById(R.id.newsTime);
            newsDesc = itemView.findViewById(R.id.newsDesc);
            imageView=itemView.findViewById(R.id.imgBody);
        }

    }

}
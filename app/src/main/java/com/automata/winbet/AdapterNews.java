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

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<NewsModel> data = Collections.emptyList();

    public AdapterNews(Context context, List<NewsModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_row, parent, false);
        MyHolder holder = new MyHolder(view);
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
        TextView newsTime, newsDesc;


        public MyHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.newsTitle);
            newsTime = (TextView) itemView.findViewById(R.id.newsTime);
            newsDesc = (TextView) itemView.findViewById(R.id.newsDesc);
        }

    }

}
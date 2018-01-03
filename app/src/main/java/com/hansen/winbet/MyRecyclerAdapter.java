package com.hansen.winbet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private List<Model> postList = new ArrayList<>();

    public MyRecyclerAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Model> posts) {
        postList.clear();
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyRecyclerAdapter.MyViewHolder holder, final int position) {
        final Model model = postList.get(position);

        holder.txtTitle.setText(model.getTitle());
        holder.txtBody.setText(model.getBody());
        holder.txtTime.setText(setTime(model.getTime()));
        holder.txtId.setText(model.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailedIntent = new Intent(mContext, PostDetailed.class);
                detailedIntent.putExtra("post_key", holder.txtId.getText());

                mContext.startActivity(detailedIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtBody;
        public TextView txtTime;
        public TextView txtId;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.postTitle);
            txtBody = (TextView) itemView.findViewById(R.id.post);
            txtTime = (TextView) itemView.findViewById(R.id.postTime);
            txtId = (TextView) itemView.findViewById(R.id.txtId);

        }
    }

    public String setTime(Long time) {
        try {
            //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
            long elapsedTime;
            long currentTime = System.currentTimeMillis();
            int elapsed = (int) ((currentTime - time) / 1000);
            if (elapsed < 60) {
                if (elapsed < 2) {
                    return "Just Now";
                } else {
                    return elapsed + " sec ago";
                }
            } else if (elapsed > 604799) {
                elapsedTime = elapsed / 604800;
                if (elapsedTime == 1) {
                    return elapsedTime + " week ago";
                } else {

                    return elapsedTime + " weeks ago";
                }
            } else if (elapsed > 86399) {
                elapsedTime = elapsed / 86400;
                if (elapsedTime == 1) {
                    return elapsedTime + " day ago";
                } else {
                    return elapsedTime + " days ago";
                }
            } else if (elapsed > 3599) {
                elapsedTime = elapsed / 3600;
                if (elapsedTime == 1) {
                    return elapsedTime + " hour ago";
                } else {
                    return elapsedTime + " hours ago";
                }
            } else if (elapsed > 59) {
                elapsedTime = elapsed / 60;
                return elapsedTime + " min ago";


            }
        } catch (Exception e) {
            return "moments ago";
        }
        return "moments ago";
    }
}

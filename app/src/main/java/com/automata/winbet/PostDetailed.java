package com.automata.winbet;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetailed extends AppCompatActivity {
    DatabaseReference mRef;
    String postKey;
    TextView tvTitle, tvBody, tvTime;

    ImageView imgBody;
    ProgressDialog pd;
    private AdView mBannerAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_detailed);

        postKey = getIntent().getExtras().getString("postKey");
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTime = (TextView) findViewById(R.id.post_time);
        imgBody = (ImageView) findViewById(R.id.imgBody);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        mBannerAd = (AdView) findViewById(R.id.banner_AdView);
        showBannerAd();


        if (postKey != null) {

            mRef = FirebaseDatabase.getInstance().getReference().child("winbet1").child(postKey);
        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String title = dataSnapshot.child("title").getValue().toString();
                    String body = dataSnapshot.child("body").getValue().toString();
                    Long time = (Long) dataSnapshot.child("time").getValue();
                    if (title != null) {
                        tvTitle.setText(title.toUpperCase());
                        pd.dismiss();
                    } else {
                        Toast.makeText(PostDetailed.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                    }
                    if (body != null) {
                        tvBody.setText(body);

                    }
                    if (time != null) {
                        setTime(time);
                    }
                    if (dataSnapshot.hasChild("image")) {
                        String image = (String) dataSnapshot.child("image").getValue();

                    }
                }catch (Exception e){
                    tvTitle.setText("");
                    tvBody.setText("");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mBannerAd.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTime(Long time) {
        TextView txtTime = (TextView) findViewById(R.id.post_time);
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

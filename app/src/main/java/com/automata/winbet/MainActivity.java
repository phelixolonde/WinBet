package com.automata.winbet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    boolean frag_livescore = false;
    private boolean doubleBack = false;
    String dev_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FirebaseMessaging.getInstance().subscribeToTopic("winbet1");


        dev_id = FirebaseInstanceId.getInstance().getToken();

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }catch (Exception ignored){

        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        try {
            tabLayout.setupWithViewPager(mViewPager);
        }catch (Exception ignored){

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.about) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("WIN BET");
            try {
                alert.setMessage("Version " + getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode +
                        "\n"+"Automata Software. \n" + "\n" +
                        "All rights reserved \n"
                );
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            alert.show();
        } else if (id == R.id.feedback) {
            startActivity(new Intent(MainActivity.this, Feedback.class));
        }else if(id==R.id.joinVip){
            Intent in=new Intent(MainActivity.this,VIPActivity.class);
            startActivity(in);
        }
        else if(id==R.id.privacy){
            Intent in=new Intent(MainActivity.this,PrivacyPolicy.class);
            startActivity(in);
        }else if (id==R.id.updaete){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.rate) {

                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                    }


        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.menu_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I've been winning big with this Win Bet app. Download here https://play.google.com/store/apps/details?id=com.automata.winbet";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Win Bet App");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return new Fragment_Tips();
                case 2:

                    return new FragmentStandings();
                case 1:
                    frag_livescore = true;
                    return new Fragment_Livescore();
                case 3:

                    return new Fragment_News();

            }

            return new Fragment_Tips();
        }

        @Override
        public int getCount() {

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            switch (position) {
                case 0:
                    return "TIPS";

                case 2:
                    return "TABLES";
                case 1:
                    return "LIVESCORE";
                case 3:
                    return "NEWS";
            }
            return "TIPS";
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            return;
        }
        this.doubleBack = true;
        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);

    }
}

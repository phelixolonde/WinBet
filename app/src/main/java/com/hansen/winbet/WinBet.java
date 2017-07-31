package com.hansen.winbet;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by HANSEN on 2/24/2017.
 */

public class WinBet extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
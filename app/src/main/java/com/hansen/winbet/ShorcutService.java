package com.hansen.winbet;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Hansen on 24-Jun-17.
 */

public class ShorcutService extends Service {
    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("winbet");
    int seenPosts;
    String seen;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            seen = intent.getExtras().getString("seenPosts");
        }catch (Exception e){
            e.printStackTrace();
        }
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               int allposts= (int) dataSnapshot.getChildrenCount();
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
               if (!(seen== null)){
                   seenPosts=Integer.parseInt(seen);

                   int newPosts=allposts-seenPosts;

                   if(newPosts>0) {
                       ShortcutBadger.applyCount(getApplicationContext(), newPosts);
                       sp.edit().putInt("newPosts",newPosts).apply();
                   }
                   else{
                       ShortcutBadger.removeCount(getApplicationContext());
                   }
               }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

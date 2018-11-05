package com.automata.winbet;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class Feedback_Detailed extends AppCompatActivity {

    TextView txtQuiz, txtAnswer;
    String quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            try {
                getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back));
            }catch (Exception ignored){

            }
        }

        setContentView(R.layout.activity_feedback__detailed);
        txtQuiz = findViewById(R.id.txtQuiz);
        txtAnswer = findViewById(R.id.txtAnswer);
        quiz = getIntent().getExtras().getString("quiz");
        txtQuiz.setText(quiz);
        txtAnswer.setTextIsSelectable(true);

        switch (quiz) {

            case "App not working":
                txtAnswer.setText("Kindly update your app to the latest version.");
                break;
            case "Tips not loading":
                txtAnswer.setText("Swipe down to refresh. Whenever you see a notification, always check the app." +
                        "If tips are not showing, then swipe down to refresh");
                break;
            case "Tips not arriving in time":
                txtAnswer.setText("Tips are usually posted in the morning," +
                        "If you are getting the tips late then always look out for the notifications." +
                        "If there was a notification but you can't see the tips, then swipe down to refresh.");

                break;
            case "Ads are too much":
                txtAnswer.setText("The most important thing to you is that you win." +
                        "We are giving you tips for free, yet we buy these tips." +
                        "Kindly bear with us since those ads are the only way we can also get something little from " +
                        "the app.without them we wouldn't even exist.Don't get frustrated, winning is the most" +
                        " useful thing here. Thank you! ");

                break;
            case "I have another issue":
                startActivity(new Intent(Feedback_Detailed.this, Email_Admin.class));
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

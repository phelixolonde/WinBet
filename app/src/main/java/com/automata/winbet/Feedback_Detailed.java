package com.automata.winbet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


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

                SpannableString ss = new SpannableString("Kindly update your app to the latest version.Click here to update");
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(Feedback_Detailed.this, "Unable to find play store", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                };
                ss.setSpan(clickableSpan, 45, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                txtAnswer.setText(ss);
                txtAnswer.setMovementMethod(LinkMovementMethod.getInstance());
                txtAnswer.setHighlightColor(Color.TRANSPARENT);
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

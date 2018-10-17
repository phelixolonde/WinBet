package com.automata.winbet;

import android.content.Intent;
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
        }

        setContentView(R.layout.activity_feedback__detailed);
        txtQuiz = (TextView) findViewById(R.id.txtQuiz);
        txtAnswer = (TextView) findViewById(R.id.txtAnswer);
        quiz = getIntent().getExtras().getString("quiz");
        txtQuiz.setText(quiz);
        txtAnswer.setTextIsSelectable(true);

        switch (quiz) {
            case "How to join VIP?":
                txtAnswer.setText(
                        "VIP CHANNEL WINS AT LEAST 5 DAYS IN A WEEK" + "\n" +
                                "IT HAS SINGLE ODDS 2+, MULTIBETS OF ODDS 20+, JACKPOTS AND MEGA-JACKPOTS" + "\n" +
                                "*AFTER PAYMENT, YOU GET THE LINK TO VIP CHANNEL INSTANTLY" + "\n" + "\n" +
                                "AMOUNT" + "\n" +
                                "-----------------------------------" + "\n" +
                                "KENYANS: KSH.1000" + "\n" +
                                "NIGERIA: NGN 4000" + "\n" +
                                "TANZANIA: TZS 24000" + "\n" +
                                "UGANDA: UGX 38000" + "\n" +
                                "GHANA: 50 CEDIS" + "\n" +
                                "OTHER COUNTRIES: 10 US DOLLARS" + "\n" + "\n" +
                                "DURATION: 4 MONTHS" + "\n" + "\n" +
                                "PAYMENT METHODS" + "\n" +
                                "------------------------------------" + "\n" +

                                "MPESA: +254719275724" + "\n" +
                                "PAYPAL: hansenphelix@gmail.com" + "\n" +
                                "ECOBANK RAPID TRANSFER (Except Nigeria): Ask admin for details" + "\n" +
                                "BITCOINS: 18yNpK6V8A4A6ZtWHkA95zFV3Bc343b18j");

                break;
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

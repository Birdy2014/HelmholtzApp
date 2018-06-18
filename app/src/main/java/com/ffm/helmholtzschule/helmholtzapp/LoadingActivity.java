package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.loadingtext);

                if (!dataStorage.isInternetReachable()) {
                    textView.setText("Kein Internet");
                } else {
                    final boolean[] authorized = new boolean[1];
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            System.out.println("Veryfy cred");
                            authorized[0] = dataStorage.getVertretungsplan().verifyCredentials();
                            System.out.println(authorized[0]);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!authorized[0]) {
                        System.out.println("Back to Login");
                        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                        intent.putExtra("tryLoginAgain", "true");
                        startActivity(intent);
                        finish();
                        System.out.println("Should be back to login");
                    } else {
                        try {
                            dataStorage.update();
                        } catch (NoConnectionException e) {
                            textView.setText("Kein Internet");
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 100);
    }
}

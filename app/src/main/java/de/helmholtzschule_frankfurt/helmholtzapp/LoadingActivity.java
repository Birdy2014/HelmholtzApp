package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity{
    DataStorage dataStorage = DataStorage.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) findViewById(R.id.version_view)).setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.loadingtext);

                if (!dataStorage.isInternetReachable()) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("Kein Internet");
                        }
                    });
                } else {
                    final boolean authorized;

                    authorized = dataStorage.getVertretungsplan().verifyCredentials();
                    System.out.println(authorized);

                    if (!authorized) {
                        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                        intent.putExtra("tryLoginAgain", "true");
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            dataStorage.update(LoadingActivity.this);
                        } catch (NoConnectionException e) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("Kein Internet");
                                }
                            });
                        }

                    }
                }
            }
        };
        thread.start();
    }
}

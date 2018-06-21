package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LoadingActivity extends AppCompatActivity {
    DataStorage dataStorage = DataStorage.getInstance();

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
                            dataStorage.update();
                        } catch (NoConnectionException e) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("Kein Internet");
                                }
                            });
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}

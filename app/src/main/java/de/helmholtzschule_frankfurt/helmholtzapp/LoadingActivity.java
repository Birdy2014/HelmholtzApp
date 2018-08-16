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
import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

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
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        String[] credentials = client.getVertretungsplanCredentials("vertretungsplan");
        String base64credentials = Base64.encodeToString((credentials[0] + ":" + credentials[1]).getBytes(), Base64.DEFAULT).trim();

        dataStorage.initialize(base64credentials);
        Thread thread = new Thread() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.loadingtext);

                if (!dataStorage.isInternetReachable()) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("Keine Verbindung");
                            textView.setText("Verbindung zum Server konnte nicht\nhergestellt werden");
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
                            int[] toDownload = getIntent().getIntArrayExtra("toDownload");
                            ArrayList<EnumDownload> params = new ArrayList<>();
                            if(toDownload == null || toDownload.length == 0 || toDownload[0] == -1){
                                params.addAll(Arrays.asList(EnumDownload.values()));
                            }
                            else {
                                for (int i : toDownload) {
                                    params.add(EnumDownload.values()[i]);
                                }
                            }
                            dataStorage.update(LoadingActivity.this, params.toArray(new EnumDownload[]{}));
                        } catch (NoConnectionException e) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("NoConnectionException");
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

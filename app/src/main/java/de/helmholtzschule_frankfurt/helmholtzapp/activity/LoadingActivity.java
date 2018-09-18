package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

public class LoadingActivity extends AppCompatActivity {
    DataStorage dataStorage = DataStorage.getInstance();
    private boolean isInBackground = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView) findViewById(R.id.version_view)).setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.loadingInfo);
        if (!dataStorage.isInternetReachable()) {
            textView.setText("Verbindung zum Server fehlgeschlagen");
            return;
        }
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        client.init(new String[]{"vertretungsplan", "kalender", "stundenplan"}, mySPR, LoadingActivity.this);


        if (!client.validateTokens()) {
            System.out.println("Login Activity started!");
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //this.finish();
            return;
        }

        String[] credentials = client.getVertretungsplanCredentials("vertretungsplan");
        String base64credentials = Base64.encodeToString((credentials[0] + ":" + credentials[1]).getBytes(), Base64.DEFAULT).trim();

        Thread thread = new Thread() {
            @Override
            public void run() {
                int[] toDownload = getIntent().getIntArrayExtra("toDownload");
                ArrayList<EnumDownload> params = new ArrayList<>();
                if (toDownload == null || toDownload.length == 0 || toDownload[0] == -1) {
                    params.addAll(Arrays.asList(EnumDownload.values()));
                } else {
                    for (int i : toDownload) {
                        params.add(EnumDownload.values()[i]);
                    }
                }
                dataStorage.update(LoadingActivity.this, params.toArray(new EnumDownload[]{}));
            }
        };
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable) {
        String stacktrace = Log.getStackTraceString(throwable);
        String message = throwable.getMessage();

        System.out.println(stacktrace);

        Intent intent = new Intent(LoadingActivity.this, SendActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("stacktrace", stacktrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }

}

package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

public class LoadingActivity extends AppCompatActivity {
    DataStorage dataStorage = DataStorage.getInstance();

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
        TextView textView = findViewById(R.id.loadingInfo);
        if (!dataStorage.isServerReachable()) {
            final int[] i = {10};
            ImageButton retry = findViewById(R.id.button_retry);
            retry.setOnClickListener(click -> this.recreate());
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    textView.post(() -> textView.setText("Verbindung zum Server fehlgeschlagen" + "\n\n" + "Erneut versuchen in " + i[0]--));
                    if (i[0] == 0) {
                        timer.cancel();
                        textView.post(() -> textView.setText("Erneut versuchen"));
                        retry.post(() -> retry.setVisibility(View.VISIBLE));
                    }
                }
            };
            timer.schedule(task, 0, 1000);
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

package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
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
import de.helmholtzschule_frankfurt.helmholtzapp.exception.NoConnectionException;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

public class LoadingActivity extends AppCompatActivity{
    DataStorage dataStorage = DataStorage.getInstance();
    private boolean isInForeground = true;

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
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        String[] credentials = client.getVertretungsplanCredentials("vertretungsplan");
        String base64credentials = Base64.encodeToString((credentials[0] + ":" + credentials[1]).getBytes(), Base64.DEFAULT).trim();

        if(getIntent().getIntArrayExtra("toDownload") == null) dataStorage.initialize(base64credentials);
        Thread thread = new Thread() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.loadingtext);

                if (!dataStorage.isInternetReachable()) {
                    textView.post(() -> {
                        System.out.println("Keine Verbindung");
                        textView.setText("Verbindung zum Server konnte nicht\nhergestellt werden");
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
                            System.out.println("Is in Foreground? " + isInForeground);
                            dataStorage.update(LoadingActivity.this, !isInForeground, params.toArray(new EnumDownload[]{}));
                        } catch (NoConnectionException e) {
                            textView.post(() -> {
                                System.out.println("NoConnectionException");
                                textView.setText("Kein Internet");
                            });
                        }

                    }
                }
            }
        };
        thread.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable){
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

    @Override
    protected void onPause() {
        super.onPause();
        isInForeground = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
    }
}

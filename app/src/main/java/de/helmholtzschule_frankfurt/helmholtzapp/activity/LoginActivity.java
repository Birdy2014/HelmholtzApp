package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        WebView webView = (WebView)findViewById(R.id.activity_login_web_view);
        Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        client.init(new String[]{"vertretungsplan", "kalender", "stundenplan"}, mySPR, LoginActivity.this, webView);
        client.login(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable){
        String stacktrace = Log.getStackTraceString(throwable);
        String message = throwable.getMessage();

        System.out.println(stacktrace);

        Intent intent = new Intent(LoginActivity.this, SendActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("stacktrace", stacktrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }
}

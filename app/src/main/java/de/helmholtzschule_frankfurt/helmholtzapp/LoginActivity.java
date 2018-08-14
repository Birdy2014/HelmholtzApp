package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //System.out.println("PackageName: " + this.getPackageName());
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        WebView webView = (WebView)findViewById(R.id.activity_login_web_view);
        Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        client.init(new String[]{"vertretungsplan", "kalender", "stundenplan"}, mySPR, LoginActivity.this, webView);
        client.login(intent);
    }
}

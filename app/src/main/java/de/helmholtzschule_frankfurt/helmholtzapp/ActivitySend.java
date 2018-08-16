package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.Calendar;

public class ActivitySend extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        String message = getIntent().getStringExtra("message");
        String stacktrace = getIntent().getStringExtra("stacktrace");

        Button button = findViewById(R.id.crashButton);
        button.setOnClickListener(click -> {


            String email = this.getResources().getString(R.string.emailHelp);

            String uriString = "mailto:" + email;

            Uri uri = Uri.parse(uriString);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("text/plain");
            emailIntent.setData(uri);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Helmholtzapp Crash Report " + Calendar.getInstance().getTime().toString());
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.putExtra(Intent.EXTRA_TEXT, stacktrace);
            startActivity(emailIntent);
            System.exit(0);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}

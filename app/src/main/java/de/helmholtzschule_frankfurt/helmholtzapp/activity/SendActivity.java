package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;

public class SendActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        String message = getIntent().getStringExtra("message");
        String stacktrace = getIntent().getStringExtra("stacktrace");

        ImageButton button = (ImageButton) findViewById(R.id.crashButton);
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
            this.finishAffinity();
        });
        ImageButton bell = (ImageButton) findViewById(R.id.send_notification);
        String src = this.getResources().getString(R.string.hhs_app_alert_message);
        Context context = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String raw = DataStorage.getInstance().download(src);
                    if (raw == null) raw = "{}";
                    HashMap<String, String> notification = raw.equals("{}") ? null : new Gson().fromJson(raw, HashMap.class);
                    if (notification == null) bell.setVisibility(View.GONE);
                    else {
                        bell.setOnClickListener(click -> {
                            Dialog dialog = new Dialog(context);
                            View dialogView = View.inflate(context, R.layout.layout_notification_crash, new LinearLayout(context));
                            dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            ImageButton dialogButton = dialogView.findViewById(R.id.dialogButton);
                            dialogButton.setOnClickListener(click1 -> dialog.cancel());
                            TextView notificationText = dialogView.findViewById(R.id.notificationText);
                            notificationText.setText(notification.get("message"));
                            dialog.show();

                            ProgressBar progressBar = dialogView.findViewById(R.id.crash_progress_bar);
                            Timer timer = new Timer();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    progressBar.post(() -> progressBar.setProgress(progressBar.getProgress() + 1));
                                    if (progressBar.getProgress() == 100) {
                                        timer.cancel();
                                        dialog.cancel();
                                    }
                                }
                            };
                            timer.schedule(task, 0, 100);
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}

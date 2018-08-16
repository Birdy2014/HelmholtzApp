package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import de.helmholtzschule_frankfurt.helmholtzapp.R;

public class VertretungsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_vertretung);
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
        ImageButton back = (ImageButton)findViewById(R.id.activity_vertretung_arrow);
        back.setOnClickListener(click -> super.onBackPressed());
        super.onCreate(savedInstanceState);

        String[] data = getIntent().getStringExtra("vertretung").split("<!>");
        String[] headers = {"", "", "Fach", "Klasse", "Lehrer", "Für", "Raum", "Hinweis"};
        int background = getIntent().getIntExtra("color", R.color.colorWhite);

        findViewById(R.id.activity_vertretung_background).setBackground(getResources().getDrawable(background));
        findViewById(R.id.activity_vertretung_arrow).setBackground(getResources().getDrawable(background));

        int[] views = {R.id.activity_vertretung_hour, R.id.activity_vertretung_type, R.id.activity_vertretung_fach_text, R.id.activity_vertretung_klasse_text, R.id.activity_vertretung_lehrer_text, R.id.activity_vertretung_fuer_text,  R.id.activity_vertretung_raum_text, R.id.activity_vertretung_hinweis_text};

        for(int i = 0; i < views.length; i++){
            ((TextView)findViewById(views[i])).setText((!headers[i].equals("") ? headers[i] + "\n" : "") + (data[i].equals("") || data[i].equals(" ") ? "???" : data[i]));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable){
        String stacktrace = Log.getStackTraceString(throwable);
        String message = throwable.getMessage();

        Intent intent = new Intent(VertretungsActivity.this, SendActivity.class);
        intent.putExtra("message", message);
        intent.putExtra("stacktrace", stacktrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(0);
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


public class LoginActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);

        setContentView(R.layout.activity_login);
        HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();
        Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        EditText usernameInput = findViewById(R.id.etBenutzername);
        EditText passwordInput = findViewById(R.id.etPasswort);

        Button button = findViewById(R.id.bLogIn);
        button.setOnClickListener(click -> {
            System.out.println("CLICKED!");
            if (DataStorage.getInstance().isServerReachable()) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if (client.validateAndRequestTokens(username, password)) {
                    System.out.println("START LOADING!!!!!");
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(this, "Falscher Benutzername oder Passwort", Toast.LENGTH_LONG).show();
                }
            } else Toast.makeText(this, "Keine Internetverbindung", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.login_register).setOnClickListener(click -> {
            String url = getResources().getString(R.string.hhs_app_register);
            Intent webIntent = new Intent();
            webIntent.setAction(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(webIntent, "Öffnen in"));
        });

        findViewById(R.id.login_protection).setOnClickListener(click -> {
            String url = getResources().getString(R.string.hhs_app_impressum_database);
            Intent webIntent = new Intent();
            webIntent.setAction(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(webIntent, "Öffnen in"));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable) {
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

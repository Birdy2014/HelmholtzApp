package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;


public class LoginActivity extends AppCompatActivity {
    boolean authorized;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Öffne Login");

        if (!dataStorage.isInternetReachable())
            Toast.makeText(getApplicationContext(), "Keine Internetverbindung.", Toast.LENGTH_LONG).show();
        else {
            SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);

            dataStorage.initialize(mySPR.getString("auth", "").trim());

            Thread thread = new Thread() {
                @Override
                public void run() {
                    authorized = dataStorage.getVertretungsplan().verifyCredentials();
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (authorized) {
                try {
                    dataStorage.update();
                } catch (NoConnectionException e) {
                    //Kein Internet
                    System.out.println("Kein Internet");
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn = (Button) findViewById(R.id.bLogIn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!dataStorage.isInternetReachable())
                    Toast.makeText(getApplicationContext(), "Keine Internetverbindung.", Toast.LENGTH_LONG).show();
                else {
                    SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
                    SharedPreferences.Editor editor = mySPR.edit();

                    EditText editTextUsername = (EditText) findViewById(R.id.etBenutzername);
                    String username = editTextUsername.getText().toString();

                    EditText editTextPassword = (EditText) findViewById(R.id.etPasswort);
                    String password = editTextPassword.getText().toString();

                    EditText editTextKlasse = (EditText) findViewById(R.id.etKlasse);
                    String klasse = editTextKlasse.getText().toString();


                    dataStorage.initialize(Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT).trim());
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            authorized = dataStorage.getVertretungsplan().verifyCredentials();
                        }
                    };
                    thread.start();

                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (authorized) {
                        try {
                            dataStorage.update();
                        } catch (NoConnectionException e) {
                            //Kein Internet
                            System.out.println("Kein Internet");
                            e.printStackTrace();
                        }
                        editor.putString("klasse", klasse);
                        editor.putString("auth", Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT));
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Benutzername und/oder Passwort falsch eingegeben.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

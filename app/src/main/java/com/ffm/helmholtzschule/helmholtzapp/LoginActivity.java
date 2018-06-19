package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Öffne Login");
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);

        if (mySPR.getString("auth", "").equals("gagagagahhbehbwehbwe")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn = (Button) findViewById(R.id.bLogIn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
                SharedPreferences.Editor editor = mySPR.edit();

                EditText editTextUsername = (EditText) findViewById(R.id.etBenutzername);
                String username = editTextUsername.getText().toString();

                EditText editTextPassword = (EditText) findViewById(R.id.etPasswort);
                String password = editTextPassword.getText().toString();

                EditText editTextKlasse = (EditText) findViewById(R.id.etKlasse);
                String klasse = editTextKlasse.getText().toString();


                Vertretungsplan vertretungsplan = new Vertretungsplan(Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (vertretungsplan.verifyCredentials()) {
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("klasse", klasse);
                            editor.putString("auth", "gagagagahhbehbwehbwe");
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Benutzername und/oder Passwort falsch eingegeben.", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                thread.start();
            }
        });
    }
}

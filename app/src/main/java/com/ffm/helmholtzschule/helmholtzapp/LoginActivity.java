package com.ffm.helmholtzschule.helmholtzapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Connection;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;


public class LoginActivity extends AppCompatActivity {

    public static final String PREFS_NAME="MyPrefsFile";
    private EditText etBenutzername,etKlasse,etPasswort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Öffne Login");
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);

        if(mySPR.getString("auth", "").equals("gagagagahhbehbwehbwe")){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etBenutzername = (EditText) findViewById(R.id.etBenutzername);
        etKlasse = (EditText) findViewById(R.id.etKlasse);
        etPasswort = (EditText) findViewById(R.id.etPasswort);


        etBenutzername.setText(mySPR.getString("myKey1", ""));
        etKlasse.setText(mySPR.getString("myKey2", ""));
        etPasswort.setText(mySPR.getString("myKey3", ""));

        //@Override
        //protected void onStop() {
        // super.onStop();
        Button btn = (Button) findViewById(R.id.bLogIn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
                SharedPreferences.Editor editor = mySPR.edit();

                editor.putString("myKey1", etBenutzername.getText().toString());
                editor.putString("myKey2", etKlasse.getText().toString());
                editor.putString("myKey3", etPasswort.getText().toString());
                editor.commit();

                EditText text = (EditText) findViewById(R.id.etBenutzername);
                String value = text.getText().toString();

                EditText text1 = (EditText) findViewById(R.id.etPasswort);
                String value1 = text1.getText().toString();

                EditText text2 = (EditText) findViewById(R.id.etKlasse);
                String value2 = text2.getText().toString();


                Vertretungsplan vertretungsplan = new Vertretungsplan(Base64.encodeToString((value + ":" + value1).getBytes(), Base64.DEFAULT));
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        if (vertretungsplan.verifyCredentials()) {
                            editor.putString("username", value);
                            editor.putString("password", value1);
                            editor.putString("auth", "gagagagahhbehbwehbwe");
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Benutzername und/oder Passwort falsch eingegeben.", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                thread.start();
            }
        });

    }

}

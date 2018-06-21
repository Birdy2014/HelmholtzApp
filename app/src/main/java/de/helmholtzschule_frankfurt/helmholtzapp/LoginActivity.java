package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class LoginActivity extends AppCompatActivity {
    boolean authorized;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean tryLoginAgain;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            tryLoginAgain = getIntent().getStringExtra("tryLoginAgain").equals("true");
        } catch (Exception e) {
            tryLoginAgain = false;
        }

        if (!tryLoginAgain) {
            SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
            dataStorage.initialize(mySPR.getString("auth", "").trim());

            Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
            startActivity(intent);
            finish();
        }
        Spinner dropdown = (Spinner)findViewById(R.id.choose_klasse);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataStorage.klassen);
        dropdown.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.bLogIn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
                SharedPreferences.Editor editor = mySPR.edit();

                EditText editTextUsername = (EditText) findViewById(R.id.etBenutzername);
                String username = editTextUsername.getText().toString().replaceAll(" ", "");

                EditText editTextPassword = (EditText) findViewById(R.id.etPasswort);
                String password = editTextPassword.getText().toString();

                String klasse = dropdown.getSelectedItem().toString();

                String base64credentials = Base64.encodeToString((username + ":" + password).getBytes(), Base64.DEFAULT).trim();

                editor.putString("auth", base64credentials);
                dataStorage.setKlasse(LoginActivity.this, klasse);
                editor.apply();
                dataStorage.initialize(base64credentials);

                Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

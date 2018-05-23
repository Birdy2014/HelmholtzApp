package com.ffm.helmholtzschule.helmholtzapp;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.RelativeLayout;

        import java.util.ArrayList;

public class MensaActivity extends AppCompatActivity {
    public ListView lstMenu;
    public ArrayList<News> daten;
    public MensaAdapter mensaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa);

        ArrayList<Mensaplan> daten = new ArrayList<Mensaplan>();
        daten.add(new Mensaplan("Tag", "Fleischgericht", "Vegetarisch", "Nachtisch"));
        daten.add(new Mensaplan("Montag", "WUAST", "Braucht niemand", "Schoki"));

        mensaAdapter = new MensaAdapter(this, daten);
        lstMenu = (ListView) findViewById(R.id.mensaMenu);
        lstMenu.setAdapter(mensaAdapter);
        mensaAdapter.notifyDataSetChanged();

    }

}


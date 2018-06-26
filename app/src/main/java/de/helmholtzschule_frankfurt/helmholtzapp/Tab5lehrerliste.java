package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class Tab5lehrerliste extends Fragment {

    public ListView lstMenu;
    public ArrayList<LehrerItem> daten = new ArrayList<>();
    public LehrerAdapter menuAdapter;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab5lehrerliste, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        for(String s : dataStorage.getLehrerliste()){
            daten.add(new LehrerItem(s));
        }
        menuAdapter = new LehrerAdapter(this.getContext(), daten);
        lstMenu = (ListView) getView().findViewById(R.id.lehrer_list_view);
        lstMenu.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();

        lstMenu.setOnItemClickListener((adapterView, view1, i, l) -> {
            String itemString = lstMenu.getItemAtPosition(i).toString();
            String email = itemString.substring(itemString.indexOf("\n") + 1);
            System.out.println(email);

            String uriString = "mailto:" + email;

            Uri uri = Uri.parse(uriString);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("text/plain");
            emailIntent.setData(uri);
            startActivity(Intent.createChooser(emailIntent, "Senden via"));
        });

        lstMenu.setAdapter(menuAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
}

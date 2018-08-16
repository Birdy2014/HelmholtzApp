package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import io.github.birdy2014.VertretungsplanLib.Vertretung;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab1vertretungsplan extends Fragment {
    public DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1vertretungsplan, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<Vertretung> daten = new ArrayList<>();

        ListAdapter MenuAdapter = new VertretungsplanAdapter(getContext(), daten);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(MenuAdapter);

        String klasse1 = HelmholtzDatabaseClient.getInstance().getKlasse();

        System.out.println("Why do java developers wear glasses?\nBecause they can\'t C#.");
        System.out.println("Programmiert von Jonas Schröter und Moritz Vogel.");

        super.onViewCreated(view, savedInstanceState);

        final CheckBox checkbox = (CheckBox) getView().findViewById(R.id.checkbox_vertretungsplan);
        final ToggleButton heuteMorgen = (ToggleButton) getView().findViewById(R.id.butShowAll);

        if (klasse1.charAt(0) == 'E' || klasse1.charAt(0) == 'Q' || klasse1.charAt(0) == 'e' || klasse1.charAt(0) == 'q') {
            klasse1 = Character.toUpperCase(klasse1.charAt(0)) + klasse1.substring(1);
        } else {
            int indexLetter = klasse1.length() - 1;
            klasse1 = klasse1.substring(0, indexLetter) + Character.toLowerCase(klasse1.charAt(indexLetter));
        }
        final String klasse = klasse1;
        System.out.println("Klasse: " + klasse);

        dataAccess(klasse);

        heuteMorgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                ArrayList<Vertretung> list = new ArrayList<>();
                if (checkbox.isChecked() && heuteMorgen.isChecked()) {
                    for (Vertretung vertretung : dataStorage.getVertretungsplan().getVertretungen().get(1))
                        list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(1));
                } else if (checkbox.isChecked() && !heuteMorgen.isChecked()) {
                    for (Vertretung vertretung : dataStorage.getVertretungsplan().getVertretungen().get(0))
                        list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(0));
                } else if (!checkbox.isChecked() && heuteMorgen.isChecked()) {
                    for (Vertretung v : dataStorage.getVertretungsplan().getVertretungen().get(1)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(1));
                } else {
                    for (Vertretung v : dataStorage.getVertretungsplan().getVertretungen().get(0)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(0));
                }
                ListView lstMenu = getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
                if (list.isEmpty()){
                    ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("Keine Vertretungen");
                }
                else {
                    ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("");
                }
            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                ArrayList<Vertretung> list = new ArrayList<Vertretung>();

                int heuteMorgenInt = !heuteMorgen.isChecked() ? 0 : 1;
                if(checkbox.isChecked()){
                    list.addAll(dataStorage.getVertretungsplan().getVertretungen().get(heuteMorgenInt));
                }
                else {
                    for (Vertretung v : dataStorage.getVertretungsplan().getVertretungen().get(heuteMorgenInt)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                }
                ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(heuteMorgenInt));
                vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
                if (list.isEmpty()){
                    ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("Keine Vertretungen");
                }
                else {
                    ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("");
                }
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_vertretungsplan);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                intent.putExtra("fragmentIndex", 1);
                intent.putExtra("toDownload", new int[]{1});
                startActivity(intent);
            }
        });
    }

    public void dataAccess(String klasse) {
        ArrayList<Vertretung> daten = new ArrayList<Vertretung>();

        for (Vertretung v : dataStorage.getVertretungsplan().getVertretungen().get(0)) {
            if (v.getKlasse().contains(klasse)) daten.add(v);
        }
        if (daten.isEmpty()){
            ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("Keine Vertretungen");
        }
        else {
            ((TextView)getActivity().findViewById(R.id.placeHolderText)).setText("");
        }

        ((TextView) getView().findViewById(R.id.dateText)).setText(dataStorage.getVertretungsplan().getDate(0));

        ArrayList<Vertretung> list = new ArrayList<>();

        for (Vertretung v : dataStorage.getVertretungsplan().getVertretungen().get(0)) {
            if (v.getKlasse().contains(klasse)) list.add(v);
        }
        VertretungsplanAdapter vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(vertretungenAdapter);
        vertretungenAdapter.notifyDataSetChanged();
    }
}

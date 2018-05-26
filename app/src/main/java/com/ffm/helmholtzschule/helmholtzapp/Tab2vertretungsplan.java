package com.ffm.helmholtzschule.helmholtzapp;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import io.github.birdy2014.VertretungsplanLib.Vertretung;
import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;

/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab2vertretungsplan extends Fragment {
    public Vertretungsplan vertretungsplan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2vertretungsplan, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<Vertretung> daten = new ArrayList<Vertretung>();
        ArrayList<Vertretung> meineDaten = new ArrayList<Vertretung>();

        ListAdapter MenuAdapter = new VertretungsplanAdapter(getContext(), daten);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(MenuAdapter);

        SharedPreferences mySPR = this.getContext().getSharedPreferences("MySPFILE", 0);
        String klasse1 = mySPR.getString("myKey2", "8a"); // TODO: 8a wieder rausnehmen
        String username = mySPR.getString("username", "");
        String password = mySPR.getString("password", "");

        vertretungsplan = new Vertretungsplan(username, password);
        downloadVertretungsplan(username, password);
        //debugAll();
        dataAccess();

        System.out.println("Why do java developers wear glasses?\nBecause they can\'t C#.");
        System.out.println("Programmiert von Jonas Schröter und Moritz Vogel.");

        super.onViewCreated(view, savedInstanceState);

        final ToggleButton klasseButton = (ToggleButton) getView().findViewById(R.id.toggleButton3);
        final ToggleButton heuteMorgen = (ToggleButton) getView().findViewById(R.id.butShowAll);

        if (klasse1.charAt(0) == 'E' || klasse1.charAt(0) == 'Q' || klasse1.charAt(0) == 'e' || klasse1.charAt(0) == 'e') {
            klasse1 = Character.toUpperCase(klasse1.charAt(0)) + klasse1.substring(1);
        } else {
            int indexLetter = klasse1.length() - 1;
            klasse1 = klasse1.substring(0, indexLetter) + Character.toLowerCase(klasse1.charAt(indexLetter));
        }
        final String klasse = klasse1;
        System.out.println("Klasse: " + klasse);
        heuteMorgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                if (klasseButton.isChecked() && heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung vertretung : vertretungsplan.getVertretungen().get(1)) list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(1));
                } else if (klasseButton.isChecked() && !heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung vertretung : vertretungsplan.getVertretungen().get(0)) list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(0));
                } else if (!klasseButton.isChecked() && heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung v : vertretungsplan.getVertretungen().get(1)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(1));
                } else {
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung v : vertretungsplan.getVertretungen().get(0)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(0));
                }
                ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
            }
        });


        klasseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                if (klasseButton.isChecked() && heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung vertretung : vertretungsplan.getVertretungen().get(1)) list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(1));
                } else if (klasseButton.isChecked() && !heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung vertretung : vertretungsplan.getVertretungen().get(0)) list.add(vertretung);
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(0));
                } else if (!klasseButton.isChecked() && heuteMorgen.isChecked()) {
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung v : vertretungsplan.getVertretungen().get(1)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(1));
                } else {
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
                    for (Vertretung v : vertretungsplan.getVertretungen().get(0)) {
                        if (v.getKlasse().contains(klasse)) list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                    ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(0));
                }
                ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
            }
        });


    }

    protected void downloadVertretungsplan(String username, String password) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    vertretungsplan.updateVertretungsplan();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void debugAll() {
        System.out.println("Nachrichten heute:");
        for (String str : vertretungsplan.getNachrichten().get(0)) {
            System.out.println(str);
        }
        System.out.println("Vertretungen heute:");
        if (vertretungsplan.getNachrichten().get(0).size() == 0)
            System.out.println("Keine Vertretungen");
        for (int i = 0; i < vertretungsplan.getVertretungen().get(0).size(); i++) {
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getKlasse());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getStunde());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getFach());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getVertretungsLehrer());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getFuer());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getRaum());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(0).get(i).getHinweis());
            System.out.print(", ");
            System.out.println(vertretungsplan.getVertretungen().get(0).get(i).getArt());
        }
        System.out.println("Nachrichten morgen:");
        for (String str : vertretungsplan.getNachrichten().get(1)) {
            System.out.println(str);
        }
        System.out.println("Vertretungen morgen:");
        if (vertretungsplan.getNachrichten().get(1).size() == 0)
            System.out.println("Keine Vertretungen");
        for (int i = 0; i < vertretungsplan.getVertretungen().get(1).size(); i++) {
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getKlasse());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getStunde());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getFach());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getVertretungsLehrer());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getFuer());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getRaum());
            System.out.print(", ");
            System.out.print(vertretungsplan.getVertretungen().get(1).get(i).getHinweis());
            System.out.print(", ");
            System.out.println(vertretungsplan.getVertretungen().get(1).get(i).getArt());
        }
    }

    public void dataAccess() {
        ArrayList<Vertretung> daten = new ArrayList<Vertretung>();

        /*
        daten.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Hinw.", "Art"));
*/
        SharedPreferences mySPR = this.getContext().getSharedPreferences("MySPFILE", 0);
        String klasse1 = mySPR.getString("myKey2", "8a"); // TODO: Klasse

        if (klasse1.charAt(0) == 'E' || klasse1.charAt(0) == 'Q' || klasse1.charAt(0) == 'e' || klasse1.charAt(0) == 'e') {
            klasse1 = Character.toUpperCase(klasse1.charAt(0)) + klasse1.substring(1);
        } else {
            int indexLetter = klasse1.length() - 1;
            klasse1 = klasse1.substring(0, indexLetter) + Character.toLowerCase(klasse1.charAt(indexLetter));
        }

        ((TextView) getView().findViewById(R.id.dateText)).setText(vertretungsplan.getDate(0));

        ArrayList<Vertretung> list = new ArrayList<>();
        list.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Von", "Hinw.", "Art"));
        for (Vertretung v : vertretungsplan.getVertretungen().get(0)) {
            if (v.getKlasse().contains(klasse1)) list.add(v);
        }
        VertretungsplanAdapter vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(vertretungenAdapter);
        vertretungenAdapter.notifyDataSetChanged();
    }
}

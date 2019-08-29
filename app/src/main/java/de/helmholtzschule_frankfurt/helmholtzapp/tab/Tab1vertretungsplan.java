package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Objects;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.activity.LoadingActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.adapter.VertretungsplanAdapter;
import de.helmholtzschule_frankfurt.helmholtzapp.item.Vertretung;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


public class Tab1vertretungsplan extends Fragment {

    CheckBox checkbox;
    ToggleButton heuteMorgen;

    DataStorage dataStorage = DataStorage.getInstance();
    ArrayList<Vertretung> daten = new ArrayList<>();
    final VertretungsplanAdapter[] vertretungenAdapter = new VertretungsplanAdapter[1];
    ListView lstMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1vertretungsplan, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("Why do java developers wear glasses?\nBecause they can\'t C#.");
        System.out.println("Programmiert von Jonas Schröter und Moritz Vogel.");

        lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        checkbox = (CheckBox) getView().findViewById(R.id.checkbox_vertretungsplan);
        checkbox.post(() -> checkbox.setChecked(false));
        heuteMorgen = (ToggleButton) getView().findViewById(R.id.butShowAll);
        heuteMorgen.post(() -> heuteMorgen.setChecked(false));

        String klasseTemp = HelmholtzDatabaseClient.getInstance().getKlasse();
        if (klasseTemp.charAt(0) == 'E' || klasseTemp.charAt(0) == 'Q' || klasseTemp.charAt(0) == 'e' || klasseTemp.charAt(0) == 'q') {
            klasseTemp = Character.toUpperCase(klasseTemp.charAt(0)) + klasseTemp.substring(1);
        } else {
            int indexLetter = klasseTemp.length() - 1;
            klasseTemp = klasseTemp.substring(0, indexLetter) + Character.toUpperCase(klasseTemp.charAt(indexLetter)); // Formatänderung
        }

        final String klasse = klasseTemp;
        System.out.println("Klasse: " + klasse);
        dataAccess(klasse);

        heuteMorgen.setOnClickListener(view12 -> switchData(heuteMorgen, checkbox, klasse));

        checkbox.setOnClickListener(view1 -> switchData(heuteMorgen, checkbox, klasse));

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_vertretungsplan);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Intent intent = new Intent(getContext(), LoadingActivity.class);
            intent.putExtra("fragmentIndex", 1);
            intent.putExtra("toDownload", new int[]{1});
            startActivity(intent);
        });
    }

    private void switchData(ToggleButton heuteMorgen, CheckBox checkBox, String klasse) {
        daten.clear();
        String heuteMorgenString = !heuteMorgen.isChecked() ? "heute" : "morgen";
        if (checkBox.isChecked()) {
            daten.addAll(dataStorage.getVertretungsplan().getData().get(heuteMorgenString));
        } else {
            for (Vertretung v : dataStorage.getVertretungsplan().getData().get(heuteMorgenString)) {
                if (v.getKlasse().contains(klasse) || v.getKlasse().contains("0" + klasse)) daten.add(v); // wichtig wegen der Vertretungsplan-Formatänderung: Alt: 6c, neu 06C
            }
        }
        setDateText(heuteMorgenString);
        vertretungenAdapter[0].notifyDataSetChanged();
        ((TextView) getActivity().findViewById(R.id.placeHolderText)).setText(daten.isEmpty() ? "Keine Vertretungen" : "");
    }

    public void dataAccess(String klasse) {
        daten.clear();
        for (Vertretung v : dataStorage.getVertretungsplan().getData().get("heute")) {
            if (v.getKlasse().contains(klasse)) daten.add(v);
        }
        if (daten.isEmpty()) {
            ((TextView) getActivity().findViewById(R.id.placeHolderText)).setText("Keine Vertretungen");
        } else {
            ((TextView) getActivity().findViewById(R.id.placeHolderText)).setText("");
        }

        setDateText("heute");
        vertretungenAdapter[0] = new VertretungsplanAdapter(getContext(), daten);
        vertretungenAdapter[0].notifyDataSetChanged();
        lstMenu.setAdapter(vertretungenAdapter[0]);
    }

    private void setDateText(String heuteMorgen) {
        String dateTextRaw = dataStorage.getVertretungsplan().getMeta().get(heuteMorgen);
        if (dateTextRaw == null || dateTextRaw.equals("")) return;
        //Switches day name and date
        String dateText = String.format("%s %s%s", dateTextRaw.substring(dateTextRaw.indexOf(" ") + 1, dateTextRaw.indexOf(",") + 1), dateTextRaw.substring(0, dateTextRaw.indexOf(" ")), dateTextRaw.substring(dateTextRaw.indexOf(",") + 1));
        //removes Woche A/B
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.dateText)).setText(dateText.substring(0, dateText.indexOf("Woche")));
    }
}

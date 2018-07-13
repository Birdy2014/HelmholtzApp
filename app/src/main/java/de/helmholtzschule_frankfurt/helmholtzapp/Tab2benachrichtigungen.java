package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Tab2benachrichtigungen extends Fragment {

    public ListView lstMenu;
    public ArrayList<Benachrichtigung> daten = new ArrayList<>();
    public BenachrichtigungAdapter menuAdapter;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2benachrichtigungen, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        int heuteMorgenInt = ((ToggleButton)getActivity().findViewById(R.id.toggleButtonSwitch)).isChecked() ? 1 : 0;
        for(String s : dataStorage.getVertretungsplan().getNachrichten().get(heuteMorgenInt)){
            if(s.contains("Abwesende Klassen"))daten.add(new Benachrichtigung(s.substring(0, 17) + ":\n" + s.substring(18)));
            else if(s.contains("Blockierte Räume"))daten.add(new Benachrichtigung(s.substring(0, 16) + ":\n" + s.substring(17)));
            else if(s.contains("Betroffene Klassen"))daten.add(new Benachrichtigung(s.substring(0, 18) + ":\n" + s.substring(19)));
            else if(s.contains("Betroffene Räume"))daten.add(new Benachrichtigung(s.substring(0, 16) + ":\n" + s.substring(17)));
            else daten.add(new Benachrichtigung(s));
        }
        ((TextView)getActivity().findViewById(R.id.tab6Date)).setText(dataStorage.getVertretungsplan().getDate(heuteMorgenInt));
        menuAdapter = new BenachrichtigungAdapter(this.getContext(), daten);
        lstMenu = (ListView) getView().findViewById(R.id.nachrichtenview);
        lstMenu.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();

        super.onViewCreated(view, savedInstanceState);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_benachrichtigungen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                intent.putExtra("fragmentIndex", 2);
                startActivity(intent);
            }
        });
    }
}
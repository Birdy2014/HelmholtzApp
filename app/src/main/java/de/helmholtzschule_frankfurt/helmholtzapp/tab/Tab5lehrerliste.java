package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.item.LehrerItem;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.adapter.LehrerAdapter;


public class Tab5lehrerliste extends Fragment {

    public ListView lstMenu;
    public ArrayList<LehrerItem> daten = new ArrayList<>();
    public LehrerAdapter menuAdapter;
    DataStorage dataStorage = DataStorage.getInstance();
    SearchView searchView;

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
        searchView = getView().findViewById(R.id.lehrerliste_search);
        searchView.post(() -> {
            searchView.setQuery("", false);
            view.requestFocus();
        });
        lstMenu.setOnItemClickListener((adapterView, view1, i, l) -> {
            String itemString = ((LehrerItem)lstMenu.getItemAtPosition(i)).getText();
            String email = itemString.substring(itemString.indexOf("\n") + 1);

            String uriString = "mailto:" + email;

            Uri uri = Uri.parse(uriString);

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("text/plain");
            emailIntent.setData(uri);
            startActivity(Intent.createChooser(emailIntent, "Senden via"));
        });

        lstMenu.setAdapter(menuAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean contains = false;
                for(LehrerItem item : daten){
                    if(item.getText().contains(query))contains = true;
                }
                if(contains){
                    menuAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getActivity(), "Kein Treffer",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                boolean contains = false;
                for(LehrerItem item : daten){
                    if(item.getText().contains(newText))contains = true;
                }
                if(contains){
                    menuAdapter.getFilter().filter(newText);
                }else{
                    Toast.makeText(getActivity(), "Kein Treffer",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}

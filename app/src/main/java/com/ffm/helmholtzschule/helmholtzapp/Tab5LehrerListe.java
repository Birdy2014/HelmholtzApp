package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import io.github.birdy2014.VertretungsplanLib.Vertretung;


public class Tab5LehrerListe extends Fragment {

    DataStorage dataStorage = DataStorage.getInstance();

    String[] list = dataStorage.getLehrerliste();

    private ListView lehrerlistView;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab5_lehrerliste, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lehrerlistView = (ListView) getView().findViewById(R.id.lehrer_list_view);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);

        lehrerlistView.setAdapter(adapter);
    }
}

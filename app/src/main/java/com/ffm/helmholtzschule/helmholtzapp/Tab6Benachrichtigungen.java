package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Intent;
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

public class Tab6Benachrichtigungen extends Fragment {

    DataStorage dataStorage = DataStorage.getInstance();

    String[] listToday = dataStorage.getVertretungsplan().getNachrichten().get(0).toArray(new String[]{});
    String[] listTomorrow = dataStorage.getVertretungsplan().getNachrichten().get(1).toArray(new String[]{});


    private ListView nachrichtenView;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab6_benachrichtigungen, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        nachrichtenView = (ListView) getView().findViewById(R.id.nachrichtenview);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listToday);
        ((TextView)getView().findViewById(R.id.tab6Date)).setText(dataStorage.getVertretungsplan().getDate(0));

        nachrichtenView.setAdapter(adapter);
        ToggleButton button = getView().findViewById(R.id.toggleButtonSwitch);
        button.setOnClickListener(view1 -> {
            if(button.isChecked()){
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listTomorrow);
                nachrichtenView.setAdapter(adapter);
                ((TextView)getView().findViewById(R.id.tab6Date)).setText(dataStorage.getVertretungsplan().getDate(1));
            }
            else {
                adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listToday);
                nachrichtenView.setAdapter(adapter);
                ((TextView)getView().findViewById(R.id.tab6Date)).setText(dataStorage.getVertretungsplan().getDate(0));
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_benachrichtigungen);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                startActivity(intent);
            }
        });
    }
}
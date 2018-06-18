package com.ffm.helmholtzschule.helmholtzapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab1news extends Fragment {
    public ListView lstMenu;
    public ArrayList<News> daten;
    public NewsAdapter menuAdapter;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        daten = new ArrayList<News>();

        menuAdapter = new NewsAdapter(this.getContext(), dataStorage.getNews());
        lstMenu = (ListView) getView().findViewById(R.id.lstNews);
        lstMenu.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        News akt = (News) parent.getItemAtPosition(position);
                        String url = akt.getUrl();

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
        );

        super.onViewCreated(view, savedInstanceState);
    }
}

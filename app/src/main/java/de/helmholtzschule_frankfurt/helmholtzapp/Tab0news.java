package de.helmholtzschule_frankfurt.helmholtzapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab0news extends Fragment {
    public ListView lstMenu;
    public ArrayList<News> daten;
    public NewsAdapter menuAdapter;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab0news, container, false);
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
                        startActivity(Intent.createChooser(intent, "Öffnen in"));
                    }
                }
        );

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_news);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                intent.putExtra("fragmentIndex", 0);
                intent.putExtra("toDownload", new int[]{0});
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Tab4mensa extends Fragment {
    MensaAdapter mensaAdapter;
    ListView lstMenu;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3mensa, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mensaAdapter = new MensaAdapter(getView().getContext(), dataStorage.getGerichte());
        lstMenu = (ListView) getView().findViewById(R.id.mensaMenu);

        lstMenu.setAdapter(mensaAdapter);
        mensaAdapter.notifyDataSetChanged();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swiperefresh_mensa);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getContext(), LoadingActivity.class);
                intent.putExtra("fragmentIndex", 4);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}

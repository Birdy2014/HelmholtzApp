package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.activity.LoadingActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.adapter.MensaAdapter;

public class Tab4mensa extends Fragment {
    MensaAdapter mensaAdapter;
    ListView lstMenu;
    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4mensa, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mensaAdapter = new MensaAdapter(getView().getContext(), dataStorage.getMensaEssen());
        lstMenu = (ListView) getView().findViewById(R.id.mensaMenu);

        lstMenu.setAdapter(mensaAdapter);
        mensaAdapter.notifyDataSetChanged();

        SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swiperefresh_mensa);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Intent intent = new Intent(getContext(), LoadingActivity.class);
            intent.putExtra("fragmentIndex", 4);
            intent.putExtra("toDownload", new int[]{2});
            startActivity(intent);
        });
        ((TextView) getActivity().findViewById(R.id.mensaplanWeek)).setText("Kalenderwoche " + dataStorage.getMensaWeek());
        super.onViewCreated(view, savedInstanceState);
    }
}

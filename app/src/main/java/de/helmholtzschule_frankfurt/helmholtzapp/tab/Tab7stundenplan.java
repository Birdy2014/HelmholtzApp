package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.adapter.StundenplanAdapter;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanCell;

public class Tab9Stundenplan extends Fragment {

    DataStorage dataStorage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab7stundenplan, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String[] days = {"Zeit", "Mo.", "Di.", "Mi.", "Do.", "Fr."};
        GridView daysHeader = getView().findViewById(R.id.stundenplanDayHeader);
        ArrayList<StundenplanCell> header = new ArrayList<>();
        for(String s : days)header.add(new StundenplanCell(s));
        StundenplanAdapter hAdapter = new StundenplanAdapter(getContext(), header);
        daysHeader.setAdapter(hAdapter);

        StundenplanAdapter spAdapter = new StundenplanAdapter(getContext(), dataStorage.getStundenplan());
        GridView gridView = (GridView) getView().findViewById(R.id.stundenplanGrid);

        gridView.setAdapter(spAdapter);

        super.onViewCreated(view, savedInstanceState);
    }
}

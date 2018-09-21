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
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Objects;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.activity.LoadingActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.adapter.BenachrichtigungAdapter;
import de.helmholtzschule_frankfurt.helmholtzapp.item.Benachrichtigung;

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
        ToggleButton button = getActivity().findViewById(R.id.toggleButtonSwitch);
        changeList(button);
        button.setOnClickListener(click -> {
            changeList(button);
        });

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
    private void changeList(ToggleButton button){
        String heuteMorgenString = button.isChecked() ? "morgen" : "heute";
        daten.clear();
        for (String s : dataStorage.getBenachrichtigungsplan().getData().get(heuteMorgenString)) {
            daten.add(new Benachrichtigung(s));
        }
        setDateText(heuteMorgenString);
        menuAdapter = new BenachrichtigungAdapter(this.getContext(), daten);
        lstMenu = (ListView) getView().findViewById(R.id.nachrichtenview);
        lstMenu.setAdapter(menuAdapter);
        menuAdapter.notifyDataSetChanged();
    }

    private void setDateText(String heuteMorgen) {
        String dateTextRaw = dataStorage.getVertretungsplan().getMeta().get(heuteMorgen);
        String dateText = String.format("%s %s%s", dateTextRaw.substring(dateTextRaw.indexOf(" ") + 1, dateTextRaw.indexOf(",") + 1), dateTextRaw.substring(0, dateTextRaw.indexOf(" ")), dateTextRaw.substring(dateTextRaw.indexOf(",") + 1));
        ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.dateText)).setText(dateText.substring(0, dateText.indexOf("Woche")));
    }
}
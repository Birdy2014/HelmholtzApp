package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


public class Tab8settings extends Fragment {

    DataStorage storage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab8settings, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View dashBoard = getView().findViewById(R.id.settings_dashboard);
        dashBoard.setOnClickListener(click -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://helmholtz-database.000webhostapp.com"));
            startActivity(browser);
            this.getActivity().finishAffinity();
        });

        Spinner hourSelect = getView().findViewById(R.id.settings_edit_hours);
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 6; i < 12; i++){
            list.add(i);
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        hourSelect.setAdapter(adapter);
        hourSelect.setSelection(list.indexOf(storage.hours));
        hourSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage.hours = (int)hourSelect.getSelectedItem();
                storage.saveStundenplan(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        CheckBox box = getActivity().findViewById(R.id.settings_edit_push_notes);
        box.setOnClickListener(view1 -> {
            storage.setPushNotificationsActive(box.isChecked(), getActivity());
        });
        View exportView = getView().findViewById(R.id.settings_export);
        exportView.setOnClickListener(click -> {
            storage.exportStundenplan(getActivity());
        });
        View importView = getView().findViewById(R.id.settings_import);
        importView.setOnClickListener(click -> {
            if(storage.importStundenplan(getActivity())){
                hourSelect.setSelection(list.size() - 1);
            }
        });
        View clearView = getView().findViewById(R.id.settings_clear);
        clearView.setOnClickListener(click -> {
            for(int i = 0; i < storage.getStundenplan().size(); i++){
                if(storage.getStundenplan().get(i) instanceof StundenplanItem){
                    storage.getStundenplan().set(i, new StundenplanItem(null, null, null, null));
                }
            }
            storage.saveStundenplan(false);
            Toast.makeText(getActivity().getBaseContext(), "Stundenplan wurde erfolgreich geleert.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(!(grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission denied to access your location", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

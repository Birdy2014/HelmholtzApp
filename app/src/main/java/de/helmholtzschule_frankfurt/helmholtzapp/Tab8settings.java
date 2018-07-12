package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner dropdown = (Spinner) getView().findViewById(R.id.settings_edit_klasse);


        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, storage.klassen);
        dropdown.setAdapter(adapter);

        dropdown.setSelection(Arrays.asList(storage.klassen).indexOf(storage.getKlasse(getActivity())));

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storage.setKlasse(getActivity(), dropdown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        Spinner hourSelect = getView().findViewById(R.id.settings_edit_hours);
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 6; i < 12; i++){
            list.add(i);
        }
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        hourSelect.setAdapter(adapter);
        hourSelect.setSelection(list.indexOf(getActivity().getSharedPreferences("MySPFILE", 0).getInt("stundenzahl", 9)));
        hourSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DataStorage.getInstance().hours = (int)hourSelect.getSelectedItem();
                SharedPreferences mySPR = getActivity().getSharedPreferences("MySPFILE", 0);
                SharedPreferences.Editor editor = mySPR.edit();
                editor.putInt("stundenzahl", (int)hourSelect.getSelectedItem());
                editor.apply();
                DataStorage.getInstance().fillStundenplan(getActivity());
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

package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.activity.LoginActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.item.StundenplanItem;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


public class Tab8settings extends Fragment {

    DataStorage storage = DataStorage.getInstance();
    HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();

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
                if(hasHourOverflow((int)hourSelect.getSelectedItem())){
                    System.out.println("Hour Overflow");

                    Dialog dialog = new Dialog(getContext());
                    View dialogView = View.inflate(getContext(), R.layout.overflow_dialog, null);
                    dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    Button cancelButton = dialogView.findViewById(R.id.close_button_cancel);
                    cancelButton.setOnClickListener(click -> {
                        hourSelect.setSelection(list.indexOf(storage.hours));
                        dialog.cancel();
                    });
                    Button continueButton = dialogView.findViewById(R.id.close_button_close);
                    continueButton.setOnClickListener(click -> {
                        storage.hours = (int) hourSelect.getSelectedItem();
                        storage.saveStundenplan(true);
                        dialog.cancel();
                    });
                    dialog.show();
                }
                else {
                    storage.hours = (int) hourSelect.getSelectedItem();
                    storage.saveStundenplan(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        CheckBox box = getActivity().findViewById(R.id.settings_edit_push_notes);
        box.setOnClickListener(view1 -> {
            storage.setPushNotificationsActive(box.isChecked());
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

        Spinner standardTab = getView().findViewById(R.id.settings_edit_tab);
        String[] tabs = {"News", "Vertretungsplan", "Benachrichtigungen", "Kalender", "Mensaplan", "Lehrerliste", "Hausaufgaben", "Stundenplan"};
        ArrayList<String> indices = new ArrayList<>(Arrays.asList(tabs));

        ArrayAdapter indexAdapter = new ArrayAdapter(getContext(), R.layout.spinner_dropdown_item, indices);
        standardTab.setAdapter(indexAdapter);
        SharedPreferences mySPR = getActivity().getSharedPreferences("MySPFILE", 0);
        standardTab.setSelection(mySPR.getInt("standardTab", 0));
        standardTab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = mySPR.edit();
                editor.putInt("standardTab", i);
                System.out.println(i);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        View logged = getActivity().findViewById(R.id.settings_logged);
        logged.setOnClickListener(click -> {
            client.logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        TextView loggedAs = getActivity().findViewById(R.id.settings_text_logged);
        loggedAs.setText("Angemeldet als: " + client.getUsername());
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
    private boolean hasHourOverflow(int selected){
        int lastIndex = 0;
        for(int i = 0; i < storage.getStundenplan().size(); i++){
            if(storage.getStundenplan().get(i) instanceof StundenplanItem && !storage.getStundenplan().get(i).getName().equals(""))lastIndex = i;
        }
        return selected * 6 <= lastIndex;
    }
}

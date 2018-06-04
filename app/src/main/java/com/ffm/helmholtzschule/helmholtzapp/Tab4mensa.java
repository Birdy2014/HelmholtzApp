package com.ffm.helmholtzschule.helmholtzapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Tab4mensa extends Fragment {
    ArrayList<Mensaplan> gerichte;
    MensaAdapter mensaAdapter;
    ListView lstMenu;
    String rawData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab4mensa, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rawData = download("https://unforkablefood.000webhostapp.com");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Download error");
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(rawData);

        Gson gson = new Gson();
        String[][] data = gson.fromJson(rawData, new TypeToken<String[][]>() {
        }.getType());

        gerichte = new ArrayList<>();

        gerichte.add(new Mensaplan("Montag", data[0][0], data[0][1], data[0][2]));
        gerichte.add(new Mensaplan("Dienstag", data[1][0], data[1][1], data[1][2]));
        gerichte.add(new Mensaplan("Mittwoch", data[2][0], data[2][1], data[2][2]));
        gerichte.add(new Mensaplan("Donnerstag", data[3][0], data[3][1], data[3][2]));
        gerichte.add(new Mensaplan("Freitag", data[4][0], data[4][1], data[4][2]));

        mensaAdapter = new MensaAdapter(getView().getContext(), gerichte); //TODO alte Zeilen bei neuem öffnen des Tabs entfernen
        lstMenu = (ListView) getView().findViewById(R.id.mensaMenu);

        lstMenu.setAdapter(mensaAdapter);
        mensaAdapter.notifyDataSetChanged();

        super.onViewCreated(view, savedInstanceState);
    }

    private String download(String website) throws IOException {
        URLConnection connection = new URL(website).openConnection();
        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        return data;
    }
}

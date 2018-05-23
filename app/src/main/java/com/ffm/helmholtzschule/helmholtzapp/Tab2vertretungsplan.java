package com.ffm.helmholtzschule.helmholtzapp;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;

/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab2vertretungsplan extends Fragment {

    public String VertretungsplanPage1;
    public String VertretungsplanPage2;
    public String VertretungsplanPage3;
    public String VertretungsplanPage4;
    public String VertretungsplanMorgenPage1;
    public String VertretungsplanMorgenPage2;
    public String VertretungsplanMorgenPage3;
    public String VertretungsplanMorgenPage4;
    public static HashMap<String, ArrayList<Object[]>> vertretungsplanKlassen = new HashMap<>();
    public static HashMap<String, ArrayList<Object[]>> vertretungsplanKlassenMorgen = new HashMap<>();
    public static ArrayList<Vertretung> vertretungenHeute = new ArrayList<>();
    public static ArrayList<Vertretung> vertretungenMorgen = new ArrayList<>();
    public static ArrayList<String> nachrichtenHeute = new ArrayList<>();
    public static ArrayList<String> nachrichtenMorgen = new ArrayList<>();
    public static int tasksFinished = 0;
    public static String dateHeute;
    public static String dateMorgen;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab2vertretungsplan, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<Vertretung> daten = new ArrayList<Vertretung>();
        ArrayList<Vertretung> meineDaten = new ArrayList<Vertretung>();


        /*daten.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Hinw.", "Art"));
        daten.add(new Vertretung("8a", "1", "D", "Sta", "Cmp", "003", "Sachen!", "Vertretung"));
        daten.add(new Vertretung("6a", "3", "M", "Bl", " ", "202", "test123", "Raumänderung"));
        /*for(int i = 0; i < vertretungenHeute.size(); i++){
            daten.add(vertretungenHeute.get(i));
        }*/





        ListAdapter MenuAdapter = new VertretungsplanAdapter(getContext(), daten);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(MenuAdapter);

        SharedPreferences mySPR = this.getContext().getSharedPreferences("MySPFILE", 0);
        String klasse1 = mySPR.getString("myKey2", "8a"); // TODO: 8a wieder rausnehmen
        String username = mySPR.getString("username", "");
        String password = mySPR.getString("password", "");

        downloadVertretungsplan(username, password);

        super.onViewCreated(view, savedInstanceState);

        final ToggleButton klasseButton = (ToggleButton) getView().findViewById(R.id.toggleButton3);
        final ToggleButton heuteMorgen = (ToggleButton) getView().findViewById(R.id.butShowAll);

        if(klasse1.charAt(0) == 'E' || klasse1.charAt(0) == 'Q' || klasse1.charAt(0) == 'e' || klasse1.charAt(0) == 'e'){
            klasse1 = Character.toUpperCase(klasse1.charAt(0)) + klasse1.substring(1);
        }
        else{
            int indexLetter = klasse1.length() - 1;
            klasse1 = klasse1.substring(0, indexLetter) + Character.toLowerCase(klasse1.charAt(indexLetter));
        }
        final String klasse = klasse1;
        System.out.println("Klasse: " + klasse);
        heuteMorgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                if(klasseButton.isChecked() && heuteMorgen.isChecked()){
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), vertretungenMorgen);
                }
                else if(klasseButton.isChecked() && !heuteMorgen.isChecked()){
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), vertretungenHeute);
                }
                else if(!klasseButton.isChecked() && heuteMorgen.isChecked()){
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    for(Vertretung v : vertretungenMorgen){
                        if(v.getKlasse().contains(klasse))list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                }
                else{
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    for(Vertretung v : vertretungenHeute){
                        if(v.getKlasse().contains(klasse))list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                }
                ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
            }
        });



        klasseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VertretungsplanAdapter vertretungenAdapter;
                if(klasseButton.isChecked() && heuteMorgen.isChecked()){
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), vertretungenMorgen);
                }
                else if(klasseButton.isChecked() && !heuteMorgen.isChecked()){
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), vertretungenHeute);
                }
                else if(!klasseButton.isChecked() && heuteMorgen.isChecked()){
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    for(Vertretung v : vertretungenMorgen){
                        if(v.getKlasse().contains(klasse))list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                }
                else{
                    ArrayList<Vertretung> list = new ArrayList<Vertretung>();
                    for(Vertretung v : vertretungenHeute){
                        if(v.getKlasse().contains(klasse))list.add(v);
                    }
                    vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
                }
                ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);
                lstMenu.setAdapter(vertretungenAdapter);
                vertretungenAdapter.notifyDataSetChanged();
            }
        });


    }




    /*public void taskFinished(){
        tasksFinished++;
        if (tasksFinished == 7){
            int anzahlSeitenHeute = pageCountVertretungsplan(true);
            for (int i = 0; i < anzahlSeitenHeute; i++){
                switch (i){
                    case 0:
                        cutString(VertretungsplanPage1, vertretungsplanKlassen, true);
                        break;
                    case 1:
                        cutString(VertretungsplanPage2, vertretungsplanKlassen, true);
                        break;
                    case 2:
                        cutString(VertretungsplanPage3, vertretungsplanKlassen, true);
                        break;
                    case 3:
                        cutString(VertretungsplanPage4, vertretungsplanKlassen, true);
                        break;
                }
            }

            int anzahlSeitenMorgen = pageCountVertretungsplan(false);
            for (int i = 0; i < anzahlSeitenMorgen; i++){
                switch (i){
                    case 0:
                        cutString(VertretungsplanMorgenPage1, vertretungsplanKlassenMorgen, false);
                        break;
                    case 1:
                        cutString(VertretungsplanMorgenPage2, vertretungsplanKlassenMorgen, false);
                        break;
                    case 2:
                        cutString(VertretungsplanMorgenPage3, vertretungsplanKlassenMorgen, false);
                        break;
                }
            }
            debugAll();
        }
    }*/




    public static void generateVertretungen(boolean heute, HashMap<String, ArrayList<Object[]>> map){
        if(heute) {
            Object[] keys = vertretungsplanKlassen.keySet().toArray();
            Arrays.sort(keys);

            for (Object object : keys) {
                for (int i = 0; i < vertretungsplanKlassen.get(object).size(); i++) {
                    Vertretung vertretung = new Vertretung((String) object, i, map);
                    vertretungenHeute.add(vertretung);
                }
            }
            vertretungsplanKlassen.clear();
        } else{
            Object[] keys = vertretungsplanKlassenMorgen.keySet().toArray();
            Arrays.sort(keys);
            System.out.println("Länge Keys morgen: " + keys.length);

            for (Object object : keys) {
                for (int i = 0; i < vertretungsplanKlassenMorgen.get(object).size(); i++) {
                    Vertretung vertretung = new Vertretung((String) object, i, map);
                    vertretungenMorgen.add(vertretung);
                }
            }
            vertretungsplanKlassenMorgen.clear();
        }
    }

    protected void downloadVertretungsplan(String username, String password) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Vertretungsplan vertretungsplan = new Vertretungsplan();
                try {
                    VertretungsplanPage1 = vertretungsplan.downloadVertretungsplan(1, 1, username, password);
                    VertretungsplanPage2 = vertretungsplan.downloadVertretungsplan(1, 2, username, password);
                    VertretungsplanPage3 = vertretungsplan.downloadVertretungsplan(1, 3, username, password);
                    VertretungsplanPage4 = vertretungsplan.downloadVertretungsplan(1, 4, username, password);
                    VertretungsplanMorgenPage1 = vertretungsplan.downloadVertretungsplan(2, 1, username, password);
                    VertretungsplanMorgenPage2 = vertretungsplan.downloadVertretungsplan(2, 2, username, password);
                    VertretungsplanMorgenPage3 = vertretungsplan.downloadVertretungsplan(2, 3, username, password);
                    //VertretungsplanMorgenPage4 = vertretungsplan.downloadVertretungsplan(2, 4, username, password);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int anzahlSeitenHeute = pageCountVertretungsplan(true);
        for (int i = 0; i < anzahlSeitenHeute; i++){
            switch (i){
                case 0:
                    cutString(VertretungsplanPage1, vertretungsplanKlassen, true);
                    break;
                case 1:
                    cutString(VertretungsplanPage2, vertretungsplanKlassen, true);
                    break;
                case 2:
                    cutString(VertretungsplanPage3, vertretungsplanKlassen, true);
                    break;
                case 3:
                    cutString(VertretungsplanPage4, vertretungsplanKlassen, true);
                    break;
            }
        }

        int anzahlSeitenMorgen = pageCountVertretungsplan(false);
        for (int i = 0; i < anzahlSeitenMorgen; i++){
            switch (i){
                case 0:
                    cutString(VertretungsplanMorgenPage1, vertretungsplanKlassenMorgen, false);
                    break;
                case 1:
                    cutString(VertretungsplanMorgenPage2, vertretungsplanKlassenMorgen, false);
                    break;
                case 2:
                    cutString(VertretungsplanMorgenPage3, vertretungsplanKlassenMorgen, false);
                    break;
            }
        }
        debugAll();

    }

    public int pageCountVertretungsplan(boolean heute){
        try{
            if (heute){
                int indexDivDateAuf = VertretungsplanPage1.indexOf("<div class=\"mon_title\">");
                int indexDivDateZu = VertretungsplanPage1.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanPage1 = VertretungsplanPage1.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanPage1.contains("Seite")) dateVertretungsplanPage1 = dateVertretungsplanPage1.substring(0, dateVertretungsplanPage1.length() - 14);
                dateHeute = dateVertretungsplanPage1;
                //System.out.println(dateVertretungsplanPage1);

                indexDivDateAuf = VertretungsplanPage2.indexOf("<div class=\"mon_title\">");
                indexDivDateZu = VertretungsplanPage2.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanPage2 = VertretungsplanPage2.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanPage2.contains("Seite")) dateVertretungsplanPage2 = dateVertretungsplanPage2.substring(0, dateVertretungsplanPage2.length() - 14);
                //System.out.println(dateVertretungsplanPage2);

                indexDivDateAuf = VertretungsplanPage3.indexOf("<div class=\"mon_title\">");
                indexDivDateZu = VertretungsplanPage3.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanPage3 = VertretungsplanPage3.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanPage3.contains("Seite")) dateVertretungsplanPage3 = dateVertretungsplanPage3.substring(0, dateVertretungsplanPage3.length() - 14);
                //System.out.println(dateVertretungsplanPage3);

                indexDivDateAuf = VertretungsplanPage4.indexOf("<div class=\"mon_title\">");
                indexDivDateZu = VertretungsplanPage4.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanPage4 = VertretungsplanPage4.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanPage4.contains("Seite")) dateVertretungsplanPage4 = dateVertretungsplanPage4.substring(0, dateVertretungsplanPage4.length() - 14);
                //System.out.println(dateVertretungsplanPage4);


                if (dateVertretungsplanPage1.equals(dateVertretungsplanPage2) && dateVertretungsplanPage2.equals(dateVertretungsplanPage3) && dateVertretungsplanPage3.equals(dateVertretungsplanPage4)){
                    return 4;
                } else if (dateVertretungsplanPage1.equals(dateVertretungsplanPage2) && dateVertretungsplanPage2.equals(dateVertretungsplanPage3)){
                    return 3;
                } else if (dateVertretungsplanPage1.equals(dateVertretungsplanPage2)){
                    return 2;
                } else {
                    return 1;
                }
            } else {
                int indexDivDateAuf = VertretungsplanMorgenPage1.indexOf("<div class=\"mon_title\">");
                int indexDivDateZu = VertretungsplanMorgenPage1.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanMorgenPage1 = VertretungsplanMorgenPage1.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanMorgenPage1.contains("Seite")) dateVertretungsplanMorgenPage1 = dateVertretungsplanMorgenPage1.substring(0, dateVertretungsplanMorgenPage1.length() - 14);
                dateMorgen = dateVertretungsplanMorgenPage1;
                //System.out.println(dateVertretungsplanMorgenPage1);

                indexDivDateAuf = VertretungsplanMorgenPage2.indexOf("<div class=\"mon_title\">");
                indexDivDateZu = VertretungsplanMorgenPage2.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanMorgenPage2 = VertretungsplanMorgenPage2.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanMorgenPage2.contains("Seite")) dateVertretungsplanMorgenPage2 = dateVertretungsplanMorgenPage2.substring(0, dateVertretungsplanMorgenPage2.length() - 14);
                //System.out.println(dateVertretungsplanMorgenPage2);

                indexDivDateAuf = VertretungsplanMorgenPage3.indexOf("<div class=\"mon_title\">");
                indexDivDateZu = VertretungsplanMorgenPage3.indexOf("</div>", indexDivDateAuf);
                String dateVertretungsplanMorgenPage3 = VertretungsplanMorgenPage3.substring(indexDivDateAuf + 23, indexDivDateZu);
                if (dateVertretungsplanMorgenPage3.contains("Seite")) dateVertretungsplanMorgenPage3 = dateVertretungsplanMorgenPage3.substring(0, dateVertretungsplanMorgenPage3.length() - 14);
                //System.out.println(dateVertretungsplanMorgenPage3);

                if (dateVertretungsplanMorgenPage1.equals(dateVertretungsplanMorgenPage2) && dateVertretungsplanMorgenPage2.equals(dateVertretungsplanMorgenPage3)){
                    return 3;
                } else if (dateVertretungsplanMorgenPage1.equals(dateVertretungsplanMorgenPage2)){
                    return 2;
                } else {
                    return 1;
                }
            }
        } catch (NullPointerException e){
            System.out.println("Ich habe gefailt");
            e.printStackTrace();
            return 0;
        }
    }

    public void cutString(String s1, HashMap<String, ArrayList<Object[]>> map, boolean heute){
        //System.out.println("Vertretungsplan: " + s1);
        Scanner scannerVertretungsplanPage1 = new Scanner(s1);

        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> nachrichten = new ArrayList<>();
        while (scannerVertretungsplanPage1.hasNextLine()) {
            String s = scannerVertretungsplanPage1.nextLine();
            /*if(s.equals("<table class=\"info\" >")){
                while (!s.equals("<br>") && scannerVertretungsplanPage1.hasNextLine()){
                    nachrichten.add(scannerVertretungsplanPage1.nextLine());
                }
            }*/
            if (s.equals("<table class=\"mon_list\" >")) {
                //System.out.println(s);
                while (!s.equals("</table>")) {
                    s = scannerVertretungsplanPage1.nextLine();
                    lines.add(s);
                }
            }
        }

        lines.remove(0);
        lines.remove(lines.size() - 1);
        if (lines.size() == 0) {
            //System.out.println("Keine Vertretungen!");
            return;
        }

        for (String x : lines) {
            try {
                String klasse;
                String stunde = "NA";
                String fach = "NA";
                String vertretung = "NA";
                String fuer = "NA";
                String raum = "NA";
                String hinweis = "NA";
                String art = "NA";

                int indexTd = x.indexOf("<td");
                int indexB = x.indexOf("><b>", indexTd);
                if (indexB != -1) {

                    //Weiß
                    if (!x.substring(indexB + 4, indexB + 6).equals("<s")) {
                        klasse = x.substring(indexB + 4, indexB + 6);
                        int indexTDAuf = x.indexOf("<td", indexB);
                        int tdZu = x.indexOf("</td>", indexTDAuf);
                        if (x.charAt(tdZu - 2) == ' ') {
                            //Longer
                            stunde = x.substring(tdZu - 5, tdZu);
                        } else {
                            stunde = x.substring(tdZu - 1, tdZu);
                        }
                        tdZu = x.indexOf("</td>", tdZu + 4);
                        if (x.charAt(tdZu - 1) == ';' || x.charAt(tdZu - 1) == '-') {
                            //Nicht angegeben
                        } else {
                            if (x.charAt(tdZu - 2) == '>') {
                                fach = x.substring(tdZu - 1, tdZu);
                            } else if (x.charAt(tdZu - 3) == '>') {
                                fach = x.substring(tdZu - 2, tdZu);
                            } else {
                                fach = x.substring(tdZu - 3, tdZu);
                            }
                        }

                        tdZu = x.indexOf("</td>", tdZu + 4);
                        if (x.charAt(tdZu - 1) == ';' || x.charAt(tdZu - 1) == '-' || x.charAt(tdZu - 1) == '+') {
                            //Nicht angegeben
                        } else {
                            if (x.charAt(tdZu - 2) == '>') {
                                vertretung = x.substring(tdZu - 1, tdZu);
                            } else if (x.charAt(tdZu - 3) == '>') {
                                vertretung = x.substring(tdZu - 2, tdZu);
                            } else {
                                vertretung = x.substring(tdZu - 3, tdZu);
                            }
                        }

                        tdZu = x.indexOf("</td>", tdZu + 4);
                        if (x.charAt(tdZu - 1) == ';' || x.charAt(tdZu - 1) == '-' || x.charAt(tdZu - 1) == '+') {
                            //Nicht angegeben
                        } else {
                            if (x.charAt(tdZu - 2) == '>') {
                                fuer = x.substring(tdZu - 1, tdZu);
                            } else if (x.charAt(tdZu - 3) == '>') {
                                fuer = x.substring(tdZu - 2, tdZu);
                            } else {
                                fuer = x.substring(tdZu - 3, tdZu);
                            }
                        }

                        tdZu = x.indexOf("</td>", tdZu + 4);
                        if (x.charAt(tdZu - 1) == ';' || x.charAt(tdZu - 1) == '-' || x.charAt(tdZu - 1) == '+') {
                            //Nicht angegeben
                        } else {
                            if (x.charAt(tdZu - 2) == '>') {
                                raum = x.substring(tdZu - 1, tdZu);
                            } else if (x.charAt(tdZu - 3) == '>') {
                                raum = x.substring(tdZu - 2, tdZu);
                            } else if (x.charAt(tdZu - 4) == '>') {
                                raum = x.substring(tdZu - 3, tdZu);
                            } else {
                                raum = x.substring(tdZu - 4, tdZu);
                            }
                        }

                        tdZu = x.indexOf("</td>", tdZu + 4);
                        int indexTdAuf = x.indexOf("<td", tdZu + 4);
                        //int anfangHinweis = indexTdAuf + 51;
                        int anfangHinweis = x.indexOf('>', indexTdAuf + 4);
                        tdZu = x.indexOf("</td>", indexTdAuf + 4);

                        //System.out.println(x);
                        //System.out.println(klasse);
                        hinweis = x.substring(anfangHinweis + 1, tdZu);
                        if (hinweis.equals("&nbsp;")) hinweis = "NA";

                        indexTdAuf = x.indexOf("<td", tdZu + 4);
                        tdZu = x.indexOf("</td>", indexTdAuf + 4);
                        int anfangArt = x.indexOf('>', indexTdAuf + 4);
                        art = x.substring(anfangArt + 1, tdZu);
                    }
                    //Gelb
                    else {
                        int indexSpanZu = x.indexOf("</span>", indexB);
                        int indexStartKlasse = x.indexOf(">", indexB + 4);
                        klasse = x.substring(indexStartKlasse + 1, indexSpanZu);
                        indexSpanZu = x.indexOf("</span>", indexSpanZu + 4);
                        if (x.charAt(indexSpanZu - 2) == ' ') {
                            //Longer
                            stunde = x.substring(indexSpanZu - 5, indexSpanZu);
                        } else {
                            stunde = x.substring(indexSpanZu - 1, indexSpanZu);
                        }

                        if (x.charAt(x.indexOf("</td>", x.indexOf("</td>", indexSpanZu) + 4) - 1) == ';') {
                            //Nicht angegeben
                        } else {
                            indexSpanZu = x.indexOf("</span>", indexSpanZu + 4);
                            if (x.charAt(indexSpanZu - 1) == ';' || x.charAt(indexSpanZu - 1) == '-') {
                                //Nicht angegeben
                            } else {
                                if (x.charAt(indexSpanZu - 2) == '>') {
                                    fach = x.substring(indexSpanZu - 1, indexSpanZu);
                                } else if (x.charAt(indexSpanZu - 3) == '>') {
                                    fach = x.substring(indexSpanZu - 2, indexSpanZu);
                                } else {
                                    fach = x.substring(indexSpanZu - 3, indexSpanZu);
                                }
                            }
                        }
                        indexSpanZu = x.indexOf("</span>", indexSpanZu + 4);
                        if (x.charAt(indexSpanZu - 1) == ';' || x.charAt(indexSpanZu - 1) == '-' || x.charAt(indexSpanZu - 1) == '+') {
                            //Nicht angegeben
                        } else {
                            if (x.charAt(indexSpanZu - 2) == '>') {
                                vertretung = x.substring(indexSpanZu - 1, indexSpanZu);
                            } else if (x.charAt(indexSpanZu - 3) == '>') {
                                vertretung = x.substring(indexSpanZu - 2, indexSpanZu);
                            } else {
                                vertretung = x.substring(indexSpanZu - 3, indexSpanZu);
                            }
                        }

                        if (x.indexOf("</td>", x.indexOf("</td>", indexSpanZu + 4) + 1) > x.indexOf("</span>", indexSpanZu + 4)) {
                            indexSpanZu = x.indexOf("</span>", indexSpanZu + 4);
                            if (x.charAt(indexSpanZu - 1) == ';' || x.charAt(indexSpanZu - 1) == '-' || x.charAt(indexSpanZu - 1) == '+') {
                                //Nicht angegeben
                            } else {
                                if (x.charAt(indexSpanZu - 2) == '>') {
                                    fuer = x.substring(indexSpanZu - 1, indexSpanZu);
                                } else if (x.charAt(indexSpanZu - 3) == '>') {
                                    fuer = x.substring(indexSpanZu - 2, indexSpanZu);
                                } else {
                                    fuer = x.substring(indexSpanZu - 3, indexSpanZu);
                                }
                            }

                            indexSpanZu = x.indexOf("</span>", indexSpanZu + 4);
                            if (x.charAt(indexSpanZu - 1) == ';' || x.charAt(indexSpanZu - 1) == '-' || x.charAt(indexSpanZu - 1) == '+') {
                                //Nicht angegeben
                            } else {
                                if (x.charAt(indexSpanZu - 2) == '>') {
                                    raum = x.substring(indexSpanZu - 1, indexSpanZu);
                                } else if (x.charAt(indexSpanZu - 3) == '>') {
                                    raum = x.substring(indexSpanZu - 2, indexSpanZu);
                                } else if (x.charAt(indexSpanZu - 4) == '>') {
                                    raum = x.substring(indexSpanZu - 3, indexSpanZu);
                                } else {
                                    raum = x.substring(indexSpanZu - 4, indexSpanZu);
                                }
                            }

                            if (x.indexOf("<span", indexSpanZu + 4) < x.indexOf("</td>", x.indexOf("</td>", indexSpanZu + 4))) {
                                int indexSpanAuf = x.indexOf("<span", indexSpanZu + 4);
                                int anfangHinweis = x.indexOf(">", indexSpanAuf + 4);
                                indexSpanZu = x.indexOf("</span>", indexSpanAuf + 4);

                                hinweis = x.substring(anfangHinweis + 1, indexSpanZu);
                                if (hinweis.equals("&nbsp;")) hinweis = "Nicht angegeben";

                                indexSpanAuf = x.indexOf("<span", indexSpanZu + 4);
                                indexSpanZu = x.indexOf("</span>", indexSpanAuf + 4);
                                int anfangArt = indexSpanAuf + 29;
                                art = x.substring(anfangArt, indexSpanZu);
                            } else {
                                int indexTdzu = x.indexOf("</td>", indexSpanZu);
                                indexTdzu = x.indexOf("</td>", indexTdzu + 4);
                                int indexTdAuf = x.indexOf("<td", indexTdzu);
                                int anfangHinweis = x.indexOf(">", indexTdAuf);
                                indexTdzu = x.indexOf("</td>", indexTdzu + 4);
                                hinweis = x.substring(anfangHinweis + 1, indexTdzu);
                                if (hinweis.equals("&nbsp;")) hinweis = "NA";
                                if (hinweis.contains("<span style=")) {
                                    hinweis = hinweis.substring(29, hinweis.length() - 7);
                                }
                                int indexSpanAuf = x.indexOf("<span", indexTdzu);
                                int anfangArt = x.indexOf(">", indexSpanAuf);
                                indexSpanZu = x.indexOf("</span>", anfangArt);
                                art = x.substring(anfangArt + 1, indexSpanZu);
                            }

                            if (hinweis.equals("&nbsp;")) hinweis = "NA";
                        } else {
                            //Unormal
                            //Für
                            int indexTdAuf = x.indexOf("<td", indexSpanZu);
                            int indexTdZu = x.indexOf("</td>", indexTdAuf);
                            int anfangFuer = x.indexOf(">", indexTdAuf);
                            fuer = x.substring(anfangFuer + 1, indexTdZu);
                            if (fuer.equals("&nbsp;")) fuer = "NA";

                            //Raum
                            int indexSpanAuf = x.indexOf("<span", indexTdZu + 4);
                            int anfangRaum = x.indexOf(">", indexSpanAuf + 4);
                            indexSpanZu = x.indexOf("</span>", anfangRaum);
                            raum = x.substring(anfangRaum + 1, indexSpanZu);

                            //Hinweis
                            indexTdZu = x.indexOf("</td>", x.indexOf("</td>", indexSpanZu + 4) + 4);
                            indexTdAuf = x.indexOf("<td", indexTdZu);
                            int anfangHinweis = x.indexOf(">", indexTdAuf + 4);
                            indexTdZu = x.indexOf("</td>", anfangHinweis);
                            hinweis = x.substring(anfangHinweis + 1, indexTdZu);
                            if (hinweis.equals("&nbsp;")) hinweis = "NA";

                            //Art
                            indexSpanAuf = x.indexOf("<span", indexTdZu);
                            int anfangArt = x.indexOf(">", indexSpanAuf);
                            indexSpanZu = x.indexOf("</span>", anfangArt);
                            art = x.substring(anfangArt + 1, indexSpanZu);
                        }
                    }

                    Object[] infos = new Object[]{stunde, fach, vertretung, fuer, raum, hinweis, art};
                    if (map.get(klasse) != null) {
                        map.get(klasse).add(infos);
                    } else {
                        ArrayList<Object[]> list = new ArrayList<>();
                        list.add(infos);
                        map.put(klasse, list);
                    }
                }
            } catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }


        generateVertretungen(heute, map);

        scannerVertretungsplanPage1 = new Scanner(s1);
        while (scannerVertretungsplanPage1.hasNextLine()) {
            String s = scannerVertretungsplanPage1.nextLine();

            if (s.equals("<table class=\"info\" >")) {
                while (!s.equals("</table>")) {
                    s = scannerVertretungsplanPage1.nextLine();
                    nachrichten.add(s);
                }
            }

        }

        if (heute) {
            nachrichtenHeute.add(dateHeute);
            for (String nachrichtenZeile : nachrichten) {
                nachrichtenHeute.add(Html.fromHtml(nachrichtenZeile).toString());
            }
            if (nachrichtenHeute.size() > 1) nachrichtenHeute.remove(1);
        } else {
            nachrichtenMorgen.add(dateMorgen);
            for (String nachrichtenZeile : nachrichten) {
                nachrichtenMorgen.add(Html.fromHtml(nachrichtenZeile).toString());
            }
            if (nachrichtenMorgen.size() > 1) nachrichtenMorgen.remove(1);
        }

    }

    public void debugAll(){
        System.out.println("Why do java developers wear glasses?\nBecause they can\'t C#.");
        System.out.println("Programmiert von Jonas Schröter und Moritz Vogel.");
        System.out.println("Nachrichten heute:");
        for (String str : nachrichtenHeute){
            System.out.println(str);
        }
        System.out.println("Vertretungen heute:");
        if (vertretungenHeute.size() == 0) System.out.println("Keine Vertretungen");
        for (int i = 0; i < vertretungenHeute.size(); i++){
            System.out.print(vertretungenHeute.get(i).getKlasse());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getStunde());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getFach());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getVertretungslehrer());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getFuerLehrer());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getRaum());
            System.out.print(", ");
            System.out.print(vertretungenHeute.get(i).getHinweis());
            System.out.print(", ");
            System.out.println(vertretungenHeute.get(i).getArt());
        }
        System.out.println("Nachrichten morgen:");
        for (String str : nachrichtenMorgen){
            System.out.println(str);
        }
        System.out.println("Vertretungen morgen:");
        if (vertretungenMorgen.size() == 0) System.out.println("Keine Vertretungen");
        for (int i = 0; i < vertretungenMorgen.size(); i++) {
            System.out.print(vertretungenMorgen.get(i).getKlasse());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getStunde());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getFach());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getVertretungslehrer());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getFuerLehrer());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getRaum());
            System.out.print(", ");
            System.out.print(vertretungenMorgen.get(i).getHinweis());
            System.out.print(", ");
            System.out.println(vertretungenMorgen.get(i).getArt());
        }
        dataAccess();
    }

    public void dataAccess(){
        //Schreiben Sie hier ihren code hin, um den Inhalt des Vertretungsplans in die GUI zu füllen.
        //Benutzen sie vertretungenHeute und vertretungenMorgen um den Vertretungsplan auszulesen.
        //Beispiel: vertretungenHeute.get(index).getKlasse()
        //index ist die Zeile des Vertretungsplans.
        //nachrichtenHeute und nachrichtenMorgen sind ArrayLists, in denen die Nachrichten gespeichert sind.
        //Die erste Zeile der Nachrichten ist das Datum.

        ArrayList<Vertretung> daten = new ArrayList<Vertretung>();

        /*
        daten.add(new Vertretung("Kl.", "Std.", "Fach", "Vertr.", "Für", "Raum", "Hinw.", "Art"));
        daten.add(new Vertretung("8a", "1", "D", "Sta", "Cmp", "003", "Sachen!", "Vertretung"));
        daten.add(new Vertretung("6a", "3", "M", "Bl", " ", "202", "test123", "Raumänderung"));
*/
        SharedPreferences mySPR = this.getContext().getSharedPreferences("MySPFILE", 0);
        String klasse1 = mySPR.getString("myKey2", "8a"); // TODO: Klasse

        if(klasse1.charAt(0) == 'E' || klasse1.charAt(0) == 'Q' || klasse1.charAt(0) == 'e' || klasse1.charAt(0) == 'e'){
            klasse1 = Character.toUpperCase(klasse1.charAt(0)) + klasse1.substring(1);
        }
        else{
            int indexLetter = klasse1.length() - 1;
            klasse1 = klasse1.substring(0, indexLetter) + Character.toLowerCase(klasse1.charAt(indexLetter));
        }

        ArrayList<Vertretung> list = new ArrayList<>();
        for(Vertretung v : vertretungenHeute){
            if(v.getKlasse().contains(klasse1))list.add(v);
        }
        VertretungsplanAdapter vertretungenAdapter = new VertretungsplanAdapter(getContext(), list);
        ListView lstMenu = (ListView) getView().findViewById(R.id.lstMenu);

        lstMenu.setAdapter(vertretungenAdapter);
        vertretungenAdapter.notifyDataSetChanged();

    }

}

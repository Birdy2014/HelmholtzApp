package com.ffm.helmholtzschule.helmholtzapp;


import java.util.ArrayList;
import java.util.HashMap;

public class Vertretung {
    String klasse;
    String stunde;
    String fach;
    String vertretungslehrer;
    String fuerLehrer;
    String raum;
    String hinweis;
    String art;

    public Vertretung(String klasse, int nummer, HashMap<String, ArrayList<Object[]>> map) {
        this.klasse = klasse;
        stunde = (String)map.get(klasse).get(nummer)[0];
        fach = (String)map.get(klasse).get(nummer)[1];
        vertretungslehrer = (String)map.get(klasse).get(nummer)[2];
        fuerLehrer = (String)map.get(klasse).get(nummer)[3];
        raum = (String)map.get(klasse).get(nummer)[4];
        hinweis = (String)map.get(klasse).get(nummer)[5];
        art = (String)map.get(klasse).get(nummer)[6];
    }


    public String getKlasse() {
        return klasse;
    }

    public String getStunde() {
        return stunde;
    }

    public String getFach() {
        return fach;
    }

    public String getVertretungslehrer() {
        return vertretungslehrer;
    }

    public String getFuerLehrer() {
        return fuerLehrer;
    }

    public String getRaum() {
        return raum;
    }

    public String getHinweis() {
        return hinweis;
    }

    public String getArt() {
        return art;
    }
}

/* Vertretung.java -> Vetretung.java

 */
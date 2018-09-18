package de.helmholtzschule_frankfurt.helmholtzapp.util;

import java.util.ArrayList;
import java.util.HashMap;

import de.helmholtzschule_frankfurt.helmholtzapp.item.Vertretung;

public class Vertretungsplan {

    private HashMap<String, ArrayList<Vertretung>> data;
    private HashMap<String, String> meta;

    public Vertretungsplan(HashMap<String, ArrayList<Vertretung>> data, HashMap<String, String> meta) {
        this.data = data;
        this.meta = meta;
    }

    public HashMap<String, ArrayList<Vertretung>> getData() {
        return data;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Benachrichtigungsplan {

    private HashMap<String, ArrayList<String>> data;
    private HashMap<String, String> meta;

    public Benachrichtigungsplan(HashMap<String, String> meta, HashMap<String, ArrayList<String>> data) {
        this.data = data;
        this.meta = meta;
    }

    public HashMap<String, ArrayList<String>> getData() {
        return data;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }
}

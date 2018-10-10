package de.helmholtzschule_frankfurt.helmholtzapp.util;

import java.util.ArrayList;
import java.util.HashMap;

import de.helmholtzschule_frankfurt.helmholtzapp.item.MensaplanItem;

public class Mensaplan {

    private HashMap<String, String> meta;
    private ArrayList<MensaplanItem> data;

    public Mensaplan(HashMap<String, String> meta, ArrayList<MensaplanItem> data) {
        this.meta = meta;
        this.data = data;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }

    public ArrayList<MensaplanItem> getData() {
        return data;
    }
}

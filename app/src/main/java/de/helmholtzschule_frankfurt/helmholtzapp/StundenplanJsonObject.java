package de.helmholtzschule_frankfurt.helmholtzapp;

import java.util.ArrayList;

public class StundenplanJsonObject {

    private ArrayList<StundenplanItem> items = new ArrayList<>();
    private int hours;

    public StundenplanJsonObject(ArrayList<StundenplanItem> items, int hours) {
        this.items = items;
        this.hours = hours;
    }

    public ArrayList<StundenplanItem> getItems() {
        return items;
    }

    public int getHours() {
        return hours;
    }
}

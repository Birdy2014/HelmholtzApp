package de.helmholtzschule_frankfurt.helmholtzapp.util;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.item.StundenplanItem;

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

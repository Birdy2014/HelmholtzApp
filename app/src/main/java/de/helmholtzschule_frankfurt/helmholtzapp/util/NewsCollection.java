package de.helmholtzschule_frankfurt.helmholtzapp.util;

import java.util.ArrayList;
import java.util.HashMap;

import de.helmholtzschule_frankfurt.helmholtzapp.item.NewsItem;

public class NewsCollection {

    private HashMap<String, String> meta;
    private ArrayList<NewsItem> data;

    public NewsCollection(HashMap<String, String> meta, ArrayList<NewsItem> data) {
        this.meta = meta;
        this.data = data;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }

    public ArrayList<NewsItem> getData() {
        return data;
    }
}


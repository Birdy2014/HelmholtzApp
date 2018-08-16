package de.helmholtzschule_frankfurt.helmholtzapp.item;

/**
 * Created by Julian on 28.06.2017.
 */

public class News {
    String titel;
    String url;

    public News(String titel, String url) {
        this.titel = titel;
        this.url = url;
    }

    public String getTitel() {
        return titel;
    }

    public String getUrl() {
        return url;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp.item;


public class NewsItem {
    String titel;
    String url;

    public NewsItem(String titel, String url) {
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

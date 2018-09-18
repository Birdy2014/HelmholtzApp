package de.helmholtzschule_frankfurt.helmholtzapp.item;

public class Vertretung {
    private String klasse;
    private String stunde;
    private String fach;
    private String vertretungsLehrer;
    private String fuer;
    private String raum;
    private String vertretungVon;
    private String hinweis;
    private String art;

    public Vertretung(String klasse, String stunde, String fach, String vertretungsLehrer, String fuer, String raum, String vertretungVon, String hinweis, String art) {
        this.klasse = klasse;
        this.stunde = stunde;
        this.fach = fach;
        this.vertretungsLehrer = vertretungsLehrer;
        this.fuer = fuer;
        this.raum = raum;
        this.vertretungVon = vertretungVon;
        this.hinweis = hinweis;
        this.art = art;
    }

    public String getKlasse() {
        return this.klasse;
    }

    public String getStunde() {
        return this.stunde;
    }

    public String getFach() {
        return this.fach;
    }

    public String getVertretungsLehrer() {
        return this.vertretungsLehrer;
    }

    public String getFuer() {
        return this.fuer;
    }

    public String getRaum() {
        return this.raum;
    }

    public String getVertretungVon() {
        return this.vertretungVon;
    }

    public String getHinweis() {
        return this.hinweis;
    }

    public String getArt() {
        return this.art;
    }
}


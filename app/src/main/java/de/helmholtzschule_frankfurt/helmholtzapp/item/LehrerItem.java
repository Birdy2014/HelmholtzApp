package de.helmholtzschule_frankfurt.helmholtzapp.item;

public class LehrerItem {

    private String text;

    public LehrerItem(String text){
        this.text = text;
    }
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}

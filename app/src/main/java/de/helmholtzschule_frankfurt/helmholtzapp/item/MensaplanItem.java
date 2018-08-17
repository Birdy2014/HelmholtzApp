package de.helmholtzschule_frankfurt.helmholtzapp.item;


public class MensaplanItem {
    String tag;
    String fleisch;
    String veg;
    String dessert;


    public MensaplanItem(String tag, String fleisch, String veg, String dessert) {
        this.tag = tag;
        this.fleisch = fleisch;
        this.veg = veg;
        this.dessert = dessert;
    }

    public String getTag() {
        return tag;
    }

    public String getFleisch() {
        return fleisch;
    }

    public String getVeg() {
        return veg;
    }

    public String getDessert() {
        return dessert;
    }

}

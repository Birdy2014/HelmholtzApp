package de.helmholtzschule_frankfurt.helmholtzapp.item;


public class MensaplanItem {
    private String fleisch;
    private String vegetarisch;
    private String salat;
    private String pasta;
    private String dessert;


    public MensaplanItem(String fleisch, String veg, String salad, String noodles, String dessert) {
        this.fleisch = fleisch;
        this.vegetarisch = veg;
        this.salat = salad;
        this.pasta = noodles;
        this.dessert = dessert;
    }

    public String getMeat() {
        return fleisch;
    }

    public String getVeg() {
        return vegetarisch;
    }

    public String getSalad() {
        return salat;
    }

    public String getNoodles() {
        return pasta;
    }

    public String getDessert() {
        return dessert;
    }

    @Override
    public String toString() {
        return getMeat() + " " + getVeg() + " " + getSalad() + " " + getNoodles() + " " + getDessert();
    }
}


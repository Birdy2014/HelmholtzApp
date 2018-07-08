package de.helmholtzschule_frankfurt.helmholtzapp;

import javax.sql.StatementEvent;

public class StundenplanItem extends StundenplanCell{

    private String lehrer;
    private String raum;
    private int color;
    private int textColor;

    public StundenplanItem(String name, String lehrer, String raum, StundenplanColor color) {
        super(name);
        setLehrer(lehrer);
        setRaum(raum);
        setColor(color);
        setTextColor(color.getTextColor());
    }

    public String getLehrer() {
        return lehrer;
    }

    public String getRaum() {
        return raum;
    }

    public int getColor() {
        return color;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer == null ? "" : lehrer;
    }

    public void setRaum(String raum) {
        this.raum = raum == null ? "" : raum;
    }

    public void setColor(StundenplanColor color) {
        this.color = color.getCode();
        setTextColor(color.getTextColor());
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

}

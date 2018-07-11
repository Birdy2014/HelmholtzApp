package de.helmholtzschule_frankfurt.helmholtzapp;

import static de.helmholtzschule_frankfurt.helmholtzapp.StundenplanColor.*;

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

    public void setColor(int color){
        this.color = color;
        String cleanHex = Integer.toHexString(color).substring(2);
        double average = 0;
        for(int i = 0; i < cleanHex.length(); i += 2){
            average += Integer.parseInt(cleanHex.substring(i, i + 2), 16);
        }
        average /= 3;
        setTextColor(Math.abs(255 - average) > 127.5 ? WHITE.getCode() : BLACK.getCode());
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void overwrite(StundenplanItem item){
        setName(item.getName());
        setLehrer(item.getLehrer());
        setRaum(item.getRaum());
        setColor(item.getColor());;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp.enums;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;

public enum StundenplanColor {

    ADD(0xFFFFFFFF),
    BLACK(0xFF000000),
    GREY(0xFF7A7A7A),
    BLUEGREY(0xFF35546B),
    RED(0xFFFF1E1E),
    ORANGE(0xFFFF7C26),
    YELLOW(0xFFFFD800),
    LIGHTGREEN(0xFF00AF4C),
    GREEN(0xFF007A00),
    LIGHTBLUE(0xFF0094FF),
    BLUE(0xFF0019A8),
    VIOLET(0xFF61008E),
    PINK(0xFFFF6DB0),
    WHITE(0xFFFFFFFF),
    BROWN(0xFF632700);

    private int code;

    StundenplanColor(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public int getTextColor(){
        String cleanHex = Integer.toHexString(this.getCode()).substring(2);
        double average = 0;
        for(int i = 0; i < cleanHex.length(); i += 2){
            average += Integer.parseInt(cleanHex.substring(i, i + 2), 16);
        }
        average /= 3;
        return Math.abs(255 - average) > DataStorage.getInstance().CONTRASTVAR ? WHITE.getCode() : BLACK.getCode();
    }
}

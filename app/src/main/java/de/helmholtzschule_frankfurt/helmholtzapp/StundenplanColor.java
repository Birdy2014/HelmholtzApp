package de.helmholtzschule_frankfurt.helmholtzapp;

import java.util.Arrays;

public enum StundenplanColor {

    BLACK(0xFF000000),
    GREY(0xFF404040),
    RED(0xFFFF0000),
    ORANGE(0xFFFF6A00),
    YELLOW(0xFFFFD800),
    LIGHTGREEN(0xFF4CFF00),
    GREEN(0xFF00AA00),
    LIGHTBLUE(0xFF0094FF),
    BLUE(0xFF0026FF),
    VIOLET(0xFFB200FF),
    PINK(0xFFFF006E),
    WHITE(0xFFFFFFFF);

    private int code;

    StundenplanColor(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public int getTextColor(){
        StundenplanColor[] blackBg = {YELLOW, WHITE};
        return Arrays.asList(blackBg).contains(this) ? 0xFF000000 : 0xFFFFFFFF;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp.util;

public class StundenplanCellTime extends StundenplanCell {

    public StundenplanCellTime(String startTime, String endTime) {
        super(startTime.equals("") ? endTime : (endTime.equals("") ? startTime : startTime + "\n" + endTime));
    }
}

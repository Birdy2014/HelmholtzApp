package de.helmholtzschule_frankfurt.helmholtzapp.util;

import de.helmholtzschule_frankfurt.helmholtzapp.item.StundenplanCell;

public class StundenplanCellTime extends StundenplanCell {

    public StundenplanCellTime(String startTime, String endTime) {
        super(startTime.equals("") ? endTime : (endTime.equals("") ? startTime : startTime + "\n" + endTime));
    }
}

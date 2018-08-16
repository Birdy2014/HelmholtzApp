package de.helmholtzschule_frankfurt.helmholtzapp.enums;

public enum ActionType {

    //Those types depend on the selected month in Calendar

    INTERNAL(),
    EXTERNAL_IN(),
    EXTERNAL_OUT(),
    EXTERNAL(),
    INGOING(),
    OUTGOING_I(), //goes into next Month
    OUTGOING_II(), //goes further
    SPANNED();

    ActionType(){

    }
}

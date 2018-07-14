package de.helmholtzschule_frankfurt.helmholtzapp;

public enum ActionType {

    //Those types depend on the selected month in Calendar

    INTERNAL(),
    EXTERNAL_IN(),
    EXTERNAL_OUT(),
    EXTERNAL(),
    INGOING(),
    OUTGOING(),
    SPANNED();

    ActionType(){

    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

public enum ActionType {

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

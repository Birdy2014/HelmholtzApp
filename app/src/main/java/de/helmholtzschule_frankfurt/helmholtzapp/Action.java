package de.helmholtzschule_frankfurt.helmholtzapp;

public class Action {

    private String name;
    private ActionContainer origin;

    public Action(String name, ActionContainer container) {
        this.name = name;
        origin = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

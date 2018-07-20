package de.helmholtzschule_frankfurt.helmholtzapp;

public class Action {

    private String name;
    private ActionContainer parent;

    public Action(String name, ActionContainer container) {
        this.name = name;
        parent = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionContainer getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return name;
    }
}

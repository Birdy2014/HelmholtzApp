package de.helmholtzschule_frankfurt.helmholtzapp.util;

public class StundenplanCell {

    private String name;

    public StundenplanCell(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import java.util.ArrayList;

public class CalendarItem {

    private int day;
    private ArrayList<Action> actions;
    private boolean isActualMonth;

    public CalendarItem(int day, boolean isActualMonth) {
        this.day = day;
        actions = new ArrayList<>();
        this.isActualMonth = isActualMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
    public void addAction(Action action){
        if(actions.size() < 4){
            actions.add(action);
        }
        else {
            try {
                throw new TODO("add Dialog or such stuff");
            } catch (TODO todo) {
                todo.printStackTrace();
            }
        }
    }

    public boolean isActualMonth() {
        return isActualMonth;
    }

    public void setActualMonth(boolean actualMonth) {
        isActualMonth = actualMonth;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import java.util.ArrayList;

public class CalendarItem {

    private int day;
    private ArrayList<Action> actions; // 5 items
    private boolean isActualMonth;
    private boolean isToday;

    public CalendarItem(int day, boolean isActualMonth, boolean isToday, int isFirstAt) {
        this.day = day;
        actions = new ArrayList<>();
        this.isActualMonth = isActualMonth;
        this.isToday = isToday;
        for(int i = 0; i < 5; i++){
            actions.add(null);
        }

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

    public void addAction(Action action, int index){
        if(index < actions.size() && actions.get(index) == null){
            actions.set(index, action);
        }
        else {
            try {
                throw new TODO("Add Dialog or such stuff");
            } catch (TODO todo) {
                todo.printStackTrace();
            }
        }
    }
    public int getFreeActionIndex(){
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i) == null)return i;
        }
        return -1;
    }
    public boolean hasActions(){
        for(Action a : actions)if(a != null)return true;
        return false;
    }

    public boolean isActualMonth() {
        return isActualMonth;
    }

    public void setActualMonth(boolean actualMonth) {
        isActualMonth = actualMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}

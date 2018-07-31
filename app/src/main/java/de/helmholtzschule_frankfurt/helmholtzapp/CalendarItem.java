package de.helmholtzschule_frankfurt.helmholtzapp;

import java.util.ArrayList;
import java.util.HashSet;

public class CalendarItem {

    private int day;
    private ArrayList<Action> actions; // 3 items
    private boolean isActualMonth;
    private boolean isToday;
    private int month;
    private HashSet<Integer> firstAt = new HashSet<>();

    public CalendarItem(int day, boolean isActualMonth, boolean isToday, int month) {
        this.day = day;
        actions = new ArrayList<>();
        this.isActualMonth = isActualMonth;
        this.isToday = isToday;
        for(int i = 0; i < 5; i++){
            actions.add(null);
        }
        setMonth(month);
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isFirstAt(int index) {
        return firstAt.contains(index);
    }

    public void setFirstAt(int index) {
        firstAt.add(index);
    }
}

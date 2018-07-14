package de.helmholtzschule_frankfurt.helmholtzapp;

public class ActionContainer {

    private Action action;
    private ActionDate startDate;
    private ActionDate endDate;

    public ActionContainer(String actionName, ActionDate startDate, ActionDate endDate) {
        this.action = new Action(actionName, this);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Action getAction() {
        return action;
    }

    public int getStartDay() {
        return startDate.getDay();
    }

    public int getStartMonth(){return startDate.getMonth();}

    public int getStartYear(){return startDate.getYear();}

    public int getEndDay() {
        return endDate.getDay();
    }

    public int getEndMonth(){return endDate.getMonth();}

    public int getEndYear(){return endDate.getYear();}
}

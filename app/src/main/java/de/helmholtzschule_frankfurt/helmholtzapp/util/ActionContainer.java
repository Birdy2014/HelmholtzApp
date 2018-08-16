package de.helmholtzschule_frankfurt.helmholtzapp.util;

import de.helmholtzschule_frankfurt.helmholtzapp.item.Action;

public class ActionContainer {

    private Action action;
    private ActionDate startDate;
    private ActionDate endDate;

    public ActionContainer(String actionName, ActionDate startDate, ActionDate endDate) {
        this.action = new Action(actionName, this);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public ActionContainer(String code){
        String actionName = code.substring(0, code.indexOf("|!|"));
        String startDateString = code.substring(code.indexOf("|!|") + 3, code.lastIndexOf("|!|"));
        String endDateString = code.substring(code.lastIndexOf("|!|") + 3);
        String[] startDateArr = startDateString.split(",");
        String[] endDateArr = endDateString.split(",");

        startDate = new ActionDate(Integer.parseInt(startDateArr[0]), Integer.parseInt(startDateArr[1]), Integer.parseInt(startDateArr[2]), Integer.parseInt(startDateArr[3]), Integer.parseInt(startDateArr[4]));
        endDate = new ActionDate(Integer.parseInt(endDateArr[0]), Integer.parseInt(endDateArr[1]), Integer.parseInt(endDateArr[2]), Integer.parseInt(endDateArr[3]), Integer.parseInt(endDateArr[4]));
        action = new Action(actionName, this);
    }

    public Action getAction() {
        return action;
    }

    public int getStartMinute(){return startDate.getMinutes();}

    public int getStartHour(){return startDate.getHours();}

    public int getStartDay() {
        return startDate.getDay();
    }

    public int getStartMonth(){return startDate.getMonth();}

    public int getStartYear(){return startDate.getYear();}

    public int getEndMinute(){return endDate.getMinutes();}

    public int getEndHour(){return endDate.getHours();}

    public int getEndDay() {
        return endDate.getDay();
    }

    public int getEndMonth(){return endDate.getMonth();}

    public int getEndYear(){return endDate.getYear();}

    @Override
    public String toString() {
        return action.toString() + "|!|" + startDate.toString() + "|!|" + endDate.toString();
    }
}

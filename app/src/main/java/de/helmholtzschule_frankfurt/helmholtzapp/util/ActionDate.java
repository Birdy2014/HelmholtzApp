package de.helmholtzschule_frankfurt.helmholtzapp.util;

public class ActionDate {

    private int day;
    private int month;
    private int year;
    private int hours;
    private int minutes;

    public ActionDate(int minutes, int hours, int day, int month, int year) {
        this.minutes = minutes;
        this.hours = hours;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return getMinutes() + "," + getHours() + "," + getDay() + "," + getMonth() + "," + getYear();
    }
}

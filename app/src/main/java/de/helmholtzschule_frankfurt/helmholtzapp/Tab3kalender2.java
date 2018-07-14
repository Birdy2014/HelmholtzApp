package de.helmholtzschule_frankfurt.helmholtzapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.EXTERNAL;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.EXTERNAL_IN;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.EXTERNAL_OUT;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.INGOING;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.INTERNAL;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.OUTGOING;
import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.SPANNED;

public class Tab3kalender2 extends Fragment{

    DataStorage storage = DataStorage.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_kalender2, container, false);
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ArrayList<CalendarItem> days = storage.getCalendarList(-1, -1);
        ArrayList<CalendarItem> placeholder = new ArrayList<>(days);
        days.clear();
        days.addAll(addActions(placeholder, storage.getMonthYear()[0], storage.getMonthYear()[1]));
        GridView grid = getActivity().findViewById(R.id.calendarGrid);
        CalendarAdapter adapter = new CalendarAdapter(getContext(), days);
        grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        TextView date = getActivity().findViewById(R.id.calendarHeader);
        date.setText(new DateFormatSymbols().getMonths()[storage.getMonthYear()[0]] + " " + storage.getMonthYear()[1]);

        ImageButton left = getActivity().findViewById(R.id.calendarButtonLeft);
        ImageButton right = getActivity().findViewById(R.id.calendarButtonRight);

        left.setOnClickListener(click -> {
            int month = storage.getMonthYear()[0];
            int year = storage.getMonthYear()[1];

            if(month == 0){
                month = 11;
                year--;
            }
            else --month;
            days.clear();
            days.addAll(storage.getCalendarList(month, year));
            ArrayList<CalendarItem> placeholder2 = new ArrayList<>(days);
            days.clear();
            days.addAll(addActions(placeholder2, month, year));
            storage.setMonthYear(new int[]{month, year});
            date.setText(new DateFormatSymbols().getMonths()[month] + " " + year);
            adapter.notifyDataSetChanged();
        });
        right.setOnClickListener(click -> {
            int month = storage.getMonthYear()[0];
            int year = storage.getMonthYear()[1];
            if(month == 11){
                month = 0;
                year++;
            }
            else month++;
            days.clear();
            days.addAll(storage.getCalendarList(month, year));
            ArrayList<CalendarItem> placeholder2 = new ArrayList<>(days);
            days.clear();
            days.addAll(addActions(placeholder2, month, year));
            storage.setMonthYear(new int[]{month, year});
            date.setText(new DateFormatSymbols().getMonths()[month] + " " + year);
            adapter.notifyDataSetChanged();
        });

        super.onViewCreated(view, savedInstanceState);
    }
    private ArrayList<CalendarItem> addActions(ArrayList<CalendarItem> list, int month, int year){

        for(ActionContainer container : storage.getContainers()){
            ActionType actionType = EXTERNAL;
            if(container.getStartMonth() == month && container.getStartYear() == year){
                if(container.getStartYear() < container.getEndYear())actionType = OUTGOING;
                else if(container.getStartYear() > container.getEndYear())actionType = INGOING;
                else {
                    if(container.getStartMonth() < container.getEndMonth())actionType = OUTGOING;
                    else if(container.getStartMonth() > container.getEndMonth())actionType = INGOING;
                    else actionType = INTERNAL;
                }
            }
            else if(container.getEndMonth() == month && container.getEndYear() == year){
                if(container.getStartYear() < container.getEndYear())actionType = INGOING;
                else if(container.getStartYear() > container.getEndYear())actionType = OUTGOING;
                else {
                    if(container.getStartMonth() < container.getEndMonth())actionType = INGOING;
                    else if(container.getStartMonth() > container.getEndMonth())actionType = OUTGOING;
                    else actionType = INTERNAL;
                }
            }
            else {
                if(container.getStartMonth() == container.getEndMonth()){
                    int monthBefore = month == 0 ? 11 : month - 1;
                    int monthAfter = month == 11 ? 0 : month + 1;
                    if(container.getStartMonth() == monthBefore)actionType = EXTERNAL_IN;
                    else if(container.getStartMonth() == monthAfter)actionType = EXTERNAL_OUT;
                }
                else {
                    if(container.getStartYear() == container.getEndYear()){
                        if(container.getStartMonth() < month && container.getEndMonth() > month && container.getStartYear() == year)actionType = SPANNED;
                    }
                    else {
                        if(container.getStartYear() <= year && container.getEndYear() > year){
                            if(container.getStartMonth() < month)actionType = SPANNED;
                        }
                        if(container.getStartYear() <= year){
                            if(container.getEndYear() == year){
                                if(container.getEndMonth() >= month) {
                                    actionType = SPANNED;
                                }
                            }
                        }
                    }
                }
            }

            ArrayList<CalendarItem> placeholder = new ArrayList<>(list);
            list.clear();
            System.out.println(actionType.toString());
            list.addAll(storage.addActionToList(placeholder, container.getStartDay(), container.getEndDay(), container.getAction(), actionType));

        }
        return list;
    }

}

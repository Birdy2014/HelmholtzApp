package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import static de.helmholtzschule_frankfurt.helmholtzapp.ActionType.*;

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

        ImageButton add = getActivity().findViewById(R.id.calendarButtonAdd);
        add.setOnClickListener(click -> {
            Dialog dialog = new Dialog(getActivity());
            View dialogView = View.inflate(getActivity(), R.layout.calendar_popup, null);
            dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Spinner startDay = dialogView.findViewById(R.id.calendar_popup_edit_start_day);
            Spinner startMonth = dialogView.findViewById(R.id.calendar_popup_edit_start_month);
            Spinner startYear = dialogView.findViewById(R.id.calendar_popup_edit_start__year);


            addActualDataToSpinner(startYear, Calendar.getInstance().get(Calendar.YEAR), "year");
            addActualDataToSpinner(startMonth, Calendar.getInstance().get(Calendar.MONTH), "month");
            addActualDataToSpinner(startDay, Calendar.getInstance().get(Calendar.DAY_OF_MONTH), "day");

            startMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ArrayList<Integer> days = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.MONTH, i);
                    for(int j = 1; j <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
                        days.add(i);
                    }
                    startMonth.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, days));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            dialog.show();
            /*storage.getContainers().add(new ActionContainer("Test", new ActionDate(2, Calendar.JUNE, 2018), new ActionDate(9, Calendar.JULY, 2018)));
            ArrayList<CalendarItem> placeholder2 = new ArrayList<>(days);
            days.clear();
            days.addAll(addActions(placeholder2, storage.getMonthYear()[0], storage.getMonthYear()[1]));
            adapter.notifyDataSetChanged();*/
        });

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
                if(container.getStartYear() < container.getEndYear()){
                    if(container.getEndMonth() == 0){
                        if(container.getStartMonth() == 11)actionType = OUTGOING_I;
                        else actionType = OUTGOING_II;
                    }
                    else{
                        if(container.getStartMonth() == container.getEndMonth() - 1)actionType = OUTGOING_I;
                        else actionType = OUTGOING_II;
                    }
                }
                else if(container.getStartYear() > container.getEndYear()){
                    System.out.println("start > endyear?");
                    actionType = INGOING;
                }
                else {
                    if(container.getStartMonth() < container.getEndMonth()){
                        if(container.getEndMonth() == 0){
                            if(container.getStartMonth() == 11)actionType = OUTGOING_I;
                            else actionType = OUTGOING_II;
                        }
                        else{
                            if(container.getStartMonth() == container.getEndMonth() - 1)actionType = OUTGOING_I;
                            else actionType = OUTGOING_II;
                        }
                    }
                    else if(container.getStartMonth() > container.getEndMonth())actionType = INGOING;
                    else actionType = INTERNAL;
                }
            }
            else if(container.getEndMonth() == month && container.getEndYear() == year){
                if(container.getStartYear() < container.getEndYear())actionType = INGOING;
                //else if(container.getStartYear() > container.getEndYear())actionType = OUTGOING;
                else {
                    if(container.getStartMonth() < container.getEndMonth())actionType = INGOING;
                    else if(container.getStartMonth() > container.getEndMonth())actionType = OUTGOING_II;
                    else actionType = INTERNAL;
                }
            }
            else {
                if(container.getStartMonth() == container.getEndMonth()){
                    int monthBefore = month == 0 ? 11 : month - 1;
                    int monthAfter = month == 11 ? 0 : month + 1;
                    if(container.getStartMonth() == monthBefore){
                        if(list.get(0).getDay() <= container.getStartDay())actionType = EXTERNAL_IN;
                    }
                    else if(container.getStartMonth() == monthAfter){
                        System.out.println(DateFormatSymbols.getInstance().getMonths()[month]);
                        int i = 0;
                        int x = 0;
                        for(; i < list.size(); i++){
                            if(list.get(i).getDay() == 1){
                                System.out.println("i: " + i);
                                if(++x == 2)break;
                            }
                        }
                        System.out.println("Index of 2nd 1: " + i);
                        for(; i < list.size(); i++){
                            if(list.get(i).getDay() == container.getStartDay()){
                                actionType = EXTERNAL_OUT;
                                break;
                            }
                        }
                    }
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
            if(actionType != EXTERNAL) {
                ArrayList<CalendarItem> placeholder = new ArrayList<>(list);
                list.clear();
                System.out.println(actionType.toString());
                list.addAll(storage.addActionToList(placeholder, container.getStartDay(), container.getEndDay(), container.getAction(), actionType));
            }

        }
        return list;
    }

    private void addActualDataToSpinner(Spinner spinner, int x, String key){
        switch (key){
            case "year": {
                ArrayList<Integer> list = new ArrayList<>();
                for(int i = x - 100; i <= x + 100; i++)list.add(i);
                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
                spinner.setAdapter(adapter);
                spinner.setSelection(101);
                break;
            }
            case "month":{
                ArrayList<String> list = new ArrayList<>();
                for(int i = 0; i < 12; i++){
                    list.add(DateFormatSymbols.getInstance().getMonths()[i]);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
                spinner.setAdapter(adapter);
                spinner.setSelection(x);
                break;
            }
            case "day": {
                ArrayList<Integer> list = new ArrayList<>();
                for(int i = 1; i < 32; i++)list.add(i);
                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
                spinner.setSelection(x - 1);
            }
        }
    }

}

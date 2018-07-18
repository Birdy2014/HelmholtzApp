package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
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



        //fix that
        ImageButton add = getActivity().findViewById(R.id.calendarButtonAdd);
        add.setOnClickListener(click -> {

            Dialog dialog = new Dialog(getActivity());
            View dialogView = View.inflate(getActivity(), R.layout.calendar_popup, null);
            dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView start = dialogView.findViewById(R.id.popup_start_view);
            TextView end = dialogView.findViewById(R.id.popup_end_view);

            start.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ". " + DateFormatSymbols.getInstance().getMonths()[storage.getMonthYear()[0]] + " " + storage.getMonthYear()[1]);
            start.setOnClickListener(click1 -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        System.out.println(i + " " + i1 + " " + i2);

                        start.setText(i2 + ". " + DateFormatSymbols.getInstance().getMonths()[i1] + " " + i);
                    }
                });

                datePickerDialog.show();
            });
            end.setText(start.getText());
            end.setOnClickListener(click1 -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        System.out.println(i + " " + i1 + " " + i2);

                        end.setText(i2 + ". " + DateFormatSymbols.getInstance().getMonths()[i1] + " " + i);

                    }
                });

                datePickerDialog.show();
            });

            Button apply = dialogView.findViewById(R.id.calendar_popup_apply);
            apply.setOnClickListener(click1 -> {

                storage.getContainers().add(new ActionContainer("Test", generateActionDateFromDate(start.getText().toString()), generateActionDateFromDate(end.getText().toString())));
                days.clear();
                days.addAll(addActions(storage.getCalendarList(storage.getMonthYear()[0], storage.getMonthYear()[1]), storage.getMonthYear()[0], storage.getMonthYear()[1]));
                adapter.notifyDataSetChanged();
            });

            TextView startTime = dialogView.findViewById(R.id.calendar_popup_start_time);
            TextView endTime = dialogView.findViewById(R.id.calendar_popup_end_time);

            startTime.setOnClickListener(click1 -> {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.primaryDarkForeground, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String hours = String.valueOf(i);
                        if(hours.length() == 1)hours = "0" + hours;
                        String minutes = String.valueOf(i1);
                        if(minutes.length() == 1)minutes = "0" + minutes;
                        startTime.setText(hours + ":" + minutes);
                    }
                }, 8, 0, true);

                timePickerDialog.show();

            });

            endTime.setOnClickListener(click1 -> {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.primaryDarkForeground, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String hours = String.valueOf(i);
                        if(hours.length() == 1)hours = "0" + hours;
                        String minutes = String.valueOf(i1);
                        if(minutes.length() == 1)minutes = "0" + minutes;
                        endTime.setText(hours + ":" + minutes);
                    }
                },8, 0 , true);

                timePickerDialog.show();

            });


            dialog.show();
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

    private ActionDate generateActionDateFromDate(String s){
        int day = Integer.parseInt(s.substring(0, s.indexOf(".")));
        String monthString = s.substring(s.indexOf(".") + 2, s.lastIndexOf(" "));
        System.out.println(monthString + "END");
        int month = Arrays.asList(DateFormatSymbols.getInstance().getMonths()).indexOf(monthString);
        int year = Integer.parseInt(s.substring(s.lastIndexOf(" ") + 1));
        System.out.println(day + " " + month + " " + year);
        return new ActionDate(day, month, year);
    }
}

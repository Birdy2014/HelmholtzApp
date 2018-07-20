package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static de.helmholtzschule_frankfurt.helmholtzapp.StundenplanColor.*;

public class CalendarAdapter extends ArrayAdapter<CalendarItem>{

    public CalendarAdapter(Context context, ArrayList<CalendarItem> list) {
        super(context, R.layout.calendar_cell, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.calendar_cell, parent, false);


        String day = String.valueOf(getItem(position).getDay());
        TextView dayView = customView.findViewById(R.id.calendarDateView);
        View view = customView.findViewById(R.id.calendarCellBackground);

        if(!getItem(position).isActualMonth()){
            dayView.setTextColor(0xFFADADAD);
        }
        if(getItem(position).isToday()){
            view.setBackgroundColor(0x555A9016);//006EFF -> light blue
        }
        for(int i = 0; i < getItem(position).getActions().size(); i++){
            if(getItem(position).getActions().get(i) == null)continue;
            customView.findViewById(getDateSpotIndexByActionIndex(i)).setBackgroundColor(getBackgroundByActionIndex(i));
        }
        dayView.setText(day);

        customView.setOnClickListener(click -> {
            showActions(getItem(position));
        });

        return customView;
    }

    private void showActions(CalendarItem item){
        System.out.println("Actions of: " + item.getDay() + ". " + item.getMonth() + ".");
        for(int i = 0; i < item.getActions().size(); i++){
            Action action = item.getActions().get(i);
            System.out.println("Slot " + i + ":");
            if(action == null)System.out.println("Empty");
            else {
                System.out.println(action.getName());
                System.out.println("Start: " + action.getParent().getStartDay() + ". " + action.getParent().getStartMonth() + ". " + action.getParent().getStartYear() + " " + action.getParent().getStartHour() + ":" + action.getParent().getStartMinute());
                System.out.println("End: " + action.getParent().getEndDay() + ". " + action.getParent().getEndMonth() + ". " + action.getParent().getEndYear() + " " + action.getParent().getEndHour() + ":" + action.getParent().getEndMinute());
            }
        }
    }

    private int getDateSpotIndexByActionIndex(int index){
        switch (index){
            case 0:
                return R.id.dateSpot0;
            case 1:
                return R.id.dateSpot1;
            case 2:
                return R.id.dateSpot2;
            case 3:
                return R.id.dateSpot3;
            case 4:
                return R.id.dateSpot4;
        }
        return 0;
    }
    private int getBackgroundByActionIndex(int index){
        switch (index){
            case 0: return GREEN.getCode();
            case 1: return BLUE.getCode();
            case 2: return ORANGE.getCode();
            case 3: return LIGHTGREEN.getCode();
            case 4: return LIGHTBLUE.getCode();
        }
        return BLACK.getCode();
    }
}

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

        //TODO implement user stuff
        customView.setOnClickListener(click -> {
            if(getItem(position).isActualMonth()) {
                System.out.println("Clicked " + getItem(position).getDay() + " " + DateFormatSymbols.getInstance().getMonths()[getItem(position).getMonth()]);
            }
            else {
                System.out.println("Clicked " + getItem(position).getDay() + " " + DateFormatSymbols.getInstance().getMonths()[getItem(position).getMonth()]);
            }
        });

        return customView;
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

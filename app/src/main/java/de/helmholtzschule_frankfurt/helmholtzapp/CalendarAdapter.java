package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        if(!getItem(position).isActualMonth()){
            dayView.setTextColor(0xFFADADAD);
        }
        dayView.setText(day);

        return customView;
    }
}

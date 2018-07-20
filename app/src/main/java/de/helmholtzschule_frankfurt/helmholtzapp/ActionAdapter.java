package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ActionAdapter extends ArrayAdapter<ActionContainer> {

    ActionAdapter(Context context, ArrayList<ActionContainer> list) {
        super(context, R.layout.action_view_cell, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.action_view_cell, parent, false);

        TextView startView = customView.findViewById(R.id.action_view_cell_start_view);
        TextView endView = customView.findViewById(R.id.action_view_cell_end_view);

        ActionContainer container = getItem(position);
        String startMinute = container.getStartMinute() < 10 ? "0" + String.valueOf(container.getStartMinute()) : String.valueOf(container.getStartMinute());
        String endMinute = container.getEndMinute() < 10 ? "0" + String.valueOf(container.getEndMinute()) : String.valueOf(container.getEndMinute());
        String startHour = container.getStartHour() < 10 ? "0" + String.valueOf(container.getStartHour()) : String.valueOf(container.getStartHour());
        String endHour = container.getEndHour() < 10 ? "0" + String.valueOf(container.getEndHour()) : String.valueOf(container.getEndHour());
        String startDay = container.getStartDay() < 10 ? "0" + String.valueOf(container.getStartDay()) : String.valueOf(container.getStartDay());
        String endDay = container.getEndDay() < 10 ? "0" + String.valueOf(container.getEndDay()) : String.valueOf(container.getEndDay());
        String startMonth = container.getStartMonth() < 10 ? "0" + String.valueOf(container.getStartMonth()) : String.valueOf(container.getStartMonth());
        String endMonth = container.getEndMonth() < 10 ? "0" + String.valueOf(container.getEndMonth()) : String.valueOf(container.getEndMonth());
        String startYear = String.valueOf(container.getStartYear());
        String endYear = String.valueOf(container.getEndYear());

        startView.setText(startDay + ". " + startMonth + ". " + startYear + ", " + startHour + ":" + startMinute + " Uhr");
        endView.setText(endDay + ". " + endMonth + ". " + endYear + ", " + endHour + ":" + endMinute + " Uhr");

        ((TextView)customView.findViewById(R.id.action_view_cell_name)).setText(getItem(position).getAction().getName());


        return customView;
    }
}

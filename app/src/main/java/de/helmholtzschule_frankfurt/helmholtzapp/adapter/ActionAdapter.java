package de.helmholtzschule_frankfurt.helmholtzapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.util.ActionContainer;

public class ActionAdapter extends ArrayAdapter<ActionContainer> {

    private CalendarAdapter calendarAdapter;
    private DataStorage storage = DataStorage.getInstance();

    public ActionAdapter(Context context, ArrayList<ActionContainer> list, CalendarAdapter calendarAdapter) {
        super(context, R.layout.action_view_cell, list);
        this.calendarAdapter = calendarAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.action_view_cell, parent, false);

        TextView startView = customView.findViewById(R.id.action_view_cell_start_view);
        TextView endView = customView.findViewById(R.id.action_view_cell_end_view);
        View background = customView.findViewById(R.id.action_view_cell_colored);
        background.setBackgroundColor(calendarAdapter.getBackgroundByActionIndex(position).getCode());

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

        customView.setOnLongClickListener(view -> {
            View popupView = inflater.inflate(R.layout.calendar_action_box, null);
            Dialog actionDialog = new Dialog(getContext());
            actionDialog.setContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            Button delete = popupView.findViewById(R.id.deleteButton);
            delete.setOnClickListener(click -> {
                storage.removeContainer(getItem(position));
                calendarAdapter.kalender2.days.clear();
                calendarAdapter.kalender2.days.addAll(calendarAdapter.kalender2.addActions(storage.getCalendarList(storage.getMonthYear()[0], storage.getMonthYear()[1]), storage.getMonthYear()[0], storage.getMonthYear()[1]));
                calendarAdapter.kalender2.adapter.notifyDataSetChanged();
                storage.saveCalendar();
                remove(getItem(position));
                actionDialog.cancel();
            });
            actionDialog.show();
            return true;
        });

        return customView;
    }
}

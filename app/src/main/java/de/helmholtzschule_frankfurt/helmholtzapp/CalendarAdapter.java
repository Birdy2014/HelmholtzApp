package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static de.helmholtzschule_frankfurt.helmholtzapp.StundenplanColor.*;

public class CalendarAdapter extends ArrayAdapter<CalendarItem>{

    public Tab3kalender2 kalender2;

    public CalendarAdapter(Context context, ArrayList<CalendarItem> list, Tab3kalender2 kalender2) {
        super(context, R.layout.calendar_cell, list);
        this.kalender2 = kalender2;
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
            customView.findViewById(getDateSpotIndexByActionIndex(i)).setBackgroundColor(getBackgroundByActionIndex(i).getCode());
            if(getItem(position).isFirstAt(i)){
                TextView textView = customView.findViewById(getDateSpotIndexByActionIndex(i));
                textView.setText(getItem(position).getActions().get(i).getName());
                textView.setTextColor(getBackgroundByActionIndex(i).getTextColor());
            }

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
        boolean actionsPresent = false;
        for(Action a : item.getActions()){
            if(a != null){
                actionsPresent = true;
                break;
            }
        }
        Dialog dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(actionsPresent ? R.layout.action_view : R.layout.action_view_empty, null);
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if(actionsPresent) {
            ListView actionView = dialogView.findViewById(R.id.action_list_view);
            ArrayList<ActionContainer> list = new ArrayList<>();
            for (Action a : item.getActions()) if (a != null) list.add(a.getParent());
            actionView.setAdapter(new ActionAdapter(getContext(), list, this));
        }

        dialog.show();
    }

    private int getDateSpotIndexByActionIndex(int index){
        switch (index){
            case 0:
                return R.id.dateSpot0;
            case 1:
                return R.id.dateSpot1;
            case 2:
                return R.id.dateSpot2;
        }
        return 0;
    }

    public ActionColors getBackgroundByActionIndex(int index){
        return ActionColors.values()[index];
    }

    enum ActionColors{

        GREEN(0x55007A00),
        BLUE(0x550019A8),
        ORANGE(0x55FF7C26);

        private int code;

        ActionColors(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public int getTextColor(){
            String cleanHex = Integer.toHexString(this.getCode()).substring(2);
            double average = 0;
            for(int i = 0; i < cleanHex.length(); i += 2){
                average += Integer.parseInt(cleanHex.substring(i, i + 2), 16);
            }
            average /= 3;
            return Math.abs(255 - average) > DataStorage.getInstance().CONTRASTVAR ? WHITE.getCode() : BLACK.getCode();
        }
    }
}

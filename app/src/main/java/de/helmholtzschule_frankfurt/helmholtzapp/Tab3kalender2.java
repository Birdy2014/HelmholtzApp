package de.helmholtzschule_frankfurt.helmholtzapp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

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
        ArrayList<CalendarItem> days = storage.getCalendarList(Calendar.APRIL);
        GridView grid = getActivity().findViewById(R.id.calendarGrid);
        CalendarAdapter adapter = new CalendarAdapter(getContext(), days);
        grid.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        super.onViewCreated(view, savedInstanceState);
    }


}

package de.helmholtzschule_frankfurt.helmholtzapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.item.Benachrichtigung;
import de.helmholtzschule_frankfurt.helmholtzapp.R;

public class BenachrichtigungAdapter extends ArrayAdapter{

    public BenachrichtigungAdapter(Context context, ArrayList<Benachrichtigung> list) {
        super(context, R.layout.benachrichtigung_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.benachrichtigung_row, parent, false);

        String text = ((Benachrichtigung)getItem(position)).getText();


        TextView benachrichtigungTextItem = (TextView) customView.findViewById(R.id.benachrichtigung_item);


        benachrichtigungTextItem.setText(text);

        return customView;
    }
}

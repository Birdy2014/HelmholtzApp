package de.helmholtzschule_frankfurt.helmholtzapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.item.LehrerItem;

public class LehrerAdapter extends ArrayAdapter{

    public LehrerAdapter(Context context, ArrayList<LehrerItem> list) {
        super(context, R.layout.lehrer_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.lehrer_row, parent, false);

        String text = ((LehrerItem)getItem(position)).getText();


        TextView lehrerTextItem = (TextView) customView.findViewById(R.id.lehrer_item);


        lehrerTextItem.setText(text);

        return customView;
    }
}

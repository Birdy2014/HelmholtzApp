package de.helmholtzschule_frankfurt.helmholtzapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MensaAdapter extends ArrayAdapter<Mensaplan> {

    public MensaAdapter(Context context, ArrayList<Mensaplan> list) {
        super(context, R.layout.mensa_row, list);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.mensa_row, parent, false);

        String tag = getItem(position).getTag();
        String fleisch = getItem(position).getFleisch();
        String veg = getItem(position).getVeg();
        String dessert = getItem(position).getDessert();

        TextView txtTag = (TextView) customView.findViewById(R.id.moTxtTag);
        TextView txtFleisch = (TextView) customView.findViewById(R.id.moTxtFleisch);
        TextView txtVeg = (TextView) customView.findViewById(R.id.moTxtVeg);
        TextView txtDessert = (TextView) customView.findViewById(R.id.moTxtDes);

        txtTag.setText(tag);
        txtFleisch.setText(fleisch);
        txtVeg.setText(veg);
        txtDessert.setText(dessert);
        return customView;
    }


}

package de.helmholtzschule_frankfurt.helmholtzapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.item.MensaplanItem;

public class MensaAdapter extends ArrayAdapter<MensaplanItem> {

    public MensaAdapter(Context context, ArrayList<MensaplanItem> list) {
        super(context, R.layout.mensa_row, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.mensa_row, parent, false);

        MensaplanItem item = getItem(position);

        String day;
        switch (position) {
            case 0: {
                day = "Montag";
                break;
            }
            case 1: {
                day = "Dienstag";
                break;
            }
            case 2: {
                day = "Mittwoch";
                break;
            }
            case 3: {
                day = "Donnerstag";
                break;
            }
            case 4: {
                day = "Freitag";
                break;
            }
            default: {
                day = "DAY_NOT_FOUND";
            }
        }

        String meat = item.getMeat();
        String veg = item.getVeg();
        String salad = item.getSalad();
        String noodles = item.getNoodles();
        String dessert = item.getDessert();

        TextView txtDay = (TextView) customView.findViewById(R.id.mensa_row_day);
        TextView txtMeat = (TextView) customView.findViewById(R.id.contentMeat);
        TextView txtVeg = (TextView) customView.findViewById(R.id.contentVeg);
        TextView txtSalad = customView.findViewById(R.id.contentSalad);
        TextView txtNoodles = customView.findViewById(R.id.contentNoodles);
        TextView txtDessert = (TextView) customView.findViewById(R.id.contentDessert);

        txtDay.setText(day);
        txtMeat.setText(meat);
        txtVeg.setText(veg);
        txtSalad.setText(salad);
        txtNoodles.setText(noodles);
        txtDessert.setText(dessert);

        System.out.println(item.toString());
        return customView;
    }


}

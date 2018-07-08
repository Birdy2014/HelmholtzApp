package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ColorFieldAdapter extends ArrayAdapter<StundenplanColor>{

    private StundenplanItem item;
    private Dialog dialog;
    private ArrayAdapter adapter;

    public ColorFieldAdapter(Context context, ArrayList<StundenplanColor> list, StundenplanItem item, Dialog dialog, ArrayAdapter adapter) {
        super(context, R.layout.color_chooser_cell, list);
        this.item = item;
        this.dialog = dialog;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.color_chooser_cell, parent, false);


        StundenplanColor color = getItem(position);
        Button button = customView.findViewById(R.id.colorButton);
        button.setBackgroundColor(color.getCode());
        button.setOnClickListener(click -> {
            item.setColor(color);
            adapter.notifyDataSetChanged();
            DataStorage.getInstance().saveStundenplan(getContext());
            dialog.cancel();
        });

        return customView;
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Julian on 28.06.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context, ArrayList<News> list) {
        super(context, R.layout.news_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.news_row, parent, false);


        String titel = getItem(position).getTitel();
        String url = getItem(position).getUrl();

        TextView txtTitel = (TextView) customView.findViewById(R.id.txtTitel);


        txtTitel.setText(titel);

        return customView;
    }
}


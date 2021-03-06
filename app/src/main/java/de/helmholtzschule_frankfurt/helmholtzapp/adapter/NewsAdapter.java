package de.helmholtzschule_frankfurt.helmholtzapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.item.NewsItem;


public class NewsAdapter extends ArrayAdapter<NewsItem> {

    public NewsAdapter(Context context, ArrayList<NewsItem> list) {
        super(context, R.layout.news_row, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.news_row, parent, false);

        //Here you change the border
        //customView.setBackgroundResource(R.drawable.boxes);

        String titel = getItem(position).getTitel();
        TextView txtTitel = (TextView) customView.findViewById(R.id.txtTitel);


        txtTitel.setText(titel);

        return customView;
    }
}


package de.helmholtzschule_frankfurt.helmholtzapp;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.birdy2014.VertretungsplanLib.Vertretung;

class VertretungsplanAdapter extends ArrayAdapter<Vertretung> {

    VertretungsplanAdapter(Context context, ArrayList<Vertretung> list) {
        super(context, R.layout.vertretungsplan_row, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.vertretungsplan_row, parent, false);

        String fach = getItem(position).getFach();
        String klasse = getItem(position).getKlasse();
        String stunde = getItem(position).getStunde();
        String vertretungslehrer = getItem(position).getVertretungsLehrer();
        String fuerLehrer = getItem(position).getFuer();
        String raum = getItem(position).getRaum();
        String hinweis = getItem(position).getHinweis();
        String art = getItem(position).getArt();

        TextView txtKlasse = (TextView) customView.findViewById(R.id.txtKlasse);
        TextView txtStunde = (TextView) customView.findViewById(R.id.txtStunde);
        TextView txtFach = (TextView) customView.findViewById(R.id.txtFach);
        TextView txtVertretungslehrer = (TextView) customView.findViewById(R.id.txtVertretungslehrer);
        TextView txtFuerLehrer = (TextView) customView.findViewById(R.id.txtFuerLehrer);
        TextView txtRaum = (TextView) customView.findViewById(R.id.txtRaum);
        TextView txtHinweis = (TextView) customView.findViewById(R.id.txtHinweis);
        TextView txtArt = (TextView) customView.findViewById(R.id.txtArt);


        txtFach.setText(fach);
        txtKlasse.setText(klasse);
        txtStunde.setText("Stunde: " + stunde);
        txtVertretungslehrer.setText(vertretungslehrer);
        txtFuerLehrer.setText(fuerLehrer);
        txtRaum.setText(raum);
        txtHinweis.setText(hinweis);
        txtArt.setText(art);

        switch (art){
            case "Veranst.": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_veranstaltung));
                break;
            }
            case "Vertretung": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_vertretung));
                break;
            }
            case "Sondereins.": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_sondereinsatz));
                break;
            }
            case "Unterricht geändert": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_aenderung));
                break;
            }
            case "Trotz Absenz": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_absenzl));
                break;
            }
            case "Entfall": {
                customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_entfall));
            }
            if(art.contains("Arbeiten"))customView.setBackground(getContext().getResources().getDrawable(R.drawable.vertretungsplan_row_background_eigarbeiten));
        }

        return customView;
    }
}

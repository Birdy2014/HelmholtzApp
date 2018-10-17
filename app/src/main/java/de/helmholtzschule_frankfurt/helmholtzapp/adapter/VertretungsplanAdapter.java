package de.helmholtzschule_frankfurt.helmholtzapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.activity.VertretungsActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.item.Vertretung;

public class VertretungsplanAdapter extends ArrayAdapter<Vertretung> {

    public VertretungsplanAdapter(Context context, ArrayList<Vertretung> list) {
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

        if(fach.equals("") || fach.equals("---"))fach = " ";
        if(klasse.equals("") || klasse.equals("---"))klasse = " ";
        if(stunde.equals("") || stunde.equals("---"))stunde = " ";
        if(vertretungslehrer.equals("") || vertretungslehrer.equals("---"))vertretungslehrer = " ";
        if(fuerLehrer.equals("") || fuerLehrer.equals("---"))fuerLehrer = " ";
        if(raum.equals("") || raum.equals("---"))raum = " ";
        if(hinweis.equals("") || hinweis.equals("---"))hinweis = " ";
        if(art.equals("") || art.equals("---"))art = " ";

        if(stunde.contains("-")){
            StringBuilder firstDigit = new StringBuilder();
            for(int i = 0; i < stunde.length(); i++){
                if(Character.isDigit(stunde.charAt(i)))firstDigit.append(stunde.charAt(i));
                if(stunde.charAt(i) == '-')break;
            }
            StringBuilder secondDigit = new StringBuilder();
            for(int i = stunde.indexOf("-") + 1; i < stunde.length(); i++){
                if(Character.isDigit(stunde.charAt(i)))secondDigit.append(stunde.charAt(i));
            }
            stunde = firstDigit.toString() + " - " + secondDigit.toString();
        }

        String extra = stunde + "<!>" + art + "<!>" + fach + "<!>" + klasse + "<!>" + vertretungslehrer + "<!>" + fuerLehrer + "<!>" + raum + "<!>" + hinweis;

        TextView stundeTxt = customView.findViewById(R.id.vertretungsplan_row_hour);
        TextView artTxt = customView.findViewById(R.id.vertretungsplan_row_type);
        TextView subTxt = customView.findViewById(R.id.vertretungsplan_row_sub);

        stundeTxt.setText(stunde.equals(" ") ? "???" : stunde);
        artTxt.setText(art.equals(" ") ? "???" : art);


        String sub = (!fach.equals(" ") ? "(" + fach + ") " : "") + (vertretungslehrer.equals(" ") || vertretungslehrer.equals("+") ? fuerLehrer : vertretungslehrer) + " in " + (raum.equals(" ") ? "???" : raum);
        subTxt.setText(sub);

        View backgroundView = customView.findViewById(R.id.backgroundView);

        int backgroundColor = R.color.colorDefault;
        switch (art){
            case "Veranst.": {
                backgroundColor = R.color.colorVeranstaltung;
                break;
            }
            case "Vertretung": {
                backgroundColor = R.color.colorVertretung;
                break;
            }
            case "Sondereins.": {
                backgroundColor = R.color.colorSondereinsatz;
                break;
            }
            case "Unterricht geändert": {
                backgroundColor = R.color.colorAenderung;
                break;
            }
            case "Trotz Absenz": {
                backgroundColor = R.color.colorAbsenz;
                break;
            }
            case "Entfall": {
                backgroundColor = R.color.colorEntfall;
                break;
            }
            case "eigenv. Arb.": {
                backgroundColor = R.color.colorEigArbeiten;
                break;
            }
            case "Verlegung": {
                backgroundColor = R.color.colorVerlegung;
                break;
            }
            case "Raumänderung": {
                backgroundColor = R.color.colorRaumaenderung;
                break;
            }
            case "Mitbetreuung": {
                backgroundColor = R.color.colorVertretung;
                break;
            }
            case "Tausch": {
                backgroundColor = R.color.colorTausch;
            }
        }
        if (art.contains("Arbeiten")) backgroundColor = R.color.colorEigArbeiten;
        backgroundView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));

        int finalBackgroundColor = backgroundColor;
        customView.setOnClickListener(click -> {
            Intent intent = new Intent(getContext(), VertretungsActivity.class);
            intent.putExtra("color", finalBackgroundColor);
            intent.putExtra("vertretung", extra);
            getContext().startActivity(intent);
        });

        return customView;
    }
}

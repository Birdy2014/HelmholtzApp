package de.helmholtzschule_frankfurt.helmholtzapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Tab7appinfo extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab7appinfo, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView LinkImpressum = (TextView) getActivity().findViewById(R.id.impressum_link);

        Spanned TextImp = Html.fromHtml("<a href='http://www.helmholtzschule-frankfurt.de/impressum-app'>Aktuelles Impressum</a>");

        LinkImpressum.setMovementMethod(LinkMovementMethod.getInstance());
        LinkImpressum.setText(TextImp);

        TextView LinkDatenschutz = (TextView) getActivity().findViewById(R.id.datenschutz_link);

        Spanned TextData = Html.fromHtml("<a href='http://www.helmholtzschule-frankfurt.de/datenschutz-app'>Aktuelle Datenschutzerklärung</a>");

        LinkDatenschutz.setMovementMethod(LinkMovementMethod.getInstance());
        LinkDatenschutz.setText(TextData);
    }
}
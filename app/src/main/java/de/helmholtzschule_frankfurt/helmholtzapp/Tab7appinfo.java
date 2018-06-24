package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
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

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            ((TextView)getActivity().findViewById(R.id.version_view2)).setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView linkImpressum = (TextView) getActivity().findViewById(R.id.impressum_link);
        Spanned textImpressum = Html.fromHtml("<a href='http://www.helmholtzschule-frankfurt.de/impressum-app'>Aktuelles Impressum</a>");
        linkImpressum.setMovementMethod(LinkMovementMethod.getInstance());
        linkImpressum.setText(textImpressum);


        TextView linkDatenschutz = (TextView) getActivity().findViewById(R.id.datenschutz_link);
        Spanned textDatenschutz = Html.fromHtml("<a href='http://www.helmholtzschule-frankfurt.de/datenschutz-app'>Aktuelle Datenschutzerklärung</a>");
        linkDatenschutz.setMovementMethod(LinkMovementMethod.getInstance());
        linkDatenschutz.setText(textDatenschutz);

        TextView linkStore = (TextView) getActivity().findViewById(R.id.store_link);
        Spanned textStore = Html.fromHtml("<a href='https://play.google.com/store/apps/details?id=de.helmholtzschule_frankfurt.helmholtzapp&hl=de'>Google Play<</a>");
        linkStore.setMovementMethod(LinkMovementMethod.getInstance());
        linkStore.setText(textStore);
    }
}
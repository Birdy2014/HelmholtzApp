package de.helmholtzschule_frankfurt.helmholtzapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab3kalender extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2kalender, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        WebView webView = (WebView) getView().findViewById(R.id.webView);
        webView.loadUrl("http://calendar.google.com/calendar/embed?src=helmholtzschule-ffm.net_9nvqfopgdvrspcvj93q2b4p188%40group.calendar.google.com&ctz=EuropeBerlin");
        //webView.loadData(Karlfritzke, "text/html", null);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        super.onViewCreated(view, savedInstanceState);
    }
}

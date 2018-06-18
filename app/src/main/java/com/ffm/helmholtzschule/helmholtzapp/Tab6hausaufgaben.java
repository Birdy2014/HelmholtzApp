package com.ffm.helmholtzschule.helmholtzapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.view.LayoutInflater;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Tab6hausaufgaben extends Fragment {
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab6hausaufgaben, container, false);

        webView = (WebView) v.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.loadUrl("https://hmwk.me/mobile");

        webView.getSettings().setJavaScriptEnabled(true);

        return v;
    }
}

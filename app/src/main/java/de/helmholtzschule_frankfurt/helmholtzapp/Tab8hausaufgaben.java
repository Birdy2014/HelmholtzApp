package de.helmholtzschule_frankfurt.helmholtzapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Tab8hausaufgaben extends Fragment {

    WebView webView;

    public class MyWebViewClient extends WebViewClient {
        public boolean shuldOverrideKeyEvent (WebView view, KeyEvent event) {

            return true;
        }

        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            if (Uri.parse(url).getHost().equals("hmwk.me")) {
                // This is my web site, so do not override; let my WebView load the page
                if(Uri.parse(url).getPath().equals("/")) {
                    view.loadUrl("https://hmwk.me/mobile");
                } else {
                    return false;
                }
            }

            // reject anything other
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl(
                    "javascript:(function() {" +
                            "var nav = document.getElementsByClassName('nav-wrapper');" +
                            "nav[0].style.display=\"none\";" +
                            "var blueElements = $(\".blue\");" +
                            "for(var i = 0; i <= blueElements.length; i++) {" +
                            "blueElements[i].style.backgroundColor=\"#5a9016\";" +
                            "blueElements[i].classList.remove('blue');" +
                            "}" +
                            "})()"
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.tab8hausaufgaben, container, false);

        webView = (WebView) view.findViewById(R.id.webView);
        // Apply upper WebViewClient
        webView.setWebViewClient(new MyWebViewClient());
        // enable JS
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (savedInstanceState == null) {
            webView.loadUrl("https://hmwk.me/mobile");
        }

        return view;
    }
    public void openPlaystore(View v) {
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hmwk.homework"));
        startActivity(browser);
    }

    public void goBack() {
        webView.goBack();
    }
}

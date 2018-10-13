package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import de.helmholtzschule_frankfurt.helmholtzapp.R;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Tab6hausaufgaben extends Fragment {

    private WebView webView;
    private static String lastSelectedClass = null;

    public class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {

            return true;
        }

        public boolean shouldOverrideUrlLoading (WebView view, String url) {

            URI originalUri = null;
            try {
                originalUri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            String query = originalUri.getQuery();

            query = (query == null) ? "hhsapp=true" : query + "&hhsapp=true";

            try {

                URI newUri = new URI(originalUri.getScheme(), originalUri.getAuthority(),
                        originalUri.getPath(), query, originalUri.getFragment());

                if (newUri.getHost().equals("hmwk.me")) {
                    // This is my web site, so do not override; let my WebView load the page
                    if (newUri.getPath().equals("/")) {
                        view.loadUrl("https://hmwk.me/mobile?hhsapp=true");
                    } else if (newUri.getPath().equals("/selectSchool")) {
                        String postData = "school=Helmholtzschule&schoolPassword=1912";
                        view.postUrl("https://hmwk.me/selectSchool", Base64.encode(postData.getBytes(), Base64.DEFAULT));
                    } else {
                        view.loadUrl(String.valueOf(newUri.toURL()));
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // reject anything other
            return true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab6hausaufgaben, container, false);

        // Check if school class has changed

        String selectedClass = HelmholtzDatabaseClient.getInstance().getKlasse();
        webView = (WebView) view.findViewById(R.id.webView);
        // Apply upper WebViewClient
        webView.setWebViewClient(new MyWebViewClient());
        // enable JS
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (lastSelectedClass != null && !lastSelectedClass.equals(selectedClass)) {
            webView.loadUrl("https://hmwk.me/leaveClass");
        } else if (savedInstanceState == null) {
            webView.loadUrl("https://hmwk.me/mobile?hhsapp=true");
        }


        lastSelectedClass = selectedClass;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView ivHomework = getActivity().findViewById(R.id.ivHomework);
        ivHomework.setOnClickListener(l -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.hmwk.homework"));
            startActivity(browser);
        });
    }

    public void goBack() {
        webView.goBack();
    }
}

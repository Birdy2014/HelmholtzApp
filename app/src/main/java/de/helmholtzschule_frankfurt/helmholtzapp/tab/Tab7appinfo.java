package de.helmholtzschule_frankfurt.helmholtzapp.tab;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.helmholtzschule_frankfurt.helmholtzapp.R;


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
        linkImpressum.setOnClickListener(listener -> {
            String url = "http://www.helmholtzschule-frankfurt.de/impressum-app";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(intent, "Öffnen in"));
        });

        TextView linkDatenschutz = (TextView) getActivity().findViewById(R.id.datenschutz_link);
        linkDatenschutz.setOnClickListener(listener -> {
            String url = "http://www.helmholtzschule-frankfurt.de/datenschutz-app";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(intent, "Öffnen in"));
        });

        TextView linkLoginDatenschutz = (TextView) getActivity().findViewById(R.id.datenschutz_login_link);
        linkLoginDatenschutz.setOnClickListener(listener -> {
            String url = "https://helmholtz-database.lazybird.me/dashboard/impressum.php";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(intent, "Öffnen in"));
        });

        TextView linkStore = (TextView) getActivity().findViewById(R.id.store_link);
        linkStore.setOnClickListener(listener -> {
            String url = "https://play.google.com/store/apps/details?id=de.helmholtzschule_frankfurt.helmholtzapp&hl=de";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(Intent.createChooser(intent, "Öffnen in"));
        });

        TextView linkMail = (TextView) getActivity().findViewById(R.id.feedbackmail_address);
        linkMail.setText(getActivity().getResources().getString(R.string.emailHelp));
        linkMail.setOnClickListener(listener -> {
            String mailStr = "mailto:" + getActivity().getResources().getString(R.string.emailHelp);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(mailStr));
            startActivity(Intent.createChooser(intent, "Senden via"));
        });
    }
}
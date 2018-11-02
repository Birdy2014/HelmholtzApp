package de.helmholtzschule_frankfurt.helmholtzapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab0news;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab10settings;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab1vertretungsplan;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab2benachrichtigungen;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab3kalender;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab4mensa;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab5lehrerliste;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab6hausaufgaben;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab7stundenplan;
import de.helmholtzschule_frankfurt.helmholtzapp.tab.Tab8appinfo;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private static int menuIndexSelected = 0;
    private Tab0news tabNews = new Tab0news();
    private Tab1vertretungsplan tabVertretungsplan = new Tab1vertretungsplan();
    private Tab2benachrichtigungen tabBenachrichtigungen = new Tab2benachrichtigungen();
    private Tab3kalender tabKalender = new Tab3kalender();
    private Tab4mensa tabMensa = new Tab4mensa();
    private Tab5lehrerliste tabLehrerliste = new Tab5lehrerliste();
    private Tab6hausaufgaben tabHausaufgaben = new Tab6hausaufgaben();
    private Tab7stundenplan tabStundenplan = new Tab7stundenplan();
    private Tab8appinfo tabAppinfo = new Tab8appinfo();
    private Tab10settings tabSettings = new Tab10settings();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException);
        setContentView(R.layout.activity_main);
        boolean b = getIntent().getBooleanExtra("background", false);
        if (b) moveTaskToBack(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                System.out.println(findViewById(R.id.sub_text));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Hide keyboard
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) view = new View(getApplication());
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                TextView textView = (TextView) findViewById(R.id.sub_text);
                if (textView != null) {
                    textView.setText("Frankfurt am Main                 Klasse " + HelmholtzDatabaseClient.getInstance().getKlasse());
                }
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                if (imageView != null) {
                    imageView.setOnClickListener(click -> {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://tools.lazybird.me/redirect.php?target=hhs-app-hhs-web"));
                        startActivity(Intent.createChooser(intent, "Öffnen in"));
                    });
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton button = (ImageButton) findViewById(R.id.refresh_toolbar);
        button.setOnClickListener(view -> {

            for (int i = 0; i < navigationView.getMenu().size(); i++) {
                if (navigationView.getMenu().getItem(i).isChecked()) {
                    menuIndexSelected = i;
                }
            }
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
            intent.putExtra("fragmentIndex", menuIndexSelected);
            int x = -1;
            switch (menuIndexSelected) {
                case 0: {
                    x = 0;
                    break;
                }
                case 2: {
                    x = 1;
                    break;
                }
                case 1: {
                    x = 1;
                    break;
                }
                case 4: {
                    x = 2;
                    break;
                }
                case 5: {
                    x = 3;
                }
            }
            intent.putExtra("toDownload", new int[]{x});
            startActivity(intent);
        });
        ImageButton notification = (ImageButton) findViewById(R.id.showAlert);
        if (DataStorage.getInstance().getMessage() != null) {
            HashMap<String, String> msg = DataStorage.getInstance().getMessage();
            SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
            int id = Integer.parseInt(mySPR.getString("latestAlertID", "0"));
            if (Integer.parseInt(msg.get("id")) > id) {
                notification.setImageResource(R.drawable.ic_notification_active);
            }
            notification.setOnClickListener(click -> {
                Dialog dialog = new Dialog(MainActivity.this);
                View dialogView = View.inflate(MainActivity.this, R.layout.layout_notification, new LinearLayout(MainActivity.this));
                dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ImageButton dialogButton = dialogView.findViewById(R.id.dialogButton);
                dialogButton.setOnClickListener(click1 -> dialog.cancel());
                TextView notificationText = dialogView.findViewById(R.id.notificationText);
                notificationText.setText(msg.get("message"));
                dialog.show();
                notification.setImageResource(R.drawable.ic_notification_inactive);
                SharedPreferences.Editor editor = mySPR.edit();
                editor.putString("latestAlertID", msg.get("id"));
                editor.apply();

            });
        } else {
            notification.setVisibility(View.GONE);
        }

        menuIndexSelected = getIntent().getIntExtra("fragmentIndex", 0);

        switchTab(menuIndexSelected, navigationView);
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        if (mySPR.getBoolean("FR", true)) {
            SharedPreferences.Editor editor = mySPR.edit();
            editor.putBoolean("FR", false);
            editor.apply();
            showNewDialog(R.layout.welcome_layout);
            FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + HelmholtzDatabaseClient.getInstance().getKlasse().toLowerCase());
        }
    }

    private void switchTab(int index, NavigationView navigationView) {
        navigationView.getMenu().getItem(index).setChecked(true);
        //Hide keyboard
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) view = new View(this);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch (index) {
            case 9: {
                //Fallthrough expected, @Share function
            }
            case 0: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabNews, tabNews.getTag()).commit();
                break;
            }
            case 1: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabVertretungsplan, tabVertretungsplan.getTag()).commit();
                break;
            }
            case 2: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabBenachrichtigungen, tabBenachrichtigungen.getTag()).commit();
                break;
            }
            case 3: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabKalender, tabKalender.getTag()).commit();
                break;
            }
            case 4: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabMensa, tabMensa.getTag()).commit();
                break;
            }
            case 5: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabLehrerliste, tabLehrerliste.getTag()).commit();
                break;
            }
            case 6: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabHausaufgaben, tabHausaufgaben.getTag()).commit();
                break;
            }
            case 7: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabStundenplan, tabStundenplan.getTag()).commit();
                break;
            }
            case 8: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabAppinfo, tabAppinfo.getTag()).commit();
                break;
            }
            case 10: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabSettings, tabSettings.getTag()).commit();
                break;
            }
        }
    }

    public void showNewDialog(int layout) {

        Dialog dialog = new Dialog(MainActivity.this);
        View dialogView = View.inflate(MainActivity.this, layout, new LinearLayout(MainActivity.this));
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button button = dialogView.findViewById(R.id.dialogButton);
        button.setOnClickListener(click -> dialog.cancel());
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_news: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabNews, tabNews.getTag()).commit();
                break;
            }
            case R.id.nav_vertretungsplan: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabVertretungsplan, tabVertretungsplan.getTag()).commit();
                break;
            }
            case R.id.nav_nachrichten: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabBenachrichtigungen, tabBenachrichtigungen.getTag()).commit();
                break;
            }
            case R.id.nav_kalender: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabKalender, tabKalender.getTag()).commit();
                break;
            }
            case R.id.nav_mensaplan: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabMensa, tabMensa.getTag()).commit();
                break;
            }
            case R.id.nav_settings: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabSettings, tabSettings.getTag()).commit();
                break;
            }
            case R.id.nav_lehrerliste: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabLehrerliste, tabLehrerliste.getTag()).commit();
                break;
            }
            case R.id.nav_about: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabAppinfo, tabAppinfo.getTag()).commit();
                break;
            }
            case R.id.nav_homework: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabHausaufgaben, tabHausaufgaben.getTag()).commit();
                break;
            }
            case R.id.nav_share: {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Lade dir jetzt die kostenlose Helmholtz - App herunter!" + getResources().getString(R.string.hhs_app_store);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Teilen via"));
                break;
            }
            case R.id.nav_stundenplan: {
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, tabStundenplan, tabStundenplan.getTag()).commit();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (tabHausaufgaben.isVisible()) {
            tabHausaufgaben.goBack();
        } else {
            SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);

            int indexTab = 0;
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            for (int i = 0; i < navigationView.getMenu().size(); i++) {
                if (navigationView.getMenu().getItem(i).isChecked()) {
                    indexTab = i;
                }
            }
            System.out.println("ST: " + mySPR.getInt("standardTab", 0) + " Index: " + indexTab);
            if (mySPR.getInt("standardTab", 0) == indexTab) {
                Dialog dialog = new Dialog(this);
                View dialogView = View.inflate(this, R.layout.close_dialog, null);
                dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Button cancelButton = dialogView.findViewById(R.id.close_button_cancel);
                Button continueButton = dialogView.findViewById(R.id.close_button_close);
                cancelButton.setOnClickListener(click -> dialog.cancel());
                continueButton.setOnClickListener(click -> {
                    dialog.cancel();
                    this.finishAffinity();
                });
                dialog.show();
            } else {
                switchTab(mySPR.getInt("standardTab", 0), navigationView);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void handleUncaughtException(Thread thread, Throwable throwable) {
        String stacktrace = Log.getStackTraceString(throwable);
        String message = throwable.getMessage();
        System.out.println(stacktrace);
        /*if (stacktrace.contains("Unable to start")) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            int mPendingIntentId = 69;
            AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, mPendingIntent);
            finishAffinity();
            System.exit(0);
        } else {*/
            Intent intent = new Intent(MainActivity.this, SendActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("stacktrace", stacktrace);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
        //}
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private static int menuIndexSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                TextView view = (TextView)findViewById(R.id.sub_text);
                if(view != null){
                    view.setText("Frankfurt am Main                 Klasse " + HelmholtzDatabaseClient.getInstance().getKlasse());
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton button = (ImageButton)findViewById(R.id.refresh_toolbar);
        button.setOnClickListener(view -> {
            for (int i = 0; i < navigationView.getMenu().size(); i++){
                if(navigationView.getMenu().getItem(i).isChecked()){
                    menuIndexSelected = i;
                }
            }
            Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
            intent.putExtra("fragmentIndex", menuIndexSelected);
            startActivity(intent);
        });
        menuIndexSelected = getIntent().getIntExtra("fragmentIndex", 0);

        navigationView.getMenu().getItem(menuIndexSelected).setChecked(true);
        switch (menuIndexSelected){
            case 9: {
                //Fallthrough expected, @Share function
            }
            case 0: {
                Tab0news news = new Tab0news();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, news, news.getTag()).commit();
                break;
            }
            case 1: {
                Tab1vertretungsplan vertretungsplan = new Tab1vertretungsplan();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, vertretungsplan, vertretungsplan.getTag()).commit();
                break;
            }
            case 2: {
                Tab2benachrichtigungen benachrichtigungen = new Tab2benachrichtigungen();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, benachrichtigungen, benachrichtigungen.getTag()).commit();
                break;
            }
            case 3: {
                Tab3kalender kalender = new Tab3kalender();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, kalender, kalender.getTag()).commit();
                break;
            }
            case 4: {
                Tab4mensa mensa = new Tab4mensa();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, mensa, mensa.getTag()).commit();
                break;
            }
            case 10: {
                Tab8settings settings = new Tab8settings();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, settings, settings.getTag()).commit();
                break;
            }
            case 5: {
                Tab5lehrerliste lehrerListe = new Tab5lehrerliste();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, lehrerListe, lehrerListe.getTag()).commit();
                break;
            }
            case 8 :{
                Tab7appinfo about = new Tab7appinfo();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, about, about.getTag()).commit();
                break;
            }
            case 6: {
                Tab6hausaufgaben hausaufgaben = new Tab6hausaufgaben();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, hausaufgaben, hausaufgaben.getTag()).commit();
                break;
            }
            case 7: {
                Tab9Stundenplan stundenplan = new Tab9Stundenplan();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, stundenplan, stundenplan.getTag()).commit();
                break;
            }
        }
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        if(mySPR.getBoolean("FR", true)){
            showDialog();
        }
    }
    public void showDialog(){
        SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
        SharedPreferences.Editor editor = mySPR.edit();
        editor.putBoolean("FR", false);
        editor.apply();

        Dialog dialog = new Dialog(MainActivity.this);
        View dialogView = View.inflate(MainActivity.this, R.layout.welcome_layout, new LinearLayout(MainActivity.this));
        dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Button button = dialogView.findViewById(R.id.buttonWelcome);
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

        switch (item.getItemId()){
            case R.id.nav_news: {
                Tab0news news = new Tab0news();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, news, news.getTag()).commit();
                break;
            }
            case R.id.nav_vertretungsplan: {
                Tab1vertretungsplan vertretungsplan = new Tab1vertretungsplan();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, vertretungsplan, vertretungsplan.getTag()).commit();
                break;
            }
            case R.id.nav_nachrichten: {
                Tab2benachrichtigungen benachrichtigungen = new Tab2benachrichtigungen();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, benachrichtigungen, benachrichtigungen.getTag()).commit();
                break;
            }
            case R.id.nav_kalender: {
                Tab3kalender kalender = new Tab3kalender();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, kalender, kalender.getTag()).commit();
                break;
            }
            case R.id.nav_mensaplan: {
                Tab4mensa mensa = new Tab4mensa();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, mensa, mensa.getTag()).commit();
                break;
            }
            case R.id.nav_settings: {
                Tab8settings settings = new Tab8settings();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, settings, settings.getTag()).commit();
                break;
            }
            case R.id.nav_lehrerliste: {
                Tab5lehrerliste lehrerListe = new Tab5lehrerliste();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, lehrerListe, lehrerListe.getTag()).commit();
                break;
            }
            case R.id.nav_about: {
                Tab7appinfo about = new Tab7appinfo();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, about, about.getTag()).commit();
                break;
            }
            case R.id.nav_homework: {
                Tab6hausaufgaben hausaufgaben = new Tab6hausaufgaben();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, hausaufgaben, hausaufgaben.getTag()).commit();
                break;
            }
            case R.id.nav_share: {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Lade dir jetzt die kostenlose Helmholtz - App herunter!\nhttps://play.google.com/store/apps/details?id=de.helmholtzschule_frankfurt.helmholtzapp&hl=de";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Teilen via"));
                break;
            }
            case R.id.nav_stundenplan: {
                Tab9Stundenplan stundenplan = new Tab9Stundenplan();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, stundenplan, stundenplan.getTag()).commit();
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Dialog dialog = new Dialog(this);
            View dialogView = View.inflate(this, R.layout.close_dialog, null);
            dialog.setContentView(dialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Button cancelButton = dialogView.findViewById(R.id.close_button_cancel);
            Button continueButton = dialogView.findViewById(R.id.close_button_close);
            cancelButton.setOnClickListener(click -> dialog.cancel());
            continueButton.setOnClickListener(click -> {
                dialog.cancel();
                super.onBackPressed();
            });
            dialog.show();
        }
    }
}

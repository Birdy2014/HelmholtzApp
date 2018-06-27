package de.helmholtzschule_frankfurt.helmholtzapp;

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
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private static int menuIndexSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                SharedPreferences mySPR = getSharedPreferences("MySPFILE", 0);
                ((TextView)findViewById(R.id.sub_text)).setText("Frankfurt am Main                 Klasse " + mySPR.getString("klasse", ""));
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

        ImageButton button = (ImageButton) findViewById(R.id.refresh_toolbar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < navigationView.getMenu().size(); i++){
                    if(navigationView.getMenu().getItem(i).isChecked()){
                        menuIndexSelected = i;
                    }
                }
                Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
                intent.putExtra("fragmentIndex", menuIndexSelected);
                startActivity(intent);
            }
        });
        menuIndexSelected = getIntent().getIntExtra("fragmentIndex", 0);

        navigationView.getMenu().getItem(menuIndexSelected).setChecked(true);
        switch (menuIndexSelected){
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
            case 8: {
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
            case 7 :{
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
        }
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
        // Handle navigation view item clicks here.

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
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

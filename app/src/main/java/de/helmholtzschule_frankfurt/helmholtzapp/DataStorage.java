package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;

class DataStorage {

    private static final DataStorage ourInstance = new DataStorage();
    private String base64credentials;
    private Vertretungsplan vertretungsplan;
    private String mensaplanRawData;
    private String newsRawData;
    private ArrayList<Mensaplan> gerichte;
    private ArrayList<News> news;
    private String lehrerlisteRawData;
    private String[] lehrerliste;
    String[] klassen;
    private boolean pushNotificationsActive = true;

    public static DataStorage getInstance() {
        return ourInstance;
    }

    private DataStorage() {
        String[] posfixes = {"a", "b", "c", "d", "e"};
        ArrayList<String> list = new ArrayList<>();
        for(int i = 5; i < 10; i++){
            for(String s : posfixes){
                list.add(i + s);
            }
        }
        list.add("E1");
        list.add("E2");
        for(int i = 1; i < 5; i++){
            list.add("Q" + i);
        }
        klassen = list.toArray(new String[]{});
    }

    private boolean isInitialized() {
        return base64credentials != null;
    }

    public void initialize(String base64credentials) {
        //if (isInitialized()) return;
        this.base64credentials = base64credentials;
        vertretungsplan = new Vertretungsplan(base64credentials);
    }

    public void update(Activity a) throws NoConnectionException {
        Thread thread = new Thread(() -> {
            try {
                ProgressBar bar = a.findViewById(R.id.progressBar2);
                vertretungsplan.updateVertretungsplan();
                bar.setProgress(35);
                mensaplanRawData = download("https://unforkablefood.000webhostapp.com");
                bar.setProgress(45);
                newsRawData = download("http://helmholtzschule-frankfurt.de");
                bar.setProgress(70);
                lehrerlisteRawData = download("http://unforkablefood.000webhostapp.com/lehrerliste/lehrerliste.json");
                bar.setProgress(100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mensaplanRawData == null || newsRawData == null) throw new NoConnectionException("No internet connection present.");
        parseMensaplan();
        parseNews();
        parseLehrerliste();
    }

    private String download(String website) throws IOException {
        URLConnection connection = new URL(website).openConnection();
        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        return data;
    }

    private void parseMensaplan() {
        gerichte = new ArrayList<>();

        Gson gson = new Gson();
        String[][] data = gson.fromJson(mensaplanRawData, new TypeToken<String[][]>() {}.getType());
        gerichte.add(new Mensaplan("Montag", data[0][0], data[0][1], data[0][2]));
        gerichte.add(new Mensaplan("Dienstag", data[1][0], data[1][1], data[1][2]));
        gerichte.add(new Mensaplan("Mittwoch", data[2][0], data[2][1], data[2][2]));
        gerichte.add(new Mensaplan("Donnerstag", data[3][0], data[3][1], data[3][2]));
        gerichte.add(new Mensaplan("Freitag", data[4][0], data[4][1], data[4][2]));
    }

    private void parseNews(){
        news = new ArrayList<>();

        Document doc = Jsoup.parse(newsRawData);
        Elements titles = doc.getElementsByClass("node__title");
        for(Element e : titles){
            News news = new News(e.children().get(0).children().get(0).html(), "http://helmholtzschule-frankfurt.de" + e.children().get(0).attr("href"));
            this.news.add(news); // adding object to news list
        }
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }

    public ArrayList<Mensaplan> getGerichte() {
        return gerichte;
    }

    public String[] getLehrerliste() {
        return lehrerliste;
    }

    public boolean isInternetReachable() {
        final boolean[] reachable = new boolean[1];
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("http://www.helmholtzschule-frankfurt.de");
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                Object objData = urlConnect.getContent();
                reachable[0] = true;
            } catch (Exception e) {
                reachable[0] = false;
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return reachable[0];
    }

    private void parseLehrerliste() {
        Gson gson = new Gson();
        lehrerliste = gson.fromJson(lehrerlisteRawData, new TypeToken<String[]>() {}.getType());
    }

    public void setKlasse(Activity activity, String klasse){
        SharedPreferences mySPR = activity.getSharedPreferences("MySPFILE", 0);
        SharedPreferences.Editor editor = mySPR.edit();

        if(klasse.contains("q") || klasse.charAt(0) == 'e') klasse = klasse.toUpperCase();
        if(Character.isDigit(klasse.charAt(0)))klasse = klasse.toLowerCase();

        editor.putString("klasse", klasse);
        editor.apply();

        // PUSH Notifications
        unscribeAll();
        FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + klasse);
    }

    public String getKlasse(Activity activity){
        SharedPreferences mySPR = activity.getSharedPreferences("MySPFILE", 0);
        return mySPR.getString("klasse", "");
    }

    private void unscribeAll(){
        for(int i=5; i<11; i++){
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra."+i+"a");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra."+i+"b");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra."+i+"c");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra."+i+"d");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra."+i+"e");
        }
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.e1");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.e2");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.q1");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.q2");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.q3");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra.q4");
    }

    public void setPushNotificationsActive(boolean b, Activity activity){
        pushNotificationsActive = b;
        unscribeAll();
        if(b){
            FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + getKlasse(activity));
        }
    }
}

package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipSession;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Scanner;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;

class DataStorage{

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
    private ArrayList<StundenplanCell> stundenplan = new ArrayList<>();
    private String[][] unterMittelstufenZeiten = {{"08:00", "08:45"}, {"08:50", "09:35"}, {"09:55", "10:40"}, {"10:45", "11:30"}, {"11:50", "12:35"}, {"12:40", "13:25"}, {"14:00", "14:45"}, {"14:50", "15:35"}, {"15:40", "16:25"}, {"16:30", "17:15"}, {"17:15", "18:00"}};
    private String[][] oberstufenZeiten = {{"08:00", "08:45"}, {"08:50", "09:35"}, {"09:55", "10:40"}, {"10:45", "11:30"}, {"11:50", "12:35"}, {"12:40", "13:25"}, {"13:30", "14:15"}, {"14:50", "15:35"}, {"15:40", "16:25"}, {"16:30", "17:15"}, {"17:15", "18:00"}};

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

    public void update(Activity a) throws NoConnectionException{
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
                fillStundenplan(a);
                bar.setProgress(100);
                if (mensaplanRawData == null || newsRawData == null || lehrerlisteRawData == null)return;
                parseMensaplan();
                parseNews();
                parseLehrerliste();
                int index = a.getIntent().getIntExtra("fragmentIndex", 0);
                Intent intent = new Intent(a, MainActivity.class);
                intent.putExtra("fragmentIndex", index);
                a.startActivity(intent);
                a.finish();
            }
            catch (UnknownHostException e){
                System.out.println("Download error. How to fix it?");
                a.findViewById(R.id.loadingtext).post(() -> ((TextView)a.findViewById(R.id.loadingtext)).setText("Download Timeout"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String download(String website) throws IOException {
        URLConnection connection = new URL(website).openConnection();
        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        System.out.println(data);
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

        editor.putString("klasse", klasse);
        editor.apply();

        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra." + mySPR.getString("klasse", "5a").toLowerCase());
        FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + klasse);
    }

    public String getKlasse(Activity activity) {
        SharedPreferences mySPR = activity.getSharedPreferences("MySPFILE", 0);
        return mySPR.getString("klasse", "");
    }

    public void setPushNotificationsActive(boolean b, Activity activity) {
        //add unsubscribeAll
        FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra." + activity.getSharedPreferences("MySPFILE", 0).getString("klasse", "5a").toLowerCase());
        if (b) {
            FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + getKlasse(activity));
        }
    }

    public StundenplanCellTime getTimeAtHour(int hour, Activity activity){
        if(Character.isDigit(getKlasse(activity).charAt(0))){ //Non - Oberstufe
            return new StundenplanCellTime(unterMittelstufenZeiten[hour][0], unterMittelstufenZeiten[hour][1]);
        }
        return new StundenplanCellTime(oberstufenZeiten[hour][0], oberstufenZeiten[hour][1]);
    }

    public void fillStundenplan(Activity a){
        int hours = 11;

        Gson gson = new Gson();
        TypeToken<ArrayList<StundenplanItem>> token = new TypeToken<ArrayList<StundenplanItem>>(){};
        ArrayList<StundenplanItem> list = gson.fromJson(readFromInternalStorage(a.getBaseContext(), "stundenplan.json"), token.getType());
        System.out.println(readFromInternalStorage(a.getBaseContext(), "stundenplan.json"));
        readFromInternalStorage(a.getBaseContext(), "stundenplan.json");
        stundenplan.clear();
        for(int i = 0; i < hours * 6; i++)stundenplan.add(new StundenplanItem(null, null, null, StundenplanColor.WHITE));
        int index = 0;
        for(int i = 0; i < stundenplan.size(); i++){
            if(i % 6 == 0){
                stundenplan.set(i, getTimeAtHour(i / 6, a));
            }
            else {
                if(list == null)continue;
                stundenplan.set(i, list.get(index++));
            }
        }
    }

    public void saveStundenplan(Context context){
        Gson gson = new Gson();
        ArrayList<StundenplanItem> list = new ArrayList<>();
        for(StundenplanCell c : stundenplan)if(c instanceof StundenplanItem)list.add(((StundenplanItem)c));
        String JSON = gson.toJson(list);
        writeToInternalStorage(context, "stundenplan.json", JSON);
    }

    public ArrayList<StundenplanCell> getStundenplan() {
        return stundenplan;
    }

    private String readFromInternalStorage(Context context, String saveFileName){
        File file = new File(context.getFilesDir(), "dir");
        File saveFile = new File(file, saveFileName);
        String s = "";
        try {
            Scanner scanner = new Scanner(saveFile);
            while (scanner.hasNextLine())s += scanner.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return s;
    }

    private void writeToInternalStorage(Context context, String saveFileName, String saveBody){
        File file = new File(context.getFilesDir(), "dir");
        if(!file.exists())file.mkdir();
        try {
            File saveFile = new File(file, saveFileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.append(saveBody);
            writer.flush();
            writer.close();
            System.out.println("Saved String successfully to " + saveFile.getAbsolutePath());
            System.out.println("File exists: " + saveFile.exists());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

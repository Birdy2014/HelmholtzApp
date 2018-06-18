package com.ffm.helmholtzschule.helmholtzapp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
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
    private ArrayList<Mensaplan> gerichte  = new ArrayList<>();
    private ArrayList<News> news = new ArrayList<>();

    public static DataStorage getInstance() {
        return ourInstance;
    }

    private DataStorage() {
    }

    private boolean isInitialized() {
        return base64credentials != null;
    }

    public void initialize(String base64credentials) {
        //if (isInitialized()) return;
        this.base64credentials = base64credentials;
        vertretungsplan = new Vertretungsplan(base64credentials);
    }

    public void update() throws NoConnectionException {
        Thread thread = new Thread(() -> {
            try {
                vertretungsplan.updateVertretungsplan();
                mensaplanRawData = download("https://unforkablefood.000webhostapp.com");
                newsRawData = download("http://helmholtzschule-frankfurt.de");
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
        Gson gson = new Gson();
        String[][] data = gson.fromJson(mensaplanRawData, new TypeToken<String[][]>() {
        }.getType());
        gerichte.add(new Mensaplan("Montag", data[0][0], data[0][1], data[0][2]));
        gerichte.add(new Mensaplan("Dienstag", data[1][0], data[1][1], data[1][2]));
        gerichte.add(new Mensaplan("Mittwoch", data[2][0], data[2][1], data[2][2]));
        gerichte.add(new Mensaplan("Donnerstag", data[3][0], data[3][1], data[3][2]));
        gerichte.add(new Mensaplan("Freitag", data[4][0], data[4][1], data[4][2]));
    }
    private void parseNews(){
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
}

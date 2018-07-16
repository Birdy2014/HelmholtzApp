package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslCertificate;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.EventListener;
import java.util.GregorianCalendar;
import java.util.Scanner;

import io.github.birdy2014.VertretungsplanLib.Vertretungsplan;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.JULY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

class DataStorage{

    private static final DataStorage ourInstance = new DataStorage();
    private Vertretungsplan vertretungsplan;
    private String mensaplanRawData;
    private String newsRawData;
    private ArrayList<Mensaplan> gerichte;
    private ArrayList<News> news;
    private String lehrerlisteRawData;
    private String[] lehrerliste;
    String[] klassen;
    private StundenplanItem copiedItem = null;
    private ArrayList<StundenplanCell> stundenplan = new ArrayList<>();
    public int hours;
    public final double CONTRASTVAR = 100; //smaller than 127.5 --> white text is preferred @stundenplan, changes influence behavior only after color change
    private String[][] unterMittelstufenZeiten = {{"08:00", "08:45"}, {"08:50", "09:35"}, {"09:55", "10:40"}, {"10:45", "11:30"}, {"11:50", "12:35"}, {"12:40", "13:25"}, {"14:00", "14:45"}, {"14:50", "15:35"}, {"15:40", "16:25"}, {"16:30", "17:15"}, {"17:15", "18:00"}};
    private String[][] oberstufenZeiten = {{"08:00", "08:45"}, {"08:50", "09:35"}, {"09:55", "10:40"}, {"10:45", "11:30"}, {"11:50", "12:35"}, {"12:40", "13:25"}, {"13:30", "14:15"}, {"14:50", "15:35"}, {"15:40", "16:25"}, {"16:30", "17:15"}, {"17:15", "18:00"}};
    private int[] monthYear;
    private ArrayList<ActionContainer> containers = new ArrayList<>();

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

    public void initialize(String base64credentials) {
        String base64credentials1 = base64credentials;
        vertretungsplan = new Vertretungsplan(base64credentials);
    }

    public void update(Activity a) throws NoConnectionException { //Do not remove that!!!
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
                fillContainers();
                monthYear = new int[]{Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR)};
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

    private StundenplanCellTime getTimeAtHour(int hour, Activity activity){
        if(Character.isDigit(getKlasse(activity).charAt(0))){ //Non - Oberstufe
            return new StundenplanCellTime(unterMittelstufenZeiten[hour][0], unterMittelstufenZeiten[hour][1]);
        }
        return new StundenplanCellTime(oberstufenZeiten[hour][0], oberstufenZeiten[hour][1]);
    }

    public void fillStundenplan(Activity a){
        Gson gson = new Gson();
        TypeToken<ArrayList<StundenplanItem>> token = new TypeToken<ArrayList<StundenplanItem>>(){};
        SharedPreferences mySPR = a.getSharedPreferences("MySPFILE", 0);
        hours = mySPR.getInt("stundenzahl", 9);
        ArrayList<StundenplanItem> list = gson.fromJson(mySPR.getString("stundenplan", ""), token.getType());
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
        SharedPreferences mySPR = context.getSharedPreferences("MySPFILE", 0);
        SharedPreferences.Editor editor = mySPR.edit();
        editor.putString("stundenplan", JSON);
        editor.apply();
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
    } //Useful?

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
    }//Useful?

    public StundenplanItem getCopiedItem() {
        return copiedItem;
    }

    public void setCopiedItem(StundenplanItem copiedItem) {
        this.copiedItem = copiedItem;
    }

    public void exportStundenplan(Activity activity){
        System.out.println(Arrays.toString(stundenplan.toArray()));
        Gson gson = new Gson();
        ArrayList<StundenplanItem> list = new ArrayList<>();
        for(StundenplanCell c : stundenplan)if(c instanceof StundenplanItem)list.add(((StundenplanItem)c));
        String JSON = gson.toJson(list);
        writeToExternalStorage(JSON, "Stundenplan.json", activity);
        Toast.makeText(activity.getBaseContext(),"Stundenplan wurde unter\n Documents/HelmholtzApp/Stundenplan.json gesichert.", Toast.LENGTH_SHORT).show();
    }

    public boolean importStundenplan(Activity activity){
        String JSON = readFromExternalStorage("Stundenplan.json", activity);
        if(JSON == null){
            Toast.makeText(activity.getBaseContext(), "Keine Datei unter Documents/HelmholtzApp/Stundenplan.json vorhanden.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            System.out.println(JSON);
            Gson gson = new Gson();
            TypeToken<ArrayList<StundenplanItem>> token = new TypeToken<ArrayList<StundenplanItem>>(){};
            hours = 11;
            SharedPreferences mySPR = activity.getSharedPreferences("MySPFILE", 0);
            SharedPreferences.Editor editor = mySPR.edit();
            editor.putInt("stundenzahl", hours);
            editor.apply();
            ArrayList<StundenplanItem> list = gson.fromJson(JSON, token.getType());
            stundenplan.clear();
            for(int i = 0; i < hours * 6; i++)stundenplan.add(new StundenplanItem(null, null, null, StundenplanColor.WHITE));
            int index = 0;
            for(int i = 0; i < stundenplan.size(); i++){
                if(i % 6 == 0){
                    try {
                        stundenplan.set(i, getTimeAtHour(i / 6, activity));
                    }
                    catch (IndexOutOfBoundsException e){
                        break;
                    }
                }
                else {
                    try {
                        stundenplan.set(i, list.get(index++));
                    }
                    catch (IndexOutOfBoundsException ignored){
                    }
                }
            }
        }
        Toast.makeText(activity.getBaseContext(), "Stundenplan wurde erforlgreich importiert.", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void writeToExternalStorage(String data, String fileName, Activity activity){
        if(Build.VERSION.SDK_INT > 22){
            activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI());
        File dir = new File(root, "HelmholtzApp");
        if(!dir.exists())dir.mkdir();
        System.out.println(dir.getAbsolutePath());
        try {
            File saveFile = new File(dir, fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.append(data);
            writer.flush();
            writer.close();
            System.out.println("Saved String successfully to " + saveFile.getAbsolutePath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String readFromExternalStorage(String fileName, Activity activity){
        if(Build.VERSION.SDK_INT > 22){
            activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI());
        File dir = new File(root, "HelmholtzApp");
        try {
            File saveFile = new File(dir, fileName);
            if(saveFile.exists()){
                StringBuilder s = new StringBuilder();
                Scanner scanner = new Scanner(saveFile);
                while (scanner.hasNextLine()) s.append(scanner.nextLine());
                return s.toString();
            }
            else return null;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<CalendarItem> getCalendarList(int month, int year){
        GregorianCalendar calendar = new GregorianCalendar();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int monthToday = calendar.get(Calendar.MONTH);
        int yearToday = calendar.get(Calendar.YEAR);
        if(month == -1 && year == -1) {
            month = monthYear[0];
            year = monthYear[1];
        }
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int monthMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = 0;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case MONDAY: dayOfWeek = 7; break;
            case TUESDAY: dayOfWeek = 1; break;
            case WEDNESDAY: dayOfWeek = 2; break;
            case THURSDAY: dayOfWeek = 3; break;
            case FRIDAY: dayOfWeek = 4; break;
            case SATURDAY: dayOfWeek = 5; break;
            case SUNDAY: dayOfWeek = 6; break;
        }
        int monthBefore = calendar.get(Calendar.MONTH) == 0 ? 11 : calendar.get(Calendar.MONTH) - 1;
        if(monthBefore == 0)calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        calendar.set(Calendar.MONTH, monthBefore);
        int monthBeforeMax = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int monthNext = month == 11 ? 0 : month + 1;
        System.out.println("Month before: " + monthBefore);
        System.out.println("Month: " + month);
        System.out.println("Month next: " + monthNext);

        ArrayList<CalendarItem> days = new ArrayList<>();
        for(int i = 0; i < 42; i++){
            days.add(new CalendarItem(0, false, false, monthBefore));
        }
        int x = monthBeforeMax - dayOfWeek + 1;
        for(int i = 0; i < dayOfWeek; i++){
            days.get(i).setDay(x++);
        }

        x = 1;
        for(int i = dayOfWeek; i < monthMax + dayOfWeek; i++){
            if(calendar.get(Calendar.MONTH) == monthToday - 1 && x == today && calendar.get(Calendar.YEAR) == yearToday){
                days.get(i).setToday(true);
            }
            days.get(i).setActualMonth(true);
            days.get(i).setMonth(month);

            days.get(i).setDay(x++); //has to be last statement here
        }
        x = 1;
        for(int i = monthMax + dayOfWeek; i < 42; i++){
            days.get(i).setDay(x++);
            days.get(i).setMonth(monthNext);
        }
        return days;
    }

    public ArrayList<CalendarItem> addActionToList(ArrayList<CalendarItem> list, int start, int end, Action action, ActionType type){
        if(type == ActionType.INTERNAL) {
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == 1) break;
            }//i is now the month's start
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == start) break;
            }//i is now the defined start of the action

            int actionIndex = 0;
            int startIndex = i;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end) break;
            }
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end) break;
            }
        }
        else if(type == ActionType.OUTGOING_I){
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == 1) break;
            }//i is now the month's start
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == start) break;
            }//i is now the defined start of the action

            int actionIndex = 0;
            int startIndex = i;
            int x = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end){
                    if(list.get(i).getDay() == end){
                        if(++x == 2)break;
                    }
                }
            }
            x = 0;
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end){
                    if(list.get(i).getDay() == end){
                        if(++x == 2)break;
                    }
                }
            }
        }
        else if(type == ActionType.OUTGOING_II){
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == 1) break;
            }//i is now the month's start
            for (; i < list.size(); i++) {
                if (list.get(i).getDay() == start) break;
            }//i is now the defined start of the action

            int actionIndex = 0;
            int startIndex = i;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
            }
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
            }
        }
        else if(type == ActionType.INGOING){
            int i = 0;
            int startIndex = 0;
            if(list.get(i).getDay() >= start)startIndex = i;
            else {
                for(; i < list.size(); i++){
                    if(list.get(i).getDay() == start){
                        startIndex = i;
                        break;
                    }
                }
            }

            int actionIndex = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end) break;
            }
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end) break;
            }
        }
        else if(type == ActionType.EXTERNAL_IN){
            int i = 0;
            int startIndex = 0;
            if(list.get(i).getDay() >= start) {
                startIndex = i;
            }
            else {
                for(; i < list.size(); i++){
                    if(list.get(i).getDay() == start){
                        startIndex = i;
                        break;
                    }
                    if(list.get(i).getDay() == 1){
                        return list;
                    }
                }
            }
            int actionIndex = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end) break;
            }
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end) break;
            }
        }
        else if(type == ActionType.EXTERNAL_OUT){
            int i = 0;
            for(; i < list.size(); i++){
                if(list.get(i).getDay() == 1)break;
            }//1st 1
            i++;
            for(; i < list.size(); i++){
                if(list.get(i).getDay() == 1)break;
            }//2nd 1

            int startIndex = 0;
            for(; i < list.size(); i++){
                if(list.get(i).getDay() == start){
                    startIndex = i;
                    break;
                }
            }
            if(i >= list.size())return list;
            int actionIndex = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end) break;
            }
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end) break;
            }
        }
        if(type == ActionType.SPANNED){
            int i = 0;
            int startIndex = 0;
            if(list.get(i).getDay() >= start)startIndex = i;
            else {
                for(; i < list.size(); i++){
                    if(list.get(i).getDay() == start){
                        startIndex = i;
                        break;
                    }
                }
            }
            int actionIndex = 0;
            int x = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).getFreeActionIndex() > actionIndex && list.get(i).getFreeActionIndex() != -1)
                    actionIndex = list.get(i).getFreeActionIndex();
                if (list.get(i).getDay() == end){
                    if(list.get(i).getDay() == end){
                        if(x == 2)break;
                        else x++;
                    }
                }
            }
            x = 0;
            for (i = startIndex; i < list.size(); i++) {
                list.get(i).addAction(action, actionIndex);
                if (list.get(i).getDay() == end){
                    if(list.get(i).getDay() == end){
                        if(x == 2)break;
                        else x++;
                    }
                }
            }
        }
        return list;
    }

    public int[] getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(int[] monthYear) {
        this.monthYear = monthYear;
    }

    public void fillContainers(){
        containers.clear();
        //containers.add(new ActionContainer("Test", new ActionDate(26, Calendar.JUNE, 2018), new ActionDate(29, Calendar.JUNE, 2018)));
    }

    public ArrayList<ActionContainer> getContainers() {
        return containers;
    }
}

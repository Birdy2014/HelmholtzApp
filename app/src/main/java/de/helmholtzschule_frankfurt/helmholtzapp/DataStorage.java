package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;

import de.helmholtzschule_frankfurt.helmholtzapp.activity.MainActivity;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.ActionType;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.StundenplanColor;
import de.helmholtzschule_frankfurt.helmholtzapp.item.Action;
import de.helmholtzschule_frankfurt.helmholtzapp.item.CalendarItem;
import de.helmholtzschule_frankfurt.helmholtzapp.item.MensaplanItem;
import de.helmholtzschule_frankfurt.helmholtzapp.item.NewsItem;
import de.helmholtzschule_frankfurt.helmholtzapp.item.StundenplanItem;
import de.helmholtzschule_frankfurt.helmholtzapp.util.ActionContainer;
import de.helmholtzschule_frankfurt.helmholtzapp.util.Benachrichtigungsplan;
import de.helmholtzschule_frankfurt.helmholtzapp.util.Mensaplan;
import de.helmholtzschule_frankfurt.helmholtzapp.util.NewsCollection;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanCell;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanCellTime;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanJsonObject;
import de.helmholtzschule_frankfurt.helmholtzapp.util.TaskCheck;
import de.helmholtzschule_frankfurt.helmholtzapp.util.Vertretungsplan;
import io.github.birdy2014.libhelmholtzdatabase.HelmholtzDatabaseClient;

import static de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload.LEHRERLISTE;
import static de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload.MENSAPLAN;
import static de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload.NEWS;
import static de.helmholtzschule_frankfurt.helmholtzapp.enums.EnumDownload.VERTRETUNGSPLAN;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

public class DataStorage{

    private static final DataStorage ourInstance = new DataStorage();
    private HelmholtzDatabaseClient client = HelmholtzDatabaseClient.getInstance();


    private Vertretungsplan vertretungsplan;
    private Benachrichtigungsplan benachrichtigungsplan;
    private Mensaplan mensaplan;
    private NewsCollection newsCollection;

    //raw JSON Strings
    private String mensaplanRawData;
    private String newsRawData;
    private String vertretungsplanRawData;
    private String benachrichtigungenRawData;
    private String lehrerlisteRawData;
    private String rawMessage;

    private HashMap<String, String> message;
    private String[] lehrerliste;
    private String[] klassen;
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
        String[] postfixes = {"a", "b", "c", "d", "e"};
        ArrayList<String> list = new ArrayList<>();
        for(int i = 5; i < 10; i++){
            for(String s : postfixes){
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

    public void update(Activity activity, EnumDownload... downloads) {
            try {
                SharedPreferences mySPR = activity.getSharedPreferences("MySPFILE", 0);
                ProgressBar bar = activity.findViewById(R.id.progressBar2);
                int stepSize = 100 / downloads.length;
                Resources resources = activity.getResources();
                for(EnumDownload e : downloads){
                    if(e == NEWS){
                        setLoadingInfo("News werden heruntergeladen", activity);
                        newsRawData = download(resources.getString(R.string.hhs_app_news));
                        parseNews();
                    }
                    else if(e == VERTRETUNGSPLAN){
                        setLoadingInfo("Vertretungsplan wird heruntergeladen", activity);
                        vertretungsplanRawData = download(resources.getString(R.string.hhs_app_vertretungsplan) + client.getVertretungsplanCredentials("vertretungsplan")[0] + "&password=" + client.getVertretungsplanCredentials("vertretungsplan")[1]);
                        benachrichtigungenRawData = download(resources.getString(R.string.hhs_app_benachrichtigungen) + client.getVertretungsplanCredentials("vertretungsplan")[0] + "&password=" + client.getVertretungsplanCredentials("vertretungsplan")[1]);
                        if (vertretungsplanRawData.equals("dError")) {
                            vertretungsplanRawData = activity.getResources().getString(R.string.emptyVertretungsplan);
                        }
                        if (benachrichtigungenRawData.equals("dError")) {
                            benachrichtigungenRawData = activity.getResources().getString(R.string.emptyBenachrichtigungen);
                        }
                        parseVertretungsplan();
                    }
                    else if(e == MENSAPLAN){
                        setLoadingInfo("Mensaplan wird heruntergeladen", activity);
                        mensaplanRawData = download(resources.getString(R.string.hhs_app_mensaplan));
                        parseMensaplan();
                    }
                    else if(e == LEHRERLISTE){
                        setLoadingInfo("Lehrerliste wird heruntergeladen", activity);
                        lehrerlisteRawData = download(resources.getString(R.string.hhs_app_lehrerliste));
                        parseLehrerliste();
                    }
                    bar.setProgress(bar.getProgress() + stepSize);
                }
                rawMessage = download(resources.getString(R.string.hhs_app_alert_message));
                parseMessage();

                checkForClass(mySPR);

                setLoadingInfo("Wird konfiguriert", activity);
                int index = activity.getIntent().getIntExtra("fragmentIndex", 0);
                if(downloads.length == EnumDownload.values().length) {
                    loadStundenplan();
                    fillContainers(activity);
                    monthYear = new int[]{Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR)};
                    index = mySPR.getInt("standardTab", 0);
                }
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("fragmentIndex", index);
                intent.putExtra("background", !(new TaskCheck().execute(activity).get()));
                activity.startActivity(intent);
                activity.finish();
            }
            catch (UnknownHostException e){
                System.out.println("Download error. How to fix it?");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void checkForClass(SharedPreferences mySPR) {
        if (!mySPR.getString("klasse", "FAILED").equals("FAILED")) {
            String theClass = mySPR.getString("klasse", "");
            System.out.println("STRING EXISTS, " + theClass);
            if (!theClass.equals(client.getKlasse().toLowerCase())) {
                setPushNotificationsActive(true);
                SharedPreferences.Editor editor = mySPR.edit();
                editor.putString("klasse", client.getKlasse().toLowerCase());
                editor.apply();
                System.out.println("NOT EQUAL!");
            } else {
                System.out.println("STRINGS ARE EQUAL");
            }
        } else {
            System.out.println("STRING DOESNT EXIST");
            setPushNotificationsActive(true);
            SharedPreferences.Editor editor = mySPR.edit();
            editor.putString("klasse", client.getKlasse().toLowerCase());
            editor.apply();
        }
    }

    private void setLoadingInfo(String text, Activity activity){
        setTextViewText(activity, R.id.loadingInfo, text + "...");
    }

    private void setTextViewText(Activity a, int id, String text){
        a.findViewById(id).post(() -> ((TextView)a.findViewById(id)).setText(text));
    }

    public String download(String website) throws IOException {
        URLConnection connection = new URL(website).openConnection();

        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        }
        catch (FileNotFoundException e){
            return "dError";
        }
        Scanner scanner = new Scanner(inputStream);
        String data = "";
        while (scanner.hasNextLine()) {
            data += scanner.nextLine();
        }
        return data;
    }

    private void parseMensaplan() {
        if(!mensaplanRawData.equals("dError")) {
            Gson gson = new Gson();
            mensaplan = gson.fromJson(mensaplanRawData, Mensaplan.class);
        }
        else {

        }
    }

    //Vertretungsplan & Nachrichten
    private void parseVertretungsplan(){
        Gson gson = new Gson();
        vertretungsplan = gson.fromJson(vertretungsplanRawData, Vertretungsplan.class);
        benachrichtigungsplan = gson.fromJson(benachrichtigungenRawData, Benachrichtigungsplan.class);
    }

    private void parseNews(){
        Gson gson = new Gson();
        newsCollection = gson.fromJson(newsRawData, NewsCollection.class);
    }

    private void parseMessage() {
        message = rawMessage.equals("{}") ? null : new Gson().fromJson(rawMessage, HashMap.class);
    }

    public ArrayList<NewsItem> getNews() {
        return newsCollection.getData();
    }

    public Vertretungsplan getVertretungsplan() {
        return vertretungsplan;
    }

    public Benachrichtigungsplan getBenachrichtigungsplan() {
        return benachrichtigungsplan;
    }

    public ArrayList<MensaplanItem> getMensaEssen() {
        return mensaplan.getData();
    }

    public String getMensaWeek() {
        return mensaplan.getMeta().get("calendarWeek");
    }

    public HashMap<String, String> getMessage() {
        return message;
    }

    public String[] getLehrerliste() {
        return lehrerliste;
    }

    public boolean isServerReachable() {
        final boolean[] reachable = new boolean[1];
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://helmholtz-database.lazybird.me");
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                Object objData = urlConnect.getContent();
                reachable[0] = true;
            }
            catch (Exception e) {
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
        if(!lehrerlisteRawData.equals("dError"))lehrerliste = gson.fromJson(lehrerlisteRawData, new TypeToken<String[]>() {}.getType());
        else lehrerliste = new String[]{"Download fehlgeschlagen"};
    }

    private String getKlasse() {
        return client.getKlasse();
    }

    private void setPushNotificationsActive(boolean b) {
        unsubscribeAll();
        System.out.println("UNSUBSCRIBED!");
        if (b) {
            FirebaseMessaging.getInstance().subscribeToTopic("de.HhsFra." + client.getKlasse().toLowerCase());
            System.out.println("SUBSCRIBED TO: " + "de.HhsFra." + client.getKlasse().toLowerCase());
        }
    }

    private void unsubscribeAll() {
        for (String s : klassen) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("de.HhsFra." + s.toLowerCase());
        }
    }

    private StundenplanCellTime getTimeAtHour(int hour){
        if(Character.isDigit(getKlasse().charAt(0))){ //Non - Oberstufe
            return new StundenplanCellTime(unterMittelstufenZeiten[hour][0], unterMittelstufenZeiten[hour][1]);
        }
        return new StundenplanCellTime(oberstufenZeiten[hour][0], oberstufenZeiten[hour][1]);
    }

    private void loadStundenplan() {
        Gson gson = new Gson();
        String JSON = client.readUserData("stundenplan");
        StundenplanJsonObject jsonObject;
        if (JSON.equals("")) {
            ArrayList<StundenplanItem> items = new ArrayList<>();
            for(int i = 0; i < 66; i++){
                items.add(new StundenplanItem(null, null, null, null));
            }
            jsonObject = new StundenplanJsonObject(items, 11);
        }
        else {
            jsonObject = gson.fromJson(JSON, StundenplanJsonObject.class);
        }

        ArrayList<StundenplanItem> list = jsonObject.getItems();
        hours = jsonObject.getHours();

        stundenplan.clear();
        for(int i = 0; i < hours * 6; i++)stundenplan.add(new StundenplanItem(null, null, null, StundenplanColor.WHITE));
        int index = 0;
        for(int i = 0; i < stundenplan.size(); i++){
            if(i % 6 == 0){
                stundenplan.set(i, getTimeAtHour(i / 6));
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

    private void refreshStundenplan() {
        ArrayList<StundenplanCell> list = new ArrayList<>(stundenplan);
        stundenplan.clear();
        for(int i = 0; i < hours * 6; i++)stundenplan.add(new StundenplanItem(null, null, null, StundenplanColor.WHITE));
        for(int i = 0; i < stundenplan.size(); i++){
            if(i % 6 == 0){
                stundenplan.set(i, getTimeAtHour(i / 6));
            }
            else {
                try {
                    stundenplan.set(i, list.get(i));
                }
                catch (IndexOutOfBoundsException ignored){}
            }
        }
    }

    public void saveStundenplan(boolean refresh){
        Thread thread = new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();
                ArrayList<StundenplanItem> list = new ArrayList<>();
                for(StundenplanCell c : stundenplan)if(c instanceof StundenplanItem)list.add(((StundenplanItem)c));
                StundenplanJsonObject jsonObject = new StundenplanJsonObject(list, hours);
                String JSON = gson.toJson(jsonObject);
                client.writeUserData("stundenplan",JSON);
                if(refresh) refreshStundenplan();
            }
        };
        thread.start();
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
            ArrayList<StundenplanItem> list = gson.fromJson(JSON, token.getType());
            stundenplan.clear();
            for(int i = 0; i < hours * 6; i++)stundenplan.add(new StundenplanItem(null, null, null, StundenplanColor.WHITE));
            int index = 0;
            for(int i = 0; i < stundenplan.size(); i++){
                if(i % 6 == 0){
                    try {
                        stundenplan.set(i, getTimeAtHour(i / 6));
                    }
                    catch (IndexOutOfBoundsException ignored){
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
        saveStundenplan(false);
        Toast.makeText(activity.getBaseContext(), "Stundenplan wurde erfolgreich importiert.", Toast.LENGTH_SHORT).show();
        return true;
    }

    private void writeToExternalStorage(String data, String fileName, Activity activity){
        if(Build.VERSION.SDK_INT > 22){
            activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI());
        if(!root.exists())root.mkdir();
        System.out.println(root.getAbsolutePath());
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
        System.out.println(type);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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
                if(i == startIndex)list.get(i).setFirstAt(actionIndex);
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

    private void fillContainers(Context context){
        containers.clear();
        loadCalendar();
        //Put here standards
    }

    public void removeContainer(ActionContainer container){
        for(int i = 0; i < containers.size(); i++){
            if(containers.get(i).toString().equals(container.toString())){
                containers.remove(i);
            }
        }
    }

    public ArrayList<ActionContainer> getContainers() {
        return containers;
    }

    public void saveCalendar(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                Gson gson = new Gson();
                ArrayList<String> calendarStrings = new ArrayList<>();
                for(ActionContainer container : containers){
                    calendarStrings.add(container.toString());
                }
                String JSON = gson.toJson(calendarStrings);

                client.writeUserData("kalender", JSON);
            }
        };
        thread.start();
    }

    private void loadCalendar(){
        Gson gson = new Gson();
        TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>(){};
        String JSON = client.readUserData("kalender");
        ArrayList<String> codes;
        if (JSON.equals("")) {
            codes = new ArrayList<>();
        }
        else codes = gson.fromJson(JSON, token.getType());

        for(String code : codes){
            containers.add(new ActionContainer(code));
        }
    }
}

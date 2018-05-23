package com.ffm.helmholtzschule.helmholtzapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.pdfparser.PDFParser;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab4mensa extends Fragment {
    ArrayList<Mensaplan> gerichte = new ArrayList<Mensaplan>();
    MensaAdapter mensaAdapter;
    ListView lstMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab4mensa, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //ArrayList<Mensaplan> daten = new ArrayList<Mensaplan>();
        //   daten.add(new Mensaplan("Tag", "Fleischgericht", "Vegetarisch", "Nachtisch"));
        // daten.add(new Mensaplan("Montag", "WUAST", "Braucht niemand", "Schoki"));


        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int week = cal.get(Calendar.WEEK_OF_YEAR);
        final int year = cal.get(Calendar.YEAR);
        System.out.println("HDF");
        // File kyle = new File(this.getFilesDir() + "/" + "mensaplan" + week + year + ".pdf");
        //File kyle = new File(getView().getContext().getFilesDir().getAbsolutePath() + "/mensaplan" + week + year + ".pdf");
        //if(!kyle.exists()) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    saveUrl("https://www.liebigmensaservice.de/speiseplan/sppl-asbhelmholtz-" + week + year + ".pdf", week, year);


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Hat net geklappt #1");
                }
                parses(week, year);
            }
        });

        thread.start();







        //Hier daten fütter
        mensaAdapter = new MensaAdapter(getView() .getContext(), gerichte);
        lstMenu = (ListView) getView().findViewById(R.id.mensaMenu);

        lstMenu.setAdapter(mensaAdapter);
        mensaAdapter.notifyDataSetChanged();









        super.onViewCreated(view, savedInstanceState);


        //}
    }



    public void saveUrl(final String urlString, int week2, int year3)
            throws MalformedURLException, IOException {

        String yourFilePath = (getView().getContext().getFilesDir().getAbsolutePath() + "/mensaplan48" + week2 + year3 + ".pdf");

        File yourFile = new File( yourFilePath );

        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            //  fout = openFileOutput(new File(this.getFilesDir() + "/mensaplan262017").getAbsolutePath().toString(), Context.MODE_PRIVATE);
            fout =  new FileOutputStream (new File(yourFile.getAbsolutePath().toString()), true); // true will be same as Context.MODE_APPEND

            System.out.println("KAKALAKAK");
            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }

    }
    public void parses(int week4, int year5) {
        String mofleisch = "";
        String difleisch = "";
        String mifleisch = "";
        String dofleisch = "";
        String frfleisch = "";

        String moveg = "";
        String diveg = "";
        String miveg = "";
        String doveg = "";
        String frveg = "";

        String modes = "";
        String dides = "";
        String mides = "";
        String dodes = "";
        String frdes = "";

        FileInputStream kas = null;
        try {
            kas = new FileInputStream(getView().getContext().getFilesDir().getAbsolutePath() + "/mensaplan48" + week4 + year5 + ".pdf");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedInputStream kaka = new BufferedInputStream(kas);

        PDFBoxResourceLoader.init(getView().getContext());
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        // /storage/emulated/0/Download/9NGQHF.pdf //"C:/MensaPlan/Test.pdf"
        // "storage/emulated/0/Downloads/Test.pdf" //Environment.DIRECTORY_DOWNLOADS + "/Test.pdf"
        //"/HelmholtzApp/Mensaplan/.mensaplan262017.pdf"
        //File storageDirectory2 = Environment.getExternalStorageDirectory();
        //String storageDirectoryPath2 = storageDirectory2.getPath();
        // File file = new File(storageDirectoryPath2 + "/drawable/.mensaplan"+ week + year +".pdf");

        // new File();
        //RandomAccessFile file2 = new RandomAccessFile("C:/MensaPlan/Test.pdf","r");
        try{

            PDFParser parser = new PDFParser(kaka);
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(1);
            String parsedText = pdfStripper.getText(pdDoc);
            Log.d("pdfout",parsedText);

            String[] splitanf = parsedText.split("Montag");
            //          String[] splitmovor = splitanf[1].split("Dienstag");
            String[] splitmo = splitanf[1].split("Dienstag");

            String[] splitdi = splitmo[1].split("Mittwoch");
            String[] splitmi = splitdi[1].split("Donnerstag");
            String[] splitdo = splitmi[1].split("Freitag");
            String[] splitfr = splitdo[1].split("An");

            Log.d("pdfout deine mutta", splitanf[1]);

            Log.d("pdfout ab Montag", splitmo[0]);
            // String[] splitdo = splitmo[0];
            Log.d("pdfout ab Dienstag", splitdi[0]);
            Log.d("pdfout ab Mittwoch", splitmi[0]);
            Log.d("pdfout ab Donnerstag", splitdo[0]);
            Log.d("pdfout ab Freitag", splitfr[0]);
            for(int i = 0; i < splitdi.length; i++){
                System.out.println("Splitdi[" + i + "] " + splitdi[i]);
            }
            //String[] splitmot   = splitmo[0].split("Montag");

            String[] splitmoFVD = splitmo[0].split("\n");
            String[] splitdiFVD = splitdi[0].split("\n");
            String[] splitmiFVD = splitmi[0].split("\n");
            String[] splitdoFVD = splitdo[0].split("\n");
            String[] splitfrFVD = splitfr[0].split("\n");

            for(int i = 0; i < splitdiFVD.length; i++){
                System.out.println("SplitdiFVD[" + i + "] " + splitdiFVD[i]);
            }

            Log.d("pdfout ab MontagFVD", splitmoFVD[7]);
            // String[] splitdo = splitmo[0];
            Log.d("pdfout ab DienstagFVD", splitdiFVD[1]);
            Log.d("pdfout ab MittwochFVD", splitmiFVD[1]);
            Log.d("pdfout ab DonnerstagFVD", splitdoFVD[1]);
            Log.d("pdfout ab FreitagFVD", splitfrFVD[0]);


            //  Log.d("pdfout nur mo", splitmot[1]);

            int moFVDl = splitmoFVD.length;
            int diFVDl = splitdiFVD.length;
            int miFVDl = splitmiFVD.length;
            int doFVDl = splitdoFVD.length;
            int frFVDl = splitfrFVD.length;

            int anzmo = 0;
            int anzdi = 0;
            int anzmi = 0;
            int anzdo = 0;
            int anzfr = 0;
            String textmo = "";
            String textdi = "";
            String textmi = "";
            String textdo = "";
            String textfr = "";
            for(int i = 0; i < moFVDl; i++){
                if(leer(splitmoFVD[i]) == false){
                    anzmo++;

                    textmo = textmo + splitmoFVD[i] + "\n" ;

                }

            }
            if(anzmo > 3) {
                Log.d("TEXT AM MONTAG", textmo);
                String[] monsplit = textmo.split("Dessert: ");
                String monfleisch = monsplit[0];
                String monnach = monsplit[1];

                Log.d("monnach", monnach);
                Log.d("FLEISCH AM MONTA", monfleisch);

                String[] monvesch = monnach.split("\n");
                Log.d("monvesch", monvesch.length + "");
                Log.d("monvesch1", monvesch[0]);
                String monveg = "";
                for (int i = 1; i < monvesch.length; i++) {
                    monveg = monveg + monvesch[i] + "\n";
                }
                Log.d("monvesch", monveg);
                Log.d("MonDessert", monvesch[0]);
                modes = monvesch[0];
            }

            for(int i = 0; i < diFVDl; i++) {
                System.out.println("diFVDl: " + diFVDl);
                if (leer(splitdiFVD[i]) == false) {
                    anzdi++;

                    textdi = textdi + splitdiFVD[i] + "\n";
                    //   }
                }
            }
            if(anzdi > 3) {
                Log.d("ANZDI", anzdi + "");
                Log.d("TEXT AM DI", textdi);
                String[] displit = textdi.split("Dessert: ");
                System.out.println("Textdi: " + textdi);
                difleisch = displit[0];
                String dinach = displit[1];
                Log.d("dinach", dinach);
                Log.d("FLEISCH AM di", difleisch);
                String[] divesch = dinach.split("\n");

                diveg = "";
                for (int i = 1; i < divesch.length; i++) {
                    diveg = diveg + divesch[i] + "\n";
                }
                Log.d("divesch", diveg);
                Log.d("diDessert", divesch[0]);
                dides = divesch[0];
            }
            for(int i = 0; i < miFVDl; i++){
                if(leer(splitmiFVD[i]) == false) {
                    anzmi++;
                    if (i != 0) {
                        textmi = textmi + "\n" + splitmiFVD[i];
                    }
                }    }
            if(anzmi > 3) {
                Log.d("TEXT AM MiTAG", textmi);
                String[] misplit = textmi.split("Dessert: ");
                mifleisch = misplit[0];
                String minach = misplit[1];
                Log.d("minach", minach);
                Log.d("FLEISCH AM MI", mifleisch);
                String[] mivesch = minach.split("\n");
                miveg = "";
                for (int i = 1; i < mivesch.length; i++) {
                    miveg = miveg + mivesch[i] + "\n";
                }
                Log.d("mivesch", miveg);
                Log.d("MiDessert", mivesch[0]);
                mides = mivesch[0];
            }
            for(int i = 0; i < doFVDl; i++) {
                if (leer(splitdoFVD[i]) == false) {
                    anzdo++;
                    if (i != 0) {
                        textdo = textdo + "\n" + splitdoFVD[i];
                    }
                }
            }
            if(anzdo > 3) {
                Log.d("TEXT AM DTAG", textdo);
                String[] dosplit = textdo.split("Dessert: ");
                dofleisch = dosplit[0];
                String donach = dosplit[1];
                Log.d("donach", donach);
                Log.d("FLEISCH AM d", dofleisch);
                String[] dovesch = donach.split("\n");
                doveg = "";
                for (int i = 1; i < dovesch.length; i++) {
                    doveg = doveg + dovesch[i] + "\n";
                }
                Log.d("dovesch", doveg);
                Log.d("doDessert", dovesch[0]);
                dodes = dovesch[0];
            }
            for(int i = 0; i < frFVDl; i++) {
                if (leer(splitfrFVD[i]) == false) {
                    anzfr++;
                    if (i != 0) {
                        textfr = textfr + "\n" + splitfrFVD[i];
                    }
                }
            }
            if(anzfr > 3) {
                Log.d("TEXT AM FR", textfr);
                String[] frsplit = textfr.split("Dessert: ");
                frfleisch = frsplit[0];
                String frnach = frsplit[1];
                Log.d("frnach", frnach);
                Log.d("FLEISCH AM fr", frfleisch);
                String[] frvesch = frnach.split("\n");
                frveg = "";
                for (int i = 1; i < frvesch.length; i++) {
                    frveg = frveg + frvesch[i] + "\n";
                }
                Log.d("frvesch", frveg);
                Log.d("frDessert", frvesch[0]);
                frdes = frvesch[0];
            }

            Log.d("anzmo", anzmo + "");
            Log.d("anzdi", anzdi + "");
            Log.d("anzmi", anzmi + "");
            Log.d("anzdo", anzdo + "");
            Log.d("anzfr", anzfr + "");

            Log.d("pdfout ab Montaglength", moFVDl + "");
            // String[] splitdo = splitmo[0];
            Log.d("pdfout ab Dienstaglengt",  diFVDl + "");
            Log.d("pdfout ab Mittwochlengt",  miFVDl + "");
            Log.d("pdfout ab Donnerstagle",  doFVDl + "");
            Log.d("pdfout ab Freitaglength",  frFVDl + "");

           /* int moF = ((moFVDl-2) / 2) - 1;
            int diF = ((diFVDl-4) / 2) - 1;
            int miF = ((miFVDl-1) / 2) - 1;
            int doF = ((doFVDl-1) / 2) - 1;
            int frF = ((frFVDl-1) / 2) - 1;

            System.out.println(moF);
            System.out.println(diF);

            String fleischmo = "";
            String vegmo = "";
            String desmo = "";

            String fleischdi = "";
            String vegdi = "";
            String desdi = "";

            String fleischmi = "";
            String vegmi = "";
            String desmi = "";

            String fleischdo = "";
            String vegdo = "";
            String desdo = "";

            String fleischfr = "";
            String vegfr = "";
            String desfr = "";

            Log.d("pdfout wie lang FDVl", moFVDl + "");
            Log.d("pdfout MO F", splitmoFVD[moF]);

            //Montag
            for(int i = 0; i < moF + 2; i++){
                fleischmo = (fleischmo + splitmoFVD[i+2] + "\n");
            }
            System.out.println(fleischmo);
            Log.d("Fleischmontag" , fleischmo);
            for(int i = moF + 3; i < moF *2 +3 ;i++ ){
                vegmo = (vegmo + splitmoFVD[i] + "\n");
            }
            System.out.println(vegmo);
            Log.d("Veg Montag", vegmo);
            System.out.println(splitmo);

            //desmo = desmo + splitmo[moF];

            //System.out.println(desmo);
            //Log.d("Dessert Montag", desmo);

            //Dienstag
            for(int i = 2; i < diF + 2; i++){
                fleischdi = (fleischdi + splitdiFVD[i] + "\n");
            }
            System.out.println(fleischdi);
            Log.d("Fleisch Dienstag", fleischdi);

            for(int i = diF + 5; i < diF *2 +5 ;i++ ){
                vegdi = (vegdi + splitdiFVD[i] + "\n");
            }
            System.out.println(vegdi);
            Log.d("Veg Diensatag", vegdi);

            //Mittwoch
            for(int i = 2; i < miF + 3; i++){
                fleischmi = (fleischmi + splitmiFVD[i] + "\n");
            }
            System.out.println(fleischmi);
            Log.d("Fleisch Mittwoch", fleischmi);

            for(int i = miF + 2; i < miF *2 +2 ;i++ ){
                vegmi = (vegmi + splitmiFVD[i] + "\n");
            }
            System.out.println(vegmi);
            Log.d("Veg Mittwoch", vegmi );

            //Donnerstag
            for(int i = 3; i < doF + 2; i++){
                fleischdo = (fleischdo + splitdoFVD[i] + "\n");
            }
            System.out.println(fleischdo);
            Log.d("Fleisch Donnerstag", fleischdo);

            for(int i = doF + 2; i < doF *2 +2 ;i++ ){
                vegdo = (vegdo + splitdoFVD[i] + "\n");
            }
            System.out.println(vegdo);
            Log.d("Veg Donnerstag", vegdo);

            //Freitag funktioniert wegen ferien net richtig kann das net einstellen und das mit der true/false
            // wenn die zeile leer ist hab ich nicht hinbekommen weil der au einem mir nicht verständlichen grund
            //mir jedes mal wenn ichs probiert hab ne dauerschleife geben hat die nicht hätte da sein sollen
            //oder er ich hab nen fehler code bekommen (sry)
            for(int i = 0; i < frF + 1; i++){
                fleischfr = (fleischfr + splitfrFVD[i] + "\n");
            }
            Log.d("Fleisch Freitag", fleischfr);

            for(int i = frF + 2; i < frF *2 +2 ;i++ ){
                vegfr = (vegfr + splitfr[i] + "\n");
            }
            Log.d("veg Freitag", vegfr);
*/
            pdDoc.close();


        }
        catch(java.io.IOException e){
            e.printStackTrace();
        }
        gerichte.add(new Mensaplan("Montag", mofleisch,  moveg,  modes));
        gerichte.add(new Mensaplan("Dienstag", difleisch,  diveg,  dides));
        gerichte.add(new Mensaplan("Mittwoch", mifleisch,  miveg,  mides));
        gerichte.add(new Mensaplan("Donnerstag", dofleisch,  doveg,  dodes));
        gerichte.add(new Mensaplan("Freitag", frfleisch,  frveg,  frdes));

    }
    public static boolean leer(String zeile){
        String moin = "";
        for(int i = 0; i < 70; i++) {
            for(int j = 0; j < i; j++) {
                moin = moin + " ";
            }
            if(zeile.equals(moin)) {
                return true;
            }
            moin = "";
        }
        return false;
    }













}

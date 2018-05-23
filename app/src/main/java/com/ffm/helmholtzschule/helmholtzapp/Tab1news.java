package com.ffm.helmholtzschule.helmholtzapp;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * Created by Staudinger on 28.06.2017.
 */

public class Tab1news extends Fragment {
    public ListView lstMenu;
    public ArrayList<News> daten;
    public NewsAdapter menuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1news, container, false);
        return rootView;

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        daten = new ArrayList<News>();

        menuAdapter = new NewsAdapter(this.getContext(), daten);
        lstMenu = (ListView) getView().findViewById(R.id.lstNews);
        lstMenu.setAdapter(menuAdapter);


        Ion.with(this.getContext()).load("http://www.helmholtzschule-frankfurt.de").asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                int g=0;
                int o=0;
                int gf=0;
                String news="";
                String h="";
                String html=result;
                /*BufferedReader br = new BufferedReader(new FileReader("F:/a/a.txt"));
                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    html = sb.toString();
                } finally {
                    br.close();
                }*/






                int z=0;
                gf=html.indexOf("data-history-node-id=\"");
                gf=gf+22;
                news="http://www.helmholtzschule-frankfurt.de/node/"+html.charAt(gf)+html.charAt(gf+1)+html.charAt(gf+2);
                gf=html.indexOf("field--label-hidden\">");

                gf=gf+21;
                z=gf;

                while(!(html.charAt(z)+"").equals("<")){
                    h=h+""+html.charAt(z);
                    z++;
                }

                o=z+5;

                System.out.println(h+" "+news);
                daten.add(new News(h,news));

                while(new Integer(html.indexOf("string field--label-hidden\">",o))!=-1){
                    h="";

                    gf=html.indexOf("data-history-node-id=\"",o);
                    gf=gf+22;

                    news="http://www.helmholtzschule-frankfurt.de/node/"+html.charAt(gf)+html.charAt(gf+1)+html.charAt(gf+2);
                    gf=html.indexOf("string field--label-hidden\">",o);

                    gf=gf+28;
                    z=gf;

                    while(!(html.charAt(z)+"").equals("<")){
                        h=h+""+html.charAt(z);
                        z++;
                        o=z;
                    }
                    o=z;
                    for(int f=0;f<=500;f++){
                        if(h.indexOf("&quot;")==f){
                            h=korrektur(h);
                        }
                    }
                    System.out.println(h+" "+news);
                    daten.add(new News(h,news));
                }
                menuAdapter.notifyDataSetChanged();


            }
        });


        lstMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        News akt = (News) parent.getItemAtPosition(position);
                        String url = akt.getUrl();


                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }



        );



        super.onViewCreated(view, savedInstanceState);

    }

    public static String korrektur(String h){
        boolean kokos=false;
        int k=0;
        int p=0;
        String l="";
        int u=0;
        while(h.indexOf("&quot;",p)>0){

            k=h.indexOf("&quot",p);

            if(p==0){
                for(int j=0;j<k;j++){
                    l=l+h.charAt(j);

                }
            }

            p=k+7;
            int a=k;

            k=h.indexOf("&quot",p);
            u=k;

            if(p!=0){
                for(a=a+6;a<u;a++){

                    l=l+h.charAt(a);
                    if(a>=h.length()){
                        kokos=true;
                        break;

                    }

                }
            }
            if(kokos==true){
                break;
            }
        }


        return l;
    }

}

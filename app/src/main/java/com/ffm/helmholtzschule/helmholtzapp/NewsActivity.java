package com.ffm.helmholtzschule.helmholtzapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    public ListView lstMenu;
    public ArrayList<News> daten;
    public NewsAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        daten = new ArrayList<News>();

        menuAdapter = new NewsAdapter(this, daten);
        lstMenu = (ListView) findViewById(R.id.lstNews);
        lstMenu.setAdapter(menuAdapter);


        Ion.with(getApplicationContext()).load("http://www.helmholtzschule-frankfurt.de").asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                int g=0;
                int o=0;
                int gf=0;
                String url="";
                String titel="";



                int z=0;
                gf=result.indexOf("data-history-node-id=\"");
                gf=gf+22;
                url="http://www.helmholtzschule-frankfurt.de/node/"+result.charAt(gf)+result.charAt(gf+1)+result.charAt(gf+2);
                gf=result.indexOf("field--label-hidden\">");

                gf=gf+21;
                z=gf;

                while(!(result.charAt(z)+"").equals("<")){
                    titel=titel+""+result.charAt(z);
                    z++;
                }

                o=z;

                System.out.println(titel+" "+url);
                daten.add(new News(titel,url));

                while(new Integer(result.indexOf("data-history-node-id=\"",o))!=-1){
                    titel="";
                    z=0;
                    gf=result.indexOf("data-history-node-id=\"",o);
                    gf=gf+22;
                    url="http://www.helmholtzschule-frankfurt.de/node/"+result.charAt(gf)+result.charAt(gf+1)+result.charAt(gf+2);
                    gf=result.indexOf("field--label-hidden\">",o);

                    gf=gf+21;
                    z=gf;

                    while(!(result.charAt(z)+"").equals("<")){
                        titel=titel+""+result.charAt(z);

                        z++;
                        o=z;
                    }
                    System.out.println(titel+" "+url);
                    daten.add(new News(titel,url));
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
                        System.out.println(url);
                    }
                }

        );

    }
}

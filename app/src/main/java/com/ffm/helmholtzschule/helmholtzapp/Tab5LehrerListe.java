package com.ffm.helmholtzschule.helmholtzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import io.github.birdy2014.VertretungsplanLib.Vertretung;


public class Tab5LehrerListe extends Fragment {

    String[] list = {"[Ave] Anna-Christine Avellini",
            "[Bar] Faris Barakat",
            "[Bmg] Christina Baumung",
            "[Bec] Hannah Angelika Becker",
            "[Böt] Dr. Frauke Böttcher",
            "[Buc] Thilo Buchmaier",
            "[Chi] Laura Chihab",
            "[Czi] Eva-Maria Czischek",
            "[Czw] Nadine Czwalinna",
            "[Dar] Asmaa Darraz",
            "[Dk] Claudia Delkurt",
            "[Dl] Walter Dill",
            "[Es] Melanie Escher",
            "[Fab] Elke Fabiunke",
            "[Fal] Johannes Falter",
            "[Gra] Steffen Grande",
            "[Hal] Sophie Halwas",
            "[Hau] Sarina Hau",
            "[HaF] Fatima Hauck",
            "[Hk] Norbert Heck",
            "[Hei] Petra Heimann",
            "[Hm] Cornelia Herrmann",
            "[Hrm] Kai Herrmann",
            "[Hd] Christoph Heyd",
            "[Hf] Nina Höfer",
            "[Höf] Stella Höfle",
            "[Hö] Steffen Höhr",
            "[Hum] Annette Hummel",
            "[Jö] Bernhard Jöst",
            "[Jg] Bianca Jung",
            "[Jün] Matthias Jünger",
            "[Ka] Katja Kaleja-Kraft",
            "[Kb] Waltraud Kallenbach",
            "[Ker] Dr. Josef Kertes",
            "[Ktz] Julia Kitzinger",
            "[Kno] Meike Knochenhauer",
            "[Knp] Jana Knopp",
            "[Knt] Oliver Knothe",
            "[Kg] Dr. Alexander König",
            "[Krf] Thilo Kraft",
            "[Lut] Melitta Luta",
            "[Mar] Angelika Martin",
            "[Mt] Christoph Martino",
            "[May] Christine May-Schmachtenberg",
            "[Mz] Sigrid Merz",
            "[Mi] Alexis Max Michael",
            "[MP] Brigitta Mosley-Pötzl",
            "[Mün] Thomas Münch",
            "[Mg] Bernd Müßig",
            "[Pau] Dr. Marion Pausch",
            "[Pt] Jessica Peters",
            "[Ra] Gunild Rachow",
            "[Rm] Martina Rassmann",
            "[Re] Johanna Rehner-Uhlig",
            "[Ros] Dr. Christina Rosseaux",
            "[Sac] Paul Sach",
            "[Sst] Heide Schimmelschmidt",
            "[Smd] Valerie Schmidt",
            "[Sbw] Imke Schoberwalter",
            "[Sof] Bastian Schoof",
            "[Sh] Ralf Schuh",
            "[Sz] Antje Schulze",
            "[Sw] Susanne Schwartze",
            "[Se] Maren Seel",
            "[Std] Jan Staudinger",
            "[Stö] Christa Stöbbe",
            "[To] Karim Touati",
            "[Tri] Marietta Trittel",
            "[Ulm] Gerrit Ulmke",
            "[Wb] Lena Weber"};

    private ListView lehrerlistView;
    private ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab5_lehrerliste, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lehrerlistView = (ListView) getView().findViewById(R.id.lehrer_list_view);
        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);

        lehrerlistView.setAdapter(adapter);
    }
}

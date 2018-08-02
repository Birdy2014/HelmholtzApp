package de.helmholtzschule_frankfurt.helmholtzapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ColorFieldAdapter extends ArrayAdapter<StundenplanColor>{

    private StundenplanItem item;
    private Dialog dialog;
    private ArrayAdapter adapter;

    public ColorFieldAdapter(Context context, ArrayList<StundenplanColor> list, StundenplanItem item, Dialog dialog, ArrayAdapter adapter) {
        super(context, R.layout.color_chooser_cell, list);
        this.item = item;
        this.dialog = dialog;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.color_chooser_cell, parent, false);

        Button button = customView.findViewById(R.id.colorButton);

        if(getItem(position) != StundenplanColor.ADD) {
            StundenplanColor color = getItem(position);
            button.setBackgroundColor(color.getCode());
            button.setOnClickListener(click -> {
                item.setColor(color);
                adapter.notifyDataSetChanged();
                DataStorage.getInstance().saveStundenplan(false);
                dialog.cancel();
            });
        }
        else {
            button.setBackgroundResource(R.drawable.ic_add);
            button.setOnClickListener(click -> {
                View pickerView = inflater.inflate(R.layout.color_picker, null);
                Dialog pickerDialog = new Dialog(getContext());
                pickerDialog.addContentView(pickerView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                SeekBar[] bars = new SeekBar[3];
                bars[0] = pickerView.findViewById(R.id.sliderRed);
                bars[1] = pickerView.findViewById(R.id.sliderGreen);
                bars[2] = pickerView.findViewById(R.id.sliderBlue);
                Button colorApply = pickerView.findViewById(R.id.buttonColorApply);
                colorApply.setOnClickListener(click1 -> {
                    TextView HEXView = pickerView.findViewById(R.id.HEXField);
                    String hex = HEXView.getText().toString();
                    item.setColor(Color.parseColor(hex));
                    adapter.notifyDataSetChanged();
                    DataStorage.getInstance().saveStundenplan(false);
                    pickerDialog.cancel();
                    dialog.cancel();
                });
                for(SeekBar bar : bars)bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        changeColorValues(pickerView, bars);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                pickerDialog.show();
            });
        }

        return customView;
    }
    @SuppressLint("Range")
    private void changeColorValues(View view, SeekBar[] bars){
        TextView RGBView = view.findViewById(R.id.RGBField);
        TextView HEXView = view.findViewById(R.id.HEXField);
        View preview = view.findViewById(R.id.previewField);
        StringBuilder hex = new StringBuilder();
        for(SeekBar bar : bars){
            String part = Integer.toHexString(bar.getProgress()).toUpperCase();
            if(part.length() == 1)part = 0 + part;
            hex.append(part);
        }
        preview.setBackgroundColor(Color.parseColor("#" + hex));
        StringBuilder rgbText = new StringBuilder("RGB: ");
        for(SeekBar bar : bars){
            String part = String.valueOf(bar.getProgress());
            rgbText.append(part).append(" ");
        }
        RGBView.setText(rgbText.toString());
        HEXView.setText("#" + hex);
    }
}

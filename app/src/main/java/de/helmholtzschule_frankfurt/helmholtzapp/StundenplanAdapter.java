package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.method.KeyListener;
import android.transition.AutoTransition;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class StundenplanAdapter extends ArrayAdapter<StundenplanCell>{

    private View popupView;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;

    public StundenplanAdapter(Context context, ArrayList<StundenplanCell> list) {
        super(context, R.layout.stundenplan_cell, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Menu2Inflater = LayoutInflater.from(getContext());
        View customView = Menu2Inflater.inflate(R.layout.stundenplan_cell, parent, false);


        String name = getItem(position).getName();
        TextView nameView = (TextView)customView.findViewById(R.id.stundenplanCellName);
        if(getItem(position) instanceof StundenplanItem){
            nameView.setTextColor(((StundenplanItem)getItem(position)).getTextColor());
            nameView.setBackgroundColor(((StundenplanItem)getItem(position)).getColor());
        }
        if(!(getItem(position) instanceof StundenplanCellTime || getItem(position) instanceof StundenplanItem)){
            nameView.setTextSize(15);
        }
        nameView.setText(name);
        customView.setOnClickListener(view -> {
            if(getItem(position) instanceof StundenplanCellTime){

            }
            else if(getItem(position) instanceof StundenplanItem){
                showPopup(getItem(position));
            }
        });
        return customView;
    }
    public void showPopup(StundenplanCell item){
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.stundenplan_popup, null);
        /*popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(100);
        popupWindow.showAsDropDown(popupView, 0, 0);*/
        Dialog dialog = new Dialog(getContext());
        dialog.addContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        EditText lessonName = popupView.findViewById(R.id.popupTextFach);
        lessonName.setText(item.getName());
        setEditable(lessonName, false);

        EditText teacher = popupView.findViewById(R.id.popupTextLehrer);
        teacher.setText(((StundenplanItem)item).getLehrer());
        setEditable(teacher, false);

        EditText room = popupView.findViewById(R.id.popupTextRaum);
        room.setText(((StundenplanItem)item).getRaum());
        setEditable(room, false);

        ImageButton editButton = popupView.findViewById(R.id.popupButtonEdit);
        ImageButton applyButton = popupView.findViewById(R.id.popupButtonApply);

        editButton.setOnClickListener(click -> {
            applyButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.INVISIBLE);

            setEditable(lessonName, true);
            setEditable(teacher, true);
            setEditable(room, true);
        });
        applyButton.setOnClickListener(click -> {
            applyButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.VISIBLE);

            setEditable(lessonName, false);
            setEditable(teacher, false);
            setEditable(room, false);

            item.setName(lessonName.getText().toString());
            ((StundenplanItem)item).setLehrer(teacher.getText().toString());
            ((StundenplanItem)item).setRaum(room.getText().toString());

            DataStorage.getInstance().saveStundenplan(getContext());

            dialog.cancel();
        });
    }
    private static void setEditable(EditText text, boolean b){
        if(!b){
            text.setTag(text.getKeyListener());
            text.setKeyListener(null);
            text.clearFocus();
        }
        else {
            text.setKeyListener((KeyListener) text.getTag());
        }
    }
}

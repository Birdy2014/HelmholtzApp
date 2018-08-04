package de.helmholtzschule_frankfurt.helmholtzapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class StundenplanAdapter extends ArrayAdapter<StundenplanCell>{

    private View popupView;
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
        TextView nameView = customView.findViewById(R.id.stundenplanCellName);
        if(getItem(position) instanceof StundenplanItem){
            nameView.setTextColor(((StundenplanItem)getItem(position)).getTextColor());
            nameView.setBackgroundColor(((StundenplanItem)getItem(position)).getColor());
        }
        else{
            nameView.setBackgroundColor(0xFFFFFFFF);
            nameView.setTextColor(0xFF000000);
        }
        if(!(getItem(position) instanceof StundenplanCellTime || getItem(position) instanceof StundenplanItem)){
            nameView.setTextSize(15);
        }
        nameView.setText(name);
        customView.setOnLongClickListener(view -> {
            inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = inflater.inflate(R.layout.stundenplan_action_box, null);
            Dialog actionDialog = new Dialog(getContext());
            actionDialog.setContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            /*Window dialogWindow = actionDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.START | Gravity.TOP);

            System.out.println("X: " + customView.getX());
            lp.x = (int)customView.getX();*/

            Button copy = popupView.findViewById(R.id.copyButton);
            Button put = popupView.findViewById(R.id.pasteButton);
            copy.setOnClickListener(click -> {
                DataStorage.getInstance().setCopiedItem((StundenplanItem)getItem(position));
                this.notifyDataSetChanged();
                DataStorage.getInstance().saveStundenplan(false);
                actionDialog.cancel();
            });
            put.setOnClickListener(click -> {
                ((StundenplanItem)getItem(position)).overwrite(DataStorage.getInstance().getCopiedItem());
                this.notifyDataSetChanged();
                DataStorage.getInstance().saveStundenplan(false);
                actionDialog.cancel();
            });
            put.setVisibility(DataStorage.getInstance().getCopiedItem() == null ? View.GONE : View.VISIBLE);
            actionDialog.show();
            return true;
        });
        customView.setOnClickListener(view -> {
            if(getItem(position) instanceof StundenplanCellTime){

            }
            else if(getItem(position) instanceof StundenplanItem){
                showPopup(getItem(position), nameView);
            }
        });
        return customView;
    }

    public void showPopup(StundenplanCell item, TextView view){
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.stundenplan_popup, null);
        Dialog dialog = new Dialog(getContext()/*, R.style.full_screen_dialog*/);
        dialog.addContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();

        EditText lessonName = popupView.findViewById(R.id.popupTextFach);
        lessonName.setText(item.getName());
        setEditable(lessonName, false, getContext());

        EditText teacher = popupView.findViewById(R.id.popupTextLehrer);
        teacher.setText(((StundenplanItem)item).getLehrer());
        setEditable(teacher, false, getContext());

        EditText room = popupView.findViewById(R.id.popupTextRaum);
        room.setText(((StundenplanItem)item).getRaum());
        setEditable(room, false, getContext());

        ImageButton editButton = popupView.findViewById(R.id.popupButtonEdit);
        ImageButton applyButton = popupView.findViewById(R.id.popupButtonApply);
        ImageButton colorButton = popupView.findViewById(R.id.popupButtonColor);
        ImageButton resetButton = popupView.findViewById(R.id.popupButtonReset);

        editButton.setOnClickListener(click -> {
            applyButton.setVisibility(View.VISIBLE);
            colorButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.INVISIBLE);

            setEditable(lessonName, true, getContext());
            setEditable(teacher, true, getContext());
            setEditable(room, true, getContext());
        });
        applyButton.setOnClickListener(click -> {
            applyButton.setVisibility(View.INVISIBLE);
            colorButton.setVisibility(View.INVISIBLE);
            resetButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.VISIBLE);

            setEditable(lessonName, false, getContext());
            setEditable(teacher, false, getContext());
            setEditable(room, false, getContext());

            item.setName(lessonName.getText().toString());
            ((StundenplanItem)item).setLehrer(teacher.getText().toString());
            ((StundenplanItem)item).setRaum(room.getText().toString());

            this.notifyDataSetChanged();

            DataStorage.getInstance().saveStundenplan(false);
        });
        colorButton.setOnClickListener(click ->{

            inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = inflater.inflate(R.layout.color_chooser, null);
            Dialog colorDialog = new Dialog(getContext());
            colorDialog.addContentView(popupView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ColorFieldAdapter adapter = new ColorFieldAdapter(getContext(), new ArrayList<>(Arrays.asList(StundenplanColor.values())), (StundenplanItem)item, colorDialog, this);
            GridView gridView = popupView.findViewById(R.id.colorGrid);
            gridView.setAdapter(adapter);
            colorDialog.show();
        });
        resetButton.setOnClickListener(click -> {
            item.setName("");
            lessonName.setText("");
            teacher.setText("");
            room.setText("");
            ((StundenplanItem) item).setColor(StundenplanColor.WHITE);
            this.notifyDataSetChanged();
            DataStorage.getInstance().saveStundenplan(false);
        });
        if(lessonName.getText().toString().equals("") && teacher.getText().toString().equals("") && room.getText().toString().equals(""))editButton.performClick();
    }
    private static void setEditable(EditText text, boolean b, Context c){
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

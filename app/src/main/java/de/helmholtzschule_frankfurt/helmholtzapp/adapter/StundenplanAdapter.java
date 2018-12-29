package de.helmholtzschule_frankfurt.helmholtzapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;
import de.helmholtzschule_frankfurt.helmholtzapp.R;
import de.helmholtzschule_frankfurt.helmholtzapp.enums.StundenplanColor;
import de.helmholtzschule_frankfurt.helmholtzapp.item.StundenplanItem;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanCell;
import de.helmholtzschule_frankfurt.helmholtzapp.util.StundenplanCellTime;

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
            if(getItem(position) instanceof StundenplanItem) {
                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = inflater.inflate(R.layout.stundenplan_action_box, null);
                Dialog actionDialog = new Dialog(getContext());
                actionDialog.setContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                Button copy = popupView.findViewById(R.id.copyButton);
                Button put = popupView.findViewById(R.id.pasteButton);
                copy.setOnClickListener(click -> {
                    DataStorage.getInstance().setCopiedItem((StundenplanItem) getItem(position));
                    this.notifyDataSetChanged();
                    DataStorage.getInstance().saveStundenplan(false);
                    actionDialog.cancel();
                });
                put.setOnClickListener(click -> {
                    ((StundenplanItem) getItem(position)).overwrite(DataStorage.getInstance().getCopiedItem());
                    this.notifyDataSetChanged();
                    DataStorage.getInstance().saveStundenplan(false);
                    actionDialog.cancel();
                });
                put.setVisibility(DataStorage.getInstance().getCopiedItem() == null ? View.GONE : View.VISIBLE);
                actionDialog.show();
                return true;
            }
            return false;
        });
        customView.setOnClickListener(view -> {
            if(getItem(position) instanceof StundenplanItem){
                showPopup(getItem(position));
            }
        });
        System.out.println(customView.getLayoutParams().width);
        customView.requestLayout();
        return customView;
    }

    private void showPopup(StundenplanCell item){
        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.stundenplan_popup, null);
        Dialog dialog = new Dialog(getContext()/*, R.style.full_screen_dialog*/);
        dialog.addContentView(popupView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        EditText lessonName = popupView.findViewById(R.id.popupTextFach);
        lessonName.setText(item.getName());
        setEditable(lessonName, false, getContext());

        EditText teacher = popupView.findViewById(R.id.popupTextLehrer);
        teacher.setText(((StundenplanItem)item).getLehrer());
        setEditable(teacher, false, getContext());

        EditText room = popupView.findViewById(R.id.popupTextRaum);
        room.setText(((StundenplanItem)item).getRaum());
        setEditable(room, false, getContext());

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                lessonName.clearFocus();
                teacher.clearFocus();
                room.clearFocus();
            }
        });

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

            if(lessonName.isFocused())lessonName.clearFocus();
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
            colorDialog.setOnDismissListener(dialogInterface -> {
                setEditable(lessonName, true, getContext());
                setEditable(teacher, true, getContext());
                setEditable(room, true, getContext());
            });

            setEditable(lessonName, false, getContext());
            setEditable(teacher, false, getContext());
            setEditable(room, false, getContext());

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
        dialog.show();
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

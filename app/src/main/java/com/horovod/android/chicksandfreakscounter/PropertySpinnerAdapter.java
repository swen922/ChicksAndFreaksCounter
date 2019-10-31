package com.horovod.android.chicksandfreakscounter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PropertySpinnerAdapter extends ArrayAdapter<String> {

    private Context myContext;
    private LayoutInflater inflater;
    private List<String> objectsList;
    private int textViewID;
    private DudeType myDudeType;


    public PropertySpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> obj, LayoutInflater inflater, DudeType myDudeType) {
        super(context, resource, textViewResourceId, obj);
        this.myContext = context;
        this.inflater = inflater;
        this.objectsList = new ArrayList<>(obj);
        this.textViewID = textViewResourceId;
        this.myDudeType = myDudeType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        TextView textView = null;


        /** Не показывает закругленный угол */


        if (myDudeType.equals(DudeType.CHICK)) {
            row = inflater.inflate(R.layout.spinner_row_chick, parent, false);
            textView = row.findViewById(textViewID);
        }
        else {
            row = inflater.inflate(R.layout.spinner_row_freak, parent, false);
            textView = row.findViewById(textViewID);
        }
        textView.setText(objectsList.get(position));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row;
        TextView textView;
        if (myDudeType.equals(DudeType.CHICK)) {
            row = inflater.inflate(R.layout.spinner_row_chick_dropdown, parent, false);
            textView = row.findViewById(R.id.spinner_row_textview_dropdown_chick);
        }
        else {
            row = inflater.inflate(R.layout.spinner_row_freak_dropdown, parent, false);
            textView = row.findViewById(R.id.spinner_row_textview_dropdown_freak);
        }
        textView.setText(objectsList.get(position));
        if (objectsList.get(position).equalsIgnoreCase(myContext.getResources().getString(R.string.spinner_edit))) {
            textView.setTypeface(null, Typeface.BOLD);
            if (myDudeType.equals(DudeType.CHICK)) {
                textView.setTextColor(myContext.getResources().getColor(R.color.colorAccent));
            }
            else {
                textView.setTextColor(myContext.getResources().getColor(R.color.colorBlueGrayAccent));
            }
        }
        return row;
    }

}

package com.horovod.android.chicksandfreakscounter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DudeAdapter extends ArrayAdapter<Dude> {

    private Context context;
    private int resourceID;
    private FragmentManager fragmentManager;

    public DudeAdapter(Context context, int resource, List<Dude> objects, FragmentManager fm) {
        super(context, resource, objects);
        this.context = context;
        this.resourceID = resource;
        this.fragmentManager = fm;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);

        DudeAdapter.ViewHolder viewHolder;
        Dude dude = getItem(position);
        int spinnerSelectedPosition = dude.getSpinnerSelectedPosition();
        String spinnerSelected;
        String descr = dude.getDescription();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            viewHolder = new DudeAdapter.ViewHolder();
            convertView = inflater.inflate(resourceID, parent, false);
            viewHolder.colorPosition = convertView.findViewById(R.id.list_item_color_idnumber);
            viewHolder.positionTextView = convertView.findViewById(R.id.list_item_textView_idnumber);
            viewHolder.headerTextView = convertView.findViewById(R.id.list_item_textView_description_header);
            viewHolder.deleteCrossImageView = convertView.findViewById(R.id.list_item_delete_cross);
            viewHolder.colorDescription = convertView.findViewById(R.id.list_item_color_description);
            viewHolder.spinnerTextView = convertView.findViewById(R.id.list_item_textView_spinner);
            viewHolder.descriptionTextView = convertView.findViewById(R.id.list_item_textView_description);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (DudeAdapter.ViewHolder) convertView.getTag();
        }

        if (dude.getDudeType().equals(DudeType.CHICK.toString())) {
            if (Data.getChicksSpinner().isEmpty()) {
                spinnerSelected = getContext().getResources().getStringArray(R.array.chicks_string_array)[spinnerSelectedPosition];
            }
            else {
                spinnerSelected = Data.getChicksSpinner().get(spinnerSelectedPosition);
            }
            viewHolder.colorPosition.setBackground(context.getResources().getDrawable(R.drawable.list_item_color_counter_chick));
            viewHolder.positionTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryLight));
            viewHolder.colorDescription.setBackground(context.getResources().getDrawable(R.drawable.list_item_color_chick));
            viewHolder.headerTextView.setTextColor(context.getResources().getColor(R.color.colorOrangeDark));
            viewHolder.headerTextView.setText(context.getResources().getString(R.string.list_item_chick));
            viewHolder.deleteCrossImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_close_red_24dp));
        }
        else {
            if (Data.getFreaksSpinner().isEmpty()) {
                spinnerSelected = getContext().getResources().getStringArray(R.array.freaks_string_array)[spinnerSelectedPosition];
            }
            else {
                spinnerSelected = Data.getFreaksSpinner().get(spinnerSelectedPosition);
            }
            viewHolder.colorPosition.setBackground(context.getResources().getDrawable(R.drawable.list_item_color_counter_freak));
            viewHolder.positionTextView.setTextColor(context.getResources().getColor(R.color.colorBlueGrayLight));
            viewHolder.colorDescription.setBackground(context.getResources().getDrawable(R.drawable.list_item_color_freak));
            viewHolder.headerTextView.setTextColor(context.getResources().getColor(R.color.colorBlueGrayDark));
            viewHolder.headerTextView.setText(context.getResources().getString(R.string.list_item_freak));
            viewHolder.deleteCrossImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_close_black_24dp));
        }

        int size = Data.getDudes().size();
        viewHolder.positionTextView.setText(String.valueOf(size - position));
        viewHolder.spinnerTextView.setText(spinnerSelected);
        viewHolder.descriptionTextView.setText(descr);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                DudeFragment dudeFragment = new DudeFragment();
                Bundle args = new Bundle();
                args.putInt(Data.KEY_IDNUMBER, position);
                dudeFragment.setArguments(args);
                ft.add(R.id.container_main, dudeFragment, null);
                ft.commit();
            }
        });

        viewHolder.deleteCrossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialogDelete dialog = new AlertDialogDelete();
                dialog.setCancelable(false);
                Bundle args = new Bundle();
                args.putInt(Data.KEY_IDNUMBER, position);
                dialog.setArguments(args);
                dialog.show(fragmentManager, null);

            }
        });

        return convertView;

    }


    public class ViewHolder {
        TextView colorPosition;
        TextView positionTextView;
        TextView headerTextView;
        ImageView deleteCrossImageView;
        TextView colorDescription;
        TextView spinnerTextView;
        TextView descriptionTextView;
    }

}

package com.horovod.android.chicksandfreakscounter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerEditFragment extends Fragment {

    private TextView background;
    private TextView topColorTextView;
    private TextView headerTextView;
    private TextView itemEditText0;
    private TextView itemEditText1;
    private TextView itemEditText2;
    private TextView itemEditText3;
    private TextView itemEditText4;
    private TextView itemEditText5;
    private TextView itemEditText6;
    private TextView revertItems;
    private Button cancelButton;
    private Button saveButton;

    private List<String> spinnerItemsList;
    private String dudeTypeString = "";
    private BroadcastReceiver spinnerItemReceiver;

    private int selectedPosition = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.spinner_edit_fragment, container, false);
        Data.spinnerEditFragment = this;

        background = rootView.findViewById(R.id.spinner_edit_fragment_background);
        topColorTextView = rootView.findViewById(R.id.spinner_edit_color_top);
        headerTextView = rootView.findViewById(R.id.spinner_edit_textview_header);

        itemEditText0 = rootView.findViewById(R.id.spinner_edit_fragment_edittext0);
        itemEditText1 = rootView.findViewById(R.id.spinner_edit_fragment_edittext1);
        itemEditText2 = rootView.findViewById(R.id.spinner_edit_fragment_edittext2);
        itemEditText3 = rootView.findViewById(R.id.spinner_edit_fragment_edittext3);
        itemEditText4 = rootView.findViewById(R.id.spinner_edit_fragment_edittext4);
        itemEditText5 = rootView.findViewById(R.id.spinner_edit_fragment_edittext5);
        itemEditText6 = rootView.findViewById(R.id.spinner_edit_fragment_edittext6);
        revertItems = rootView.findViewById(R.id.spinner_edit_fragment_revert);
        cancelButton = rootView.findViewById(R.id.spinner_edit_fragment_cancel_button);
        saveButton = rootView.findViewById(R.id.spinner_edit_fragment_save_button);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do - prevent clicks on background View through our SpinnerEditFragment
            }
        });



        Bundle args = getArguments();
        if (args != null) {
            dudeTypeString = args.getString(Data.KEY_DUDETYPE);
            selectedPosition = args.getInt(Data.KEY_PREVIOUS_ITEM, 0);
        }
        if (dudeTypeString != null && !dudeTypeString.isEmpty()) {
            if (dudeTypeString.equalsIgnoreCase(DudeType.CHICK.toString())) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    topColorTextView.setBackground(getContext().getResources().getDrawable(R.drawable.background_fragment_spinner_edit_top_chick));
                }
                else {
                    topColorTextView.setBackground(getContext().getResources().getDrawable(R.drawable.land_background_fragment_spinner_edit_top_chick));
                }
                headerTextView.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                background.setBackground(getResources().getDrawable(R.drawable.background_fragment_spinner_edit_chick));
                initSpinnerItems();
                revertItems.setTextColor(getResources().getColor(R.color.colorPrimaryMedium));
            }
            else {

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    topColorTextView.setBackground(getContext().getResources().getDrawable(R.drawable.background_fragment_spinner_edit_top_freak));
                }
                else {
                    topColorTextView.setBackground(getContext().getResources().getDrawable(R.drawable.land_background_fragment_spinner_edit_top_freak));
                }

                headerTextView.setTextColor(getResources().getColor(R.color.colorBlueGrayLight));
                background.setBackground(getResources().getDrawable(R.drawable.background_fragment_spinner_edit_freak));
                initSpinnerItems();
                revertItems.setTextColor(getResources().getColor(R.color.colorBlueGrayPrimary));
            }


            itemEditText0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(0, itemEditText0.getText().toString());
                }
            });
            itemEditText1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(1, itemEditText1.getText().toString());
                }
            });
            itemEditText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(2, itemEditText2.getText().toString());
                }
            });
            itemEditText3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(3, itemEditText3.getText().toString());
                }
            });
            itemEditText4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(4, itemEditText4.getText().toString());
                }
            });
            itemEditText5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(5, itemEditText5.getText().toString());
                }
            });
            itemEditText6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    openEditItemFragment(6, itemEditText6.getText().toString());
                }
            });

        }

        spinnerItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra(Data.KEY_IDNUMBER, 0);
                String newItem = intent.getStringExtra(Data.KEY_SPINNER_ITEM);
                switch (position) {
                    case 0 :
                        itemEditText0.setText(newItem);
                        break;
                    case 1 :
                        itemEditText1.setText(newItem);
                        break;
                    case 2 :
                        itemEditText2.setText(newItem);
                        break;
                    case 3 :
                        itemEditText3.setText(newItem);
                        break;
                    case 4 :
                        itemEditText4.setText(newItem);
                        break;
                    case 5 :
                        itemEditText5.setText(newItem);
                        break;
                    case 6 :
                        itemEditText6.setText(newItem);
                        break;
                }
            }
        };
        IntentFilter intentFilterSpinnerItem = new IntentFilter(Data.KEY_SPINNER_UPDATE_ITEM);
        getContext().registerReceiver(spinnerItemReceiver, intentFilterSpinnerItem);

        revertItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dudeTypeString != null && !dudeTypeString.isEmpty()) {
                    if (dudeTypeString.equalsIgnoreCase(DudeType.CHICK.toString())) {
                        spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.chicks_string_array)));
                    }
                    else {
                        spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.freaks_string_array)));
                    }
                    itemEditText0.setText(spinnerItemsList.get(0));
                    itemEditText1.setText(spinnerItemsList.get(1));
                    itemEditText2.setText(spinnerItemsList.get(2));
                    itemEditText3.setText(spinnerItemsList.get(3));
                    itemEditText4.setText(spinnerItemsList.get(4));
                    itemEditText5.setText(spinnerItemsList.get(5));
                    itemEditText6.setText(spinnerItemsList.get(6));
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(Data.spinnerEditFragment).commit();
                Data.spinnerEditFragment = null;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerItemsList.clear();
                spinnerItemsList.add(itemEditText0.getText().toString());
                spinnerItemsList.add(itemEditText1.getText().toString());
                spinnerItemsList.add(itemEditText2.getText().toString());
                spinnerItemsList.add(itemEditText3.getText().toString());
                spinnerItemsList.add(itemEditText4.getText().toString());
                spinnerItemsList.add(itemEditText5.getText().toString());
                spinnerItemsList.add(itemEditText6.getText().toString());

                Loader loader = new Loader(getContext());
                if (dudeTypeString.equalsIgnoreCase(DudeType.CHICK.toString())) {
                    Data.setChicksSpinner(spinnerItemsList);
                    loader.writeChickSpinnerToJSON();
                }
                else {
                    Data.setFreaksSpinner(spinnerItemsList);
                    loader.writeFreakSpinnerToJSON();
                }

                Intent intent = new Intent(Data.KEY_SPINNER_EDIT);
                intent.putExtra(Data.KEY_PREVIOUS_ITEM, selectedPosition);
                getActivity().sendBroadcast(intent);

                getActivity().getSupportFragmentManager().beginTransaction().remove(Data.spinnerEditFragment).commit();
                Data.spinnerEditFragment = null;
            }
        });

        return rootView;
    }


    private void openEditItemFragment(int position, String editItem) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Data.spinnerEditItemFragment = new SpinnerEditItemFragment();

        Bundle args = new Bundle();
        args.putInt(Data.KEY_IDNUMBER, position);
        args.putString(Data.KEY_DUDETYPE, dudeTypeString);
        args.putString(Data.KEY_SPINNER_EDIT_ITEM, editItem);
        Data.spinnerEditItemFragment.setArguments(args);

        ft.add(R.id.container_main, Data.spinnerEditItemFragment, null);
        ft.commit();
    }

    private void initSpinnerItems() {
        if (dudeTypeString != null && !dudeTypeString.isEmpty()) {
            if (dudeTypeString.equalsIgnoreCase(DudeType.CHICK.toString())) {
                if (Data.getChicksSpinner().isEmpty()) {
                    spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.chicks_string_array)));
                }
                else {
                    spinnerItemsList = new ArrayList<>(Data.getChicksSpinner());
                }
            }
            else {
                if (Data.getFreaksSpinner().isEmpty()) {
                    spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.freaks_string_array)));
                }
                else {
                    spinnerItemsList = new ArrayList<>(Data.getFreaksSpinner());
                }
            }

            itemEditText0.setText(spinnerItemsList.get(0));
            itemEditText1.setText(spinnerItemsList.get(1));
            itemEditText2.setText(spinnerItemsList.get(2));
            itemEditText3.setText(spinnerItemsList.get(3));
            itemEditText4.setText(spinnerItemsList.get(4));
            itemEditText5.setText(spinnerItemsList.get(5));
            itemEditText6.setText(spinnerItemsList.get(6));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(spinnerItemReceiver);
    }

}

package com.horovod.android.chicksandfreakscounter;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DudeFragment extends Fragment {

    private TextView background;
    private TextView backgroundLeft;
    private TextView headerColor;
    private TextView headerTextView;
    private TextView indexTextView;
    private Spinner propertySpinner;
    private TextView dateTextView;
    private TextView promptTextView;
    private EditText descriptionEditText;
    private ImageView shareImageView;
    private ImageView deleteImageView;
    private Button cancelButton;
    private Button saveButton;

    private Dude myDude;
    private List<String> spinnerItemsList;
    private ArrayAdapter<String> spinnerAdapter;

    private String selectedSpinnerItem = "";
    private int selectedSpinnerPosition = 0;
    private int dudeID = -1;

    private BroadcastReceiver spinnerEditReceiver;


    public String getDescriptionInEditText() {
        return descriptionEditText.getText().toString();
    }

    public TextView getPromptTextView() {
        return promptTextView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.dude_edit_fragment, container, false);
        Data.dudeFragment = this;

        background = rootView.findViewById(R.id.dude_fragment_background);
        headerColor = rootView.findViewById(R.id.dude_fragment_color_header);
        headerTextView = rootView.findViewById(R.id.dude_fragment_textview_header);
        indexTextView = rootView.findViewById(R.id.dude_fragment_textview_index);
        propertySpinner = rootView.findViewById(R.id.dude_fragment_spinner_property);
        dateTextView = rootView.findViewById(R.id.dude_fragment_date_textview);
        promptTextView = rootView.findViewById(R.id.dude_fragment_prompt_info);
        descriptionEditText = rootView.findViewById(R.id.dude_fragment_edittext_info);
        shareImageView = rootView.findViewById(R.id.dude_fragment_icon_share);
        deleteImageView = rootView.findViewById(R.id.dude_fragment_icon_delete);
        cancelButton = rootView.findViewById(R.id.dude_fragment_cancel_button);
        saveButton = rootView.findViewById(R.id.dude_fragment_save_button);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do - prevent clicks on ListView through our DudeFragment
            }
        });

        Bundle args = getArguments();

        if (args != null) {
            dudeID = args.getInt(Data.KEY_IDNUMBER, -1);
            if (dudeID >= 0) {
                myDude = Data.getDude(dudeID);
            }
        }

        if (myDude != null) {

            if (myDude.getDudeType().equals(DudeType.CHICK.toString())) {
                headerTextView.setText(getResources().getString(R.string.list_item_chick));
                headerTextView.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                background.setBackground(getResources().getDrawable(R.drawable.background_fragment_chick));

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    headerColor.setBackground(getResources().getDrawable(R.drawable.background_fragment_top_chick));
                }
                else {
                    backgroundLeft = rootView.findViewById(R.id.land_dude_fragment_background_left);
                    backgroundLeft.setBackground(getResources().getDrawable(R.drawable.land_background_fragment_left_chick));
                    headerColor.setBackgroundColor(getResources().getColor(R.color.colorOrangeDark));
                }

                indexTextView.setBackground(getResources().getDrawable(R.drawable.background_fragment_index_chick));
                indexTextView.setTextColor(getResources().getColor(R.color.colorOrangeDark));
                dateTextView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                shareImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_24dp_red));
                deleteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel_32dp_red));
                initSpinner(myDude.getDudeType());
            }
            else {
                headerTextView.setText(getResources().getString(R.string.list_item_freak));
                headerTextView.setTextColor(getResources().getColor(R.color.colorBlueGrayLight));
                background.setBackground(getResources().getDrawable(R.drawable.background_fragment_freak));

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    headerColor.setBackground(getResources().getDrawable(R.drawable.background_fragment_top_freak));
                }
                else {
                    backgroundLeft = rootView.findViewById(R.id.land_dude_fragment_background_left);
                    backgroundLeft.setBackground(getResources().getDrawable(R.drawable.land_background_fragment_left_freak));
                    headerColor.setBackgroundColor(getResources().getColor(R.color.colorBlueGrayDark));
                }

                indexTextView.setBackground(getResources().getDrawable(R.drawable.background_fragment_index_freak));
                indexTextView.setTextColor(getResources().getColor(R.color.colorBlueGrayDark));
                dateTextView.setBackgroundColor(getResources().getColor(R.color.colorBlueGrayMedium));
                shareImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_24dp_gray));
                deleteImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel_32dp_gray));
                initSpinner(myDude.getDudeType());
            }

            indexTextView.setText(String.valueOf(Data.getDudes().size() - dudeID));

            String dt = getString(R.string.dude_date_time) + " " + myDude.getDateString();
            dateTextView.setText(dt);

            propertySpinner.setSelection(myDude.getSpinnerSelectedPosition());

            descriptionEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    promptTextView.setVisibility(View.INVISIBLE);
                    return false;

                }
            });

            descriptionEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String descr = descriptionEditText.getText().toString();
                    if (descr != null && !descr.isEmpty()) {
                        promptTextView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        promptTextView.setVisibility(View.VISIBLE);
                    }
                }
            });



            String description = myDude.getDescription();
            if (description == null || description.isEmpty()) {
                promptTextView.setVisibility(View.VISIBLE);
            }
            else {
                descriptionEditText.setText(myDude.getDescription());
                promptTextView.setVisibility(View.INVISIBLE);
            }

            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    StringBuilder sb = new StringBuilder(headerTextView.getText().toString().toUpperCase());
                    sb.append(" - ").append(indexTextView.getText().toString()).append("\n");
                    sb.append(propertySpinner.getSelectedItem().toString()).append("\n");
                    sb.append(dateTextView.getText().toString()).append("\n");
                    sb.append(descriptionEditText.getText().toString());
                    intent.putExtra(Intent.EXTRA_TEXT, sb.toString());

                    startActivity(Intent.createChooser(intent, null));
                }
            });

            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialogDelete dialog = new AlertDialogDelete();
                    dialog.setCancelable(false);
                    Bundle args = new Bundle();
                    args.putInt(Data.KEY_IDNUMBER, dudeID);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager(), null);

                }
            });


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descriptionEditText.setText(myDude.getDescription());
                    getActivity().getSupportFragmentManager().beginTransaction().remove(Data.dudeFragment).commit();
                    closeKeyboard();
                    Data.dudeFragment = null;
                }
            });

            final int dudeIDfin = dudeID;

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputDescription = descriptionEditText.getText().toString();
                    inputDescription = Util.clearGaps(inputDescription);
                    int spinnerPosition = getPositionOfSpinnerItem(propertySpinner.getSelectedItem().toString());

                    Intent intent = new Intent(Data.KEY_UPDATE_DUDE);
                    intent.putExtra(Data.KEY_IDNUMBER, dudeIDfin);
                    intent.putExtra(Data.KEY_DESCRIPTION, inputDescription);
                    intent.putExtra(Data.KEY_SPINNER, spinnerPosition);
                    getActivity().sendBroadcast(intent);

                    getActivity().getSupportFragmentManager().beginTransaction().remove(Data.dudeFragment).commit();
                    closeKeyboard();
                    Data.dudeFragment = null;
                }
            });

            propertySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (spinnerItemsList.get(position).equalsIgnoreCase(getString(R.string.spinner_edit))) {

                        closeKeyboard();

                        if (selectedSpinnerItem.isEmpty()) {
                            propertySpinner.setSelection(myDude.getSpinnerSelectedPosition());
                        }
                        else {
                            propertySpinner.setSelection(selectedSpinnerPosition);
                        }

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        Data.spinnerEditFragment = new SpinnerEditFragment();

                        Bundle args = new Bundle();
                        args.putString(Data.KEY_DUDETYPE, myDude.getDudeType());
                        if (!selectedSpinnerItem.isEmpty()) {
                            args.putInt(Data.KEY_PREVIOUS_ITEM, selectedSpinnerPosition);
                        }
                        Data.spinnerEditFragment.setArguments(args);

                        ft.add(R.id.container_main, Data.spinnerEditFragment, null);
                        ft.commit();
                    }
                    else {
                        selectedSpinnerItem = propertySpinner.getSelectedItem().toString();
                        selectedSpinnerPosition = getPositionOfSpinnerItem(propertySpinner.getSelectedItem().toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerEditReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    selectedSpinnerPosition = intent.getIntExtra(Data.KEY_PREVIOUS_ITEM, 0);
                    initSpinner(myDude.getDudeType());
                    propertySpinner.setSelection(selectedSpinnerPosition);

                }
            };
            IntentFilter spinnerEditFilter = new IntentFilter(Data.KEY_SPINNER_EDIT);
            getActivity().registerReceiver(spinnerEditReceiver, spinnerEditFilter);
        }

        return rootView;
    }

    private int getPositionOfSpinnerItem(String line) {
        int selected = spinnerItemsList.indexOf(line);
        if (selected >= 0) {
            return selected;
        }
        return 0;
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (descriptionEditText != null) {
            imm.hideSoftInputFromWindow(descriptionEditText.getWindowToken(), 0);
        }
    }

    private void initSpinner(String dudeTypeString) {

        /*** В методе надо вызывать проверку привязки нашего фрашмента к фрагмент-менеджеру,
         * иначе приложение валится с исключением
         * Caused by: java.lang.IllegalStateException: onGetLayoutInflater()
         * cannot be executed until the Fragment is attached to the FragmentManager.
         * то есть строчки
         * Activity activity = getActivity();
         *         if (isAdded() && activity != null)*/

        Activity activity = getActivity();

        if (isAdded() && activity != null) {

            if (dudeTypeString.equalsIgnoreCase(DudeType.CHICK.toString())) {
                if (Data.getChicksSpinner().isEmpty()) {
                    spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.chicks_string_array)));
                }
                else {
                    spinnerItemsList = new ArrayList<>(Data.getChicksSpinner());
                }
                spinnerItemsList.add(getResources().getString(R.string.spinner_edit));
                spinnerAdapter = new PropertySpinnerAdapter(getContext(), R.layout.spinner_row_chick, R.id.spinner_row_textview_chick, spinnerItemsList, getLayoutInflater(), DudeType.CHICK);
            }
            else {
                if (Data.getFreaksSpinner().isEmpty()) {
                    spinnerItemsList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.freaks_string_array)));
                }
                else {
                    spinnerItemsList = new ArrayList<>(Data.getFreaksSpinner());
                }
                spinnerItemsList.add(getResources().getString(R.string.spinner_edit));
                spinnerAdapter = new PropertySpinnerAdapter(getContext(), R.layout.spinner_row_freak, R.id.spinner_row_textview_freak, spinnerItemsList, getLayoutInflater(), DudeType.FREAK);
            }
            propertySpinner.setAdapter(spinnerAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(spinnerEditReceiver);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        String descr = descriptionEditText.getText().toString();
        if (descr != null && !descr.isEmpty()) {
            promptTextView.setVisibility(View.INVISIBLE);
        }
    }

}

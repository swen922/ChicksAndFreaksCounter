package com.horovod.android.chicksandfreakscounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DudeFragmentInput extends Fragment {

    private TextView background;
    private EditText inputDescriptionEditText;
    private Button cancelButton;
    private Button saveButton;

    private String text;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.dude_edit_input_fragment, container, false);
        background = rootView.findViewById(R.id.dude_fragment_input_background);
        inputDescriptionEditText = rootView.findViewById(R.id.dude_fragment_input_edittext);
        cancelButton = rootView.findViewById(R.id.dude_fragment_input_cancel_button);
        saveButton = rootView.findViewById(R.id.dude_fragment_input_save_button);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nothing to do - prevent clicks on ListView through our DudeFragment
            }
        });

        Bundle args = getArguments();

        if (args != null) {
            text = args.getString(Data.KEY_DESCRIPTION_ITEM);
            if (text != null && !text.isEmpty()) {
                inputDescriptionEditText.setText(text);
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(Data.dudeFragmentInput).commit();
                closeKeyboard();
                Data.dudeFragmentInput = null;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputDescription = inputDescriptionEditText.getText().toString();
                inputDescription = Util.clearGaps(inputDescription);

                Intent intent = new Intent(Data.KEY_DESCRIPTION_EDIT);
                intent.putExtra(Data.KEY_DESCRIPTION_TEXT, inputDescription);
                getActivity().sendBroadcast(intent);

                getActivity().getSupportFragmentManager().beginTransaction().remove(Data.dudeFragmentInput).commit();
                closeKeyboard();
                Data.dudeFragmentInput = null;
            }
        });
        return rootView;
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputDescriptionEditText != null) {
            imm.hideSoftInputFromWindow(inputDescriptionEditText.getWindowToken(), 0);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Data.dudeFragmentInput = this;
    }
}

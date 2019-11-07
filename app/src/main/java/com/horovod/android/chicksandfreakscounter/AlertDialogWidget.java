package com.horovod.android.chicksandfreakscounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class AlertDialogWidget extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.dialog_widget_title));

        //View view = getLayoutInflater().inflate(R.layout.alert_widget, null);
        //builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}

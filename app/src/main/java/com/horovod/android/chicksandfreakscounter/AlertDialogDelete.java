package com.horovod.android.chicksandfreakscounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class AlertDialogDelete extends DialogFragment {

    private int index;
    private int size = Data.getDudes().size();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        index = getArguments().getInt(Data.KEY_IDNUMBER);
        String title = getString(R.string.delete_alert) + " " + (size - index) + "?";
        builder.setTitle(title);
        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Data.dudeFragment != null) {
                    getFragmentManager().beginTransaction().remove(Data.dudeFragment).commit();
                    Data.dudeFragment = null;
                }
                Intent intent = new Intent(Data.KEY_DELETE_DUDE);
                intent.putExtra(Data.KEY_IDNUMBER, (index));
                getActivity().sendBroadcast(intent);
            }
        });
        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show(); /*** ОБЯЗАТЕЛЬНО!!! Вначале вызывать метод dialog.show() Иначе кнопки будут = null   ***/

        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlueGrayDark));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlueGrayDark));

        //return builder.create();
        return dialog;

    }

}

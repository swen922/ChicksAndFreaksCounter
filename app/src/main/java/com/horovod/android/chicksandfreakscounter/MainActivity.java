package com.horovod.android.chicksandfreakscounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Loader loader;

    private TextView freaksCounterHeader;
    private TextView chicksCounterHeader;
    private TextView freaksCounter;
    private TextView chicksCounter;
    private TextView addFreakTextView;
    private TextView addChickTextView;
    private ListView listView;

    private ArrayAdapter<Dude> adapter;
    private BroadcastReceiver createReceiver;
    private BroadcastReceiver editReceiver;
    private BroadcastReceiver deleteReceiver;
    private BroadcastReceiver spinnerEditReceiver;
    private BroadcastReceiver clearListReceiver;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        View view = getLayoutInflater().inflate(R.layout.my_action_bar, null);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setCustomView(view);


        listView = findViewById(R.id.listview_main);
        freaksCounterHeader = findViewById(R.id.textview_counter_freaks_header);
        chicksCounterHeader = findViewById(R.id.textview_counter_chicks_header);
        freaksCounter = findViewById(R.id.textview_counter_freaks);
        chicksCounter = findViewById(R.id.textview_counter_chicks);
        addFreakTextView = findViewById(R.id.textview_add_freak);
        addChickTextView = findViewById(R.id.textview_add_chick);


        loader = new Loader(getApplicationContext());
        loader.readBaseFromJSON();
        loader.readChickSpinnerFromJSON();
        loader.readFreakSpinnerFromJSON();

        updateCounters();

        if (adapter == null) {
            adapter = new DudeAdapter(this, R.layout.list_item, Data.getDudes(), fragmentManager);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addFreakTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFreak();
            }
        });

        addChickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChick();
            }
        });


        if (createReceiver == null) {
            createReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    String createDudeType = intent.getStringExtra(Data.KEY_DUDETYPE);
                    String descr = intent.getStringExtra(Data.KEY_DESCRIPTION);
                    int spinner = intent.getIntExtra(Data.KEY_SPINNER, 0);

                    if (createDudeType.equalsIgnoreCase(DudeType.CHICK.toString())) {
                        Data.createDude(DudeType.CHICK.toString(), descr, spinner);
                    }
                    else {
                        Data.createDude(DudeType.FREAK.toString(), descr, spinner);
                    }

                    intent.setAction("");
                    loader.writeBaseToJSON();
                    adapter.notifyDataSetChanged();
                    updateCounters();
                    updateWidget();

                }
            };
        }

        if (editReceiver == null) {
            editReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    int id = intent.getIntExtra(Data.KEY_IDNUMBER, -1);

                    if (id >= 0) {
                        Dude dude = Data.getDude(id);
                        String newDescription = intent.getStringExtra(Data.KEY_DESCRIPTION);
                        int newSpinner = intent.getIntExtra(Data.KEY_SPINNER, 0);
                        dude.setDescription(newDescription);
                        dude.setSpinnerSelectedPosition(newSpinner);
                        intent.setAction("");
                        loader.writeBaseToJSON();
                        adapter.notifyDataSetChanged();
                        updateCounters();
                        updateWidget();
                    }
                }
            };
        }

        if (deleteReceiver == null) {
            deleteReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    int id = intent.getIntExtra(Data.KEY_IDNUMBER, -1);
                    int size = Data.getDudes().size();

                    if (id >= 0) {
                        boolean result = Data.removeDude(id);
                        if (result) {
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_success1) + " " + (size - id) + " " + getString(R.string.delete_success2), Toast.LENGTH_LONG).show();
                            loader.writeBaseToJSON();
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_fail) + " " + (size - id), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.delete_fail) + " " + (size - id), Toast.LENGTH_LONG).show();
                    }
                    intent.setAction("");
                    updateCounters();
                    updateWidget();
                }
            };
        }

        if (spinnerEditReceiver == null) {
            spinnerEditReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    intent.setAction("");
                    adapter.notifyDataSetChanged();
                }
            };
        }

        if (clearListReceiver == null) {
            clearListReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (Data.spinnerEditItemFragment != null) {
                        fragmentManager.beginTransaction().remove(Data.spinnerEditItemFragment).commit();
                        Data.spinnerEditItemFragment = null;
                    }
                    if (Data.spinnerEditFragment != null) {
                        fragmentManager.beginTransaction().remove(Data.spinnerEditFragment).commit();
                        Data.spinnerEditFragment = null;
                    }
                    if (Data.createFragment != null) {
                        fragmentManager.beginTransaction().remove(Data.createFragment).commit();
                        Data.createFragment = null;
                    }
                    if (Data.dudeFragment != null) {
                        fragmentManager.beginTransaction().remove(Data.dudeFragment).commit();
                        Data.dudeFragment = null;
                    }
                    intent.setAction("");
                    Data.clearDudes();
                    updateCounters();
                    updateWidget();
                    loader.writeBaseToJSON();
                    adapter.notifyDataSetChanged();
                }
            };
        }


        IntentFilter intentFilterCreate = new IntentFilter(Data.KEY_CREATE_DUDE);
        registerReceiver(createReceiver, intentFilterCreate);
        IntentFilter intentFilterEdit = new IntentFilter(Data.KEY_UPDATE_DUDE);
        registerReceiver(editReceiver, intentFilterEdit);
        IntentFilter intentFilterDelete = new IntentFilter(Data.KEY_DELETE_DUDE);
        registerReceiver(deleteReceiver, intentFilterDelete);
        IntentFilter spinnerEditFilter = new IntentFilter(Data.KEY_SPINNER_EDIT);
        registerReceiver(spinnerEditReceiver, spinnerEditFilter);
        IntentFilter intentFilterClear = new IntentFilter(Data.KEY_CLEAR_LIST);
        registerReceiver(clearListReceiver, intentFilterClear);


        // Интент из виджета с командой создать чувака или чувиху
        Intent intent = getIntent();
        if (intent != null) {
            if (Data.KEY_CREATE_CHICK.equalsIgnoreCase(intent.getAction())) {
                intent.setFlags(0);
                intent.setAction("");
                addChick();
            }
            else if (Data.KEY_CREATE_FREAK.equalsIgnoreCase(intent.getAction())) {
                intent.setFlags(0);
                intent.setAction("");
                addFreak();
            }
        }

    }



    private void updateCounters() {
        if (!Data.getDudes().isEmpty()) {
            int freaks = 0;
            for (Dude dude : Data.getDudes()) {
                if (dude.getDudeType().equals(DudeType.FREAK.toString())) {
                    freaks++;
                }
            }
            freaksCounter.setText(String.valueOf(freaks));
            chicksCounter.setText(String.valueOf(Data.getDudes().size() - freaks));
        }
        else {
            freaksCounter.setText(String.valueOf(0));
            chicksCounter.setText(String.valueOf(0));
        }
    }

    private void addFreak() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Data.createFragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(Data.KEY_DUDETYPE, DudeType.FREAK.toString());
        Data.createFragment.setArguments(args);
        ft.add(R.id.container_main, Data.createFragment, null);
        ft.commit();
    }

    private void addChick() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Data.createFragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(Data.KEY_DUDETYPE, DudeType.CHICK.toString());
        Data.createFragment.setArguments(args);
        ft.add(R.id.container_main, Data.createFragment, null);
        ft.commit();
    }

    private void updateWidget() {
        Intent intent = new Intent(MainActivity.this, ChicksAndFreaksWidget.class);
        intent.setAction(Data.KEY_UPDATE_WIDGET);
        intent.putExtra(Data.KEY_NUMBER_FREAK, freaksCounter.getText().toString());
        intent.putExtra(Data.KEY_NUMBER_CHICK, chicksCounter.getText().toString());
        sendBroadcast(intent);
    }

    public void onClickMenuDelete(MenuItem item) {
        AlertDialogClear dialogClear = new AlertDialogClear();
        dialogClear.setCancelable(false);
        dialogClear.show(getSupportFragmentManager(), null);
    }

    public void onClickMenuWidget(MenuItem item) {
        /*AlertDialogWidget dialogWidget = new AlertDialogWidget();
        dialogWidget.setCancelable(true);
        dialogWidget.show(getSupportFragmentManager(), null);*/

        showDialog(1);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        //return super.onCreateDialog(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle(getResources().getString(R.string.dialog_widget_title));

        View view = getLayoutInflater().inflate(R.layout.alert_widget, null);
        builder.setView(view);
        builder.setNeutralButton(getResources().getString(R.string.dialog_widget_button), null);

        /*TextView textView = view.findViewById(R.id.alert_widget_textview);
        textView.setText(getResources().getString(R.string.dialog_widget_text1));*/

        AlertDialog dialog = builder.create();

        dialog.show(); /*** ОБЯЗАТЕЛЬНО!!! Вначале вызывать метод dialog.show() Иначе кнопки будут = null   ***/
        dialog.getButton(dialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorBlueGrayDark));

        return dialog;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(createReceiver);
        unregisterReceiver(editReceiver);
        unregisterReceiver(deleteReceiver);
        unregisterReceiver(spinnerEditReceiver);
        unregisterReceiver(clearListReceiver);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // override OnBackPressed() for to close DudeFragment first, if it's opened now
    @Override
    public void onBackPressed() {

        if (Data.spinnerEditItemFragment != null) {
            fragmentManager.beginTransaction().remove(Data.spinnerEditItemFragment).commit();
            Data.spinnerEditItemFragment = null;
        }
        else if (Data.spinnerEditFragment != null) {
            fragmentManager.beginTransaction().remove(Data.spinnerEditFragment).commit();
            Data.spinnerEditFragment = null;
        }
        else if (Data.createFragment != null) {
            fragmentManager.beginTransaction().remove(Data.createFragment).commit();
            Data.createFragment = null;
        }
        else if (Data.dudeFragment != null) {
            fragmentManager.beginTransaction().remove(Data.dudeFragment).commit();
            Data.dudeFragment = null;
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Data.dudeFragment != null || Data.createFragment != null) {
            if (Data.spinnerEditFragment != null) {
                outState.putString(Data.KEY_SPINNER_ITEM0, Data.spinnerEditFragment.getItemEditText0());
                outState.putString(Data.KEY_SPINNER_ITEM1, Data.spinnerEditFragment.getItemEditText1());
                outState.putString(Data.KEY_SPINNER_ITEM2, Data.spinnerEditFragment.getItemEditText2());
                outState.putString(Data.KEY_SPINNER_ITEM3, Data.spinnerEditFragment.getItemEditText3());
                outState.putString(Data.KEY_SPINNER_ITEM4, Data.spinnerEditFragment.getItemEditText4());
                outState.putString(Data.KEY_SPINNER_ITEM5, Data.spinnerEditFragment.getItemEditText5());
                outState.putString(Data.KEY_SPINNER_ITEM6, Data.spinnerEditFragment.getItemEditText6());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (Data.dudeFragment != null || Data.createFragment != null) {
            if (Data.spinnerEditFragment != null) {
                Data.spinnerEditFragment.setItemEditText0(savedInstanceState.getString(Data.KEY_SPINNER_ITEM0));
                Data.spinnerEditFragment.setItemEditText1(savedInstanceState.getString(Data.KEY_SPINNER_ITEM1));
                Data.spinnerEditFragment.setItemEditText2(savedInstanceState.getString(Data.KEY_SPINNER_ITEM2));
                Data.spinnerEditFragment.setItemEditText3(savedInstanceState.getString(Data.KEY_SPINNER_ITEM3));
                Data.spinnerEditFragment.setItemEditText4(savedInstanceState.getString(Data.KEY_SPINNER_ITEM4));
                Data.spinnerEditFragment.setItemEditText5(savedInstanceState.getString(Data.KEY_SPINNER_ITEM5));
                Data.spinnerEditFragment.setItemEditText6(savedInstanceState.getString(Data.KEY_SPINNER_ITEM6));
            }
        }
    }

}

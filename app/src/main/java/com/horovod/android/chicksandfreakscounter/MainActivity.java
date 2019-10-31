package com.horovod.android.chicksandfreakscounter;

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


        final Loader loader = new Loader(getApplicationContext());
        loader.readBaseFromJSON();
        loader.readChickSpinnerFromJSON();
        loader.readFreakSpinnerFromJSON();

        updateCounters();

        adapter = new DudeAdapter(this, R.layout.list_item, Data.getDudes(), fragmentManager);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int addLength1 = getResources().getString(R.string.add_chick1).split("\n")[0].length();
        int addLength2 = getResources().getString(R.string.add_chick1).split("\n")[1].length();
        int addLength3 = getResources().getString(R.string.add_freak1).split("\n")[0].length();
        int addLength4 = getResources().getString(R.string.add_freak1).split("\n")[1].length();

        if ((addLength1 < 8) && (addLength2 < 8) && (addLength3 < 8) && (addLength4 < 8)) {
            freaksCounterHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            chicksCounterHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            addFreakTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            addChickTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        }

        // В черновую приложежку скопировал отсюда код установки высоты тектового блока - просто для шпаргалки


        addFreakTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Data.createFragment = new CreateFragment();
                Bundle args = new Bundle();
                args.putString(Data.KEY_DUDETYPE, DudeType.FREAK.toString());
                Data.createFragment.setArguments(args);
                ft.add(R.id.container_main, Data.createFragment, null);
                ft.commit();
            }
        });

        addChickTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Data.createFragment = new CreateFragment();
                Bundle args = new Bundle();
                args.putString(Data.KEY_DUDETYPE, DudeType.CHICK.toString());
                Data.createFragment.setArguments(args);
                ft.add(R.id.container_main, Data.createFragment, null);
                ft.commit();
            }
        });


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
                loader.writeBaseToJSON();
                adapter.notifyDataSetChanged();
                updateCounters();
            }
        };

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
                    loader.writeBaseToJSON();
                    adapter.notifyDataSetChanged();
                    updateCounters();
                }
            }
        };
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
                updateCounters();
            }
        };
        spinnerEditReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.notifyDataSetChanged();
            }
        };

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

                Data.clearDudes();
                updateCounters();
                loader.writeBaseToJSON();
                adapter.notifyDataSetChanged();
            }
        };

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClickMenuDelete(MenuItem item) {
        AlertDialogClear dialogClear = new AlertDialogClear();
        dialogClear.setCancelable(false);
        dialogClear.show(getSupportFragmentManager(), null);
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

}

package com.horovod.android.chicksandfreakscounter;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Data {

    private static LinkedList<Dude> dudes = new LinkedList<>();
    private static List<String> chicksSpinner = new ArrayList<>();
    private static List<String> freaksSpinner = new ArrayList<>();

    public static final String KEY_CREATE_DUDE = "com.horovod.android.chicksandfreakscounter_KEY_CREATE_DUDE";
    public static final String KEY_UPDATE_DUDE = "com.horovod.android.chicksandfreakscounter_KEY_UPDATE_DUDE";
    public static final String KEY_DELETE_DUDE = "com.horovod.android.chicksandfreakscounter_KEY_DELETE_DUDE";
    public static final String KEY_CLEAR_LIST = "com.horovod.android.chicksandfreakscounter_KEY_CLEAR_LIST";
    public static final String KEY_IDNUMBER = "com.horovod.android.chicksandfreakscounter_KEY_IDNUMBER";
    public static final String KEY_DESCRIPTION = "com.horovod.android.chicksandfreakscounter_KEY_DESCRIPTION";
    public static final String KEY_SPINNER = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER";
    public static final String KEY_SPINNER_ITEM = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM";
    public static final String KEY_DUDETYPE = "com.horovod.android.chicksandfreakscounter_KEY_DUDETYPE";
    public static final String KEY_SPINNER_EDIT = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_EDIT";
    public static final String KEY_SPINNER_EDIT_ITEM = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_EDIT_ITEM";
    public static final String KEY_SPINNER_UPDATE_ITEM = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_UPDATE_ITEM";
    public static final String KEY_PREVIOUS_ITEM = "com.horovod.android.chicksandfreakscounter_KEY_PREVIOUS_ITEM";

    public static final String KEY_CREATE_CHICK = "com.horovod.android.chicksandfreakscounter_KEY_CREATE_CHICK";
    public static final String KEY_CREATE_FREAK = "com.horovod.android.chicksandfreakscounter_KEY_CREATE_FREAK";
    public static final String KEY_UPDATE_WIDGET = "com.horovod.android.chicksandfreakscounter_KEY_UPDATE_WIDGET";
    public static final String KEY_NUMBER_CHICK = "com.horovod.android.chicksandfreakscounter_KEY_NUMBER_CHICK";
    public static final String KEY_NUMBER_FREAK = "com.horovod.android.chicksandfreakscounter_KEY_NUMBER_FREAK";


    public static final String KEY_SPINNER_ITEM0 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM0";
    public static final String KEY_SPINNER_ITEM1 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM1";
    public static final String KEY_SPINNER_ITEM2 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM2";
    public static final String KEY_SPINNER_ITEM3 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM3";
    public static final String KEY_SPINNER_ITEM4 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM4";
    public static final String KEY_SPINNER_ITEM5 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM5";
    public static final String KEY_SPINNER_ITEM6 = "com.horovod.android.chicksandfreakscounter_KEY_SPINNER_ITEM6";




    public static CreateFragment createFragment;
    public static DudeFragment dudeFragment;
    public static SpinnerEditFragment spinnerEditFragment;
    public static SpinnerEditItemFragment spinnerEditItemFragment;


    public static void createDude(String dt, String description, int spinnerPos) {
        Dude newDude = new Dude(dt, new Date(), description, spinnerPos);
        dudes.addFirst(newDude);
    }


    public static Dude getDude(int index) {
        if (dudes.size() > index) {
            return dudes.get(index);
        }
        return null;
    }

    public static Dude getDudeFirst() {
        if (!dudes.isEmpty()) {
            return dudes.getFirst();
        }
        return null;
    }

    public static Dude getDudeLast() {
        if (!dudes.isEmpty()) {
            return dudes.getLast();
        }
        return null;
    }

    public static void addDude(Dude dude) {
        dudes.add(dude);
    }

    public static void addDudeFirst(Dude dude) {
        dudes.addFirst(dude);
    }

    public static void addDudeLast(Dude dude) {
        dudes.addLast(dude);
    }

    public static boolean removeDude(int index) {
        if (dudes.size() > index) {
            dudes.remove(index);
            return true;
        }
        return false;
    }


    public static List<Dude> getDudes() {
        return dudes;
    }

    public static void setDudes(List<Dude> newDudes) {
        Data.dudes = new LinkedList<>(newDudes);
    }

    public static void clearDudes() {
        Data.dudes.clear();
    }

    public static List<String> getChicksSpinner() {
        return chicksSpinner;
    }

    public static void setChicksSpinner(List<String> chicksSpinner) {
        Data.chicksSpinner = new ArrayList<>(chicksSpinner);
    }

    public static List<String> getFreaksSpinner() {
        return freaksSpinner;
    }

    public static void setFreaksSpinner(List<String> freaksSpinner) {
        Data.freaksSpinner = new ArrayList<>(freaksSpinner);
    }

}

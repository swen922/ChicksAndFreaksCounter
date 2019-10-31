package com.horovod.android.chicksandfreakscounter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loader {

    private final String FILENAME = "FILENAME";
    private final String FILENAME_SPINNER_CHICK = "FILENAME_SPINNER_CHICK";
    private final String FILENAME_SPINNER_FREAK = "FILENAME_SPINNER_FREAK";


    private Context myContext;

    public Loader(Context myContext) {
        this.myContext = myContext;
    }

    public void writeBaseToJSON() {



        Gson gson = new Gson();
        String outputJSON = gson.toJson(new ArrayList<>(Data.getDudes()));

        try (FileOutputStream out =  myContext.openFileOutput(FILENAME, Context.MODE_PRIVATE)) {
            out.write(outputJSON.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readBaseFromJSON() {

        StringBuilder sb = new StringBuilder("");

        try (FileInputStream inn = myContext.openFileInput(FILENAME)) {
            if (inn.available() > 0) {
                byte[] input = new byte[inn.available()];
                while (inn.read(input) != -1) {
                    sb.append(new String(input));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input = sb.toString();
        if (!input.isEmpty()) {
            Gson gson = new Gson();
            List<Dude> inputList = gson.fromJson(input, new TypeToken<List<Dude>>(){}.getType());
            if (inputList != null) {
                Data.setDudes(inputList);
            }
        }
    }


    public void writeChickSpinnerToJSON() {

        Gson gson = new Gson();
        List<String> chicksSpinner;

        if (!Data.getChicksSpinner().isEmpty()) {
            chicksSpinner = new ArrayList<>(Data.getChicksSpinner());
        }
        else {
            chicksSpinner = new ArrayList<>(Arrays.asList(myContext.getResources().getStringArray(R.array.chicks_string_array)));
        }

        String outputChickSpinner = gson.toJson(chicksSpinner);

        try (FileOutputStream out =  myContext.openFileOutput(FILENAME_SPINNER_CHICK, Context.MODE_PRIVATE)) {
            out.write(outputChickSpinner.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeFreakSpinnerToJSON() {

        Gson gson = new Gson();
        List<String> freaksSpinner;

        if (!Data.getFreaksSpinner().isEmpty()) {
            freaksSpinner = new ArrayList<>(Data.getFreaksSpinner());
        }
        else {
            freaksSpinner = new ArrayList<>(Arrays.asList(myContext.getResources().getStringArray(R.array.freaks_string_array)));
        }

        String outputFreakSpinner = gson.toJson(freaksSpinner);

        try (FileOutputStream out =  myContext.openFileOutput(FILENAME_SPINNER_FREAK, Context.MODE_PRIVATE)) {
            out.write(outputFreakSpinner.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readChickSpinnerFromJSON() {

        StringBuilder sb = new StringBuilder("");

        try (FileInputStream inn = myContext.openFileInput(FILENAME_SPINNER_CHICK)) {
            if (inn.available() > 0) {
                byte[] input = new byte[inn.available()];
                while (inn.read(input) != -1) {
                    sb.append(new String(input));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input = sb.toString();

        if (!input.isEmpty()) {
            Gson gson = new Gson();
            List<String> inputList = gson.fromJson(input, new TypeToken<List<String>>(){}.getType());
            if (inputList != null) {
                Data.setChicksSpinner(inputList);
            }
        }
    }

    public void readFreakSpinnerFromJSON() {

        StringBuilder sb = new StringBuilder("");

        try (FileInputStream inn = myContext.openFileInput(FILENAME_SPINNER_FREAK)) {
            if (inn.available() > 0) {
                byte[] input = new byte[inn.available()];
                while (inn.read(input) != -1) {
                    sb.append(new String(input));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String input = sb.toString();

        if (!input.isEmpty()) {
            Gson gson = new Gson();
            List<String> inputList = gson.fromJson(input, new TypeToken<List<String>>(){}.getType());
            if (inputList != null) {
                Data.setFreaksSpinner(inputList);
            }
        }
    }

}
